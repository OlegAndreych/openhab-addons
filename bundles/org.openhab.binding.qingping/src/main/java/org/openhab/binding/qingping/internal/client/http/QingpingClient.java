package org.openhab.binding.qingping.internal.client.http;

import static org.eclipse.jetty.http.HttpHeader.AUTHORIZATION;
import static org.eclipse.jetty.http.HttpMethod.GET;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.DeviceListResponse;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Client to communicate with the Qingping cloud API.
 */
public class QingpingClient {
    private static final String BASIC_URL = "https://apis.cleargrass.com";
    private static final String LIST_DEVICES_URL = BASIC_URL + "/v1/apis/devices";

    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;
    private final QingpingOAuthClientService oAuthClientService;

    public QingpingClient(ObjectMapper objectMapper, HttpClient httpClient,
            QingpingOAuthClientService oAuthClientService) {
        this.objectMapper = objectMapper;
        this.httpClient = httpClient;
        this.oAuthClientService = oAuthClientService;
    }

    public DeviceListResponse listDevices() throws QingpingServiceInteractionException {
        final String accessToken = oAuthClientService.getOAuthAccessToken();

        final ContentResponse response;
        try {
            response = httpClient.newRequest(LIST_DEVICES_URL).header(AUTHORIZATION, "Bearer " + accessToken)
                    .method(GET).send();
        } catch (InterruptedException | TimeoutException | ExecutionException e) {
            throw new QingpingServiceInteractionException(
                    "There's an error while getting devices list from the Qingping API.", ThingStatus.ONLINE,
                    ThingStatusDetail.COMMUNICATION_ERROR, e);
        }

        try {
            return objectMapper.readValue(response.getContent(), DeviceListResponse.class);
        } catch (IOException e) {
            throw new QingpingServiceInteractionException("Cannot parse response from Qingping API service.",
                    ThingStatus.ONLINE, ThingStatusDetail.COMMUNICATION_ERROR, e);
        }
    }
}
