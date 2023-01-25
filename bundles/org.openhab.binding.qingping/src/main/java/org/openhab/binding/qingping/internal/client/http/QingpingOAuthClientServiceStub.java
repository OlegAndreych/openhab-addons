package org.openhab.binding.qingping.internal.client.http;

public class QingpingOAuthClientServiceStub implements QingpingOAuthClientService {

    private final String stubbingReason;

    public QingpingOAuthClientServiceStub(String stubbingReason) {
        this.stubbingReason = stubbingReason;
    }

    @Override
    public String getOAuthAccessToken() throws QingpingServiceInteractionException {
        throw new QingpingServiceInteractionException(stubbingReason);
    }

    @Override
    public void close() {
    }
}
