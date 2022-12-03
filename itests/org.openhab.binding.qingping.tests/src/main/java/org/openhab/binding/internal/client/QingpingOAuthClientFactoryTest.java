package org.openhab.binding.internal.client;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openhab.binding.qingping.internal.client.QingpingOAuthClientFactory;
import org.openhab.core.auth.client.oauth2.*;
import org.openhab.core.storage.StorageService;
import org.openhab.core.test.java.JavaOSGiTest;
import org.openhab.core.test.storage.VolatileStorageService;

public class QingpingOAuthClientFactoryTest extends JavaOSGiTest {
    private final StorageService storageService = new VolatileStorageService();
    private QingpingOAuthClientFactory qingpingOAuthClientFactory;

    @BeforeEach
    void setUp() {
        registerService(storageService);
        final OAuthFactory oAuthFactory = getService(OAuthFactory.class);
        qingpingOAuthClientFactory = new QingpingOAuthClientFactory(oAuthFactory);
    }

    @Test
    void shouldGetOAuthToken() throws OAuthResponseException, OAuthException, IOException {
        final Properties properties;
        try (InputStream propertiesStream = this.getClass().getClassLoader()
                .getResourceAsStream("env/qingping-client-test.properties")) {
            properties = new Properties();
            properties.load(propertiesStream);
        }
        // TODO: убрать свои креды в проперти
        try (OAuthClientService oAuthClientService = qingpingOAuthClientFactory
                .createInstance(properties.getProperty("app.key"), properties.getProperty("app.secret"))) {
            final AccessTokenResponse accessTokenResponse = oAuthClientService.getAccessTokenByClientCredentials(null);
            assertNotNull(accessTokenResponse.getAccessToken());
        }
    }
}
