package org.openhab.binding.qingping.internal.client.http;

import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;

public class QingpingOAuthClientServiceStub implements QingpingOAuthClientService {
    private final String stubbingReason;
    private final ThingStatus thingStatus;
    private final ThingStatusDetail thingStatusDetail;

    public QingpingOAuthClientServiceStub(String stubbingReason, ThingStatus thingStatus,
            ThingStatusDetail thingStatusDetail) {
        this.stubbingReason = stubbingReason;
        this.thingStatus = thingStatus;
        this.thingStatusDetail = thingStatusDetail;
    }

    @Override
    public String getOAuthAccessToken() throws QingpingServiceInteractionException {
        throw new QingpingServiceInteractionException(stubbingReason, thingStatus, thingStatusDetail);
    }

    @Override
    public void close() {
    }
}
