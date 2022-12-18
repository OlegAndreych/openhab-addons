package org.openhab.binding.internal.client.http;

import static org.junit.jupiter.api.Assumptions.assumeFalse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.eclipse.jetty.client.HttpClient;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openhab.binding.qingping.internal.client.http.QingpingHttpClient;
import org.openhab.binding.qingping.internal.client.http.QingpingOAuthClientFactory;
import org.openhab.binding.qingping.internal.client.http.QingpingOAuthClientService;
import org.openhab.binding.qingping.internal.client.http.QingpingServiceInteractionException;
import org.openhab.core.auth.client.oauth2.OAuthFactory;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.openhab.core.storage.StorageService;
import org.openhab.core.test.java.JavaOSGiTest;
import org.openhab.core.test.storage.VolatileStorageService;

import com.fasterxml.jackson.databind.ObjectMapper;

public class QingpingHTTPInteractionsTest extends JavaOSGiTest {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private final StorageService storageService = new VolatileStorageService();
    private HttpClientFactory httpClientFactory;
    private QingpingOAuthClientService oAuthClientService;

    @BeforeEach
    void setUp() throws IOException {
        registerService(storageService);
        final OAuthFactory oAuthFactory = getService(OAuthFactory.class);
        httpClientFactory = getService(HttpClientFactory.class);
        QingpingOAuthClientFactory qingpingOAuthClientFactory = new QingpingOAuthClientFactory(oAuthFactory);

        final Properties properties = loadTestProperties();
        oAuthClientService = qingpingOAuthClientFactory.createInstance(properties.getProperty("app.key"),
                properties.getProperty("app.secret"));
    }

    @AfterEach
    void tearDown() {
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
