package org.openhab.binding.qingping.internal.client.http;

import static org.eclipse.jetty.http.HttpHeader.AUTHORIZATION;
import static org.eclipse.jetty.http.HttpMethod.GET;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;

/**
 * Client to communicate with the Qingping cloud API.
 */
public class QingpingHttpClient {
    private static final String BASIC_URL = "https://apis.cleargrass.com";
    private static final String LIST_DEVICES_URL = BASIC_URL + "/v1/apis/devices";

    private final HttpClient httpClient;
    private final QingpingOAuthClientService oAuthClientService;

    public QingpingHttpClient(HttpClient httpClient, QingpingOAuthClientService oAuthClientService) {
        this.httpClient = httpClient;
        this.oAuthClientService = oAuthClientService;
    }

    public void listDevices() throws QingpingServiceInteractionException {
        final String accessToken = oAuthClientService.getOAuthAccessToken();

        final ContentResponse response;
        try {
            response = httpClient.newRequest(LIST_DEVICES_URL).header(AUTHORIZATION, "Bearer " + accessToken)
                    .method(GET).send();
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new QingpingServiceInteractionException(
                    "There's an error while getting devices list from the Qingping API.", e);
        }
    }
}
