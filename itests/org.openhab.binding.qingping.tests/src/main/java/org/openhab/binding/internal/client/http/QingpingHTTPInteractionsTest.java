package org.openhab.binding.internal.client.http;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Hashtable;
import java.util.Map;
import java.util.Properties;

import org.eclipse.jetty.client.HttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openhab.binding.qingping.internal.QingpingBindingConstants;
import org.openhab.binding.qingping.internal.client.http.QingpingHttpClient;
import org.openhab.binding.qingping.internal.client.http.QingpingOAuthClientProvider;
import org.openhab.binding.qingping.internal.client.http.QingpingOAuthClientService;
import org.openhab.binding.qingping.internal.client.http.QingpingServiceInteractionException;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.openhab.core.storage.StorageService;
import org.openhab.core.test.java.JavaOSGiTest;
import org.openhab.core.test.storage.VolatileStorageService;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QingpingHTTPInteractionsTest extends JavaOSGiTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final StorageService storageService = new VolatileStorageService();
    private HttpClientFactory httpClientFactory;
    private QingpingOAuthClientService oAuthClientService;

    @BeforeEach
    void setUp() throws IOException {
        final ConfigurationAdmin configurationAdmin = getService(ConfigurationAdmin.class);
        assertThat(configurationAdmin, is(notNullValue()));
        final Configuration configuration = configurationAdmin.getConfiguration("binding.qingping");

        final Properties properties = loadTestProperties();
        final Hashtable<String, String> configData = new Hashtable<>(
                Map.of(QingpingBindingConstants.APP_KEY_PARAMETER, properties.getProperty("app.key"),
                        QingpingBindingConstants.APP_SECRET_PARAMETER, properties.getProperty("app.secret")));

        configuration.update(configData);

        registerService(storageService);
        final QingpingOAuthClientProvider qingpingOAuthClientProvider = getService(QingpingOAuthClientProvider.class);
        assertThat(qingpingOAuthClientProvider, is(notNullValue()));
        this.httpClientFactory = getService(HttpClientFactory.class);

        this.oAuthClientService = qingpingOAuthClientProvider.getInstance();
    }

    @AfterEach
    void tearDown() throws Exception {
        oAuthClientService.close();
    }

    private Properties loadTestProperties() throws IOException {
        try (InputStream propertiesStream = this.getClass().getClassLoader()
                .getResourceAsStream("env/qingping-client-test.properties")) {
            assumeFalse(propertiesStream == null,
                    "There's no properties needed for communication with Qingping API. Please see README.md for more details.");
            final Properties properties = new Properties();
            properties.load(propertiesStream);
            return properties;
        }
    }

    @Test
    void shouldNotFailOnListingDevices() throws QingpingServiceInteractionException {
        final HttpClient httpClient = httpClientFactory.getCommonHttpClient();
        final QingpingHttpClient qingpingHttpClient = new QingpingHttpClient(OBJECT_MAPPER, httpClient,
                oAuthClientService);
        qingpingHttpClient.listDevices();
    }
}
