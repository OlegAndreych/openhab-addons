package org.openhab.binding.qingping.internal.client.http;

public interface QingpingOAuthClientService extends AutoCloseable {
    String getOAuthAccessToken() throws QingpingServiceInteractionException;
}
