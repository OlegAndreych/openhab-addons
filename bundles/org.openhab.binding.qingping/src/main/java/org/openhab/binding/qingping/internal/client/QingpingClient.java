package org.openhab.binding.qingping.internal.client;

import org.eclipse.jetty.client.HttpClient;
import org.openhab.core.auth.client.oauth2.OAuthFactory;

/**
 * Client to communicate with the Qingping cloud API.
 */
public class QingpingClient {
    private final HttpClient httpClient;
    private final OAuthFactory oAuthFactory;

    public QingpingClient(HttpClient httpClient, OAuthFactory oAuthFactory) {
        this.httpClient = httpClient;
        this.oAuthFactory = oAuthFactory;
    }

    public void listDevices() {
        // oAuthFactory.createOAuthClientService()
    }
}
