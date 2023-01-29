package org.openhab.binding.qingping.internal.client.http;

import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;

public class QingpingServiceInteractionException extends Exception {

    private final ThingStatus thingStatus;
    private final ThingStatusDetail thingStatusDetail;

    public QingpingServiceInteractionException(String message, ThingStatus thingStatus,
            ThingStatusDetail thingStatusDetail) {
        super(message);
        this.thingStatus = thingStatus;
        this.thingStatusDetail = thingStatusDetail;
    }

    public QingpingServiceInteractionException(String message, ThingStatus thingStatus,
            ThingStatusDetail thingStatusDetail, Throwable cause) {
        super(message, cause);
        this.thingStatus = thingStatus;
        this.thingStatusDetail = thingStatusDetail;
    }

    public ThingStatus getThingStatus() {
        return thingStatus;
    }

    public ThingStatusDetail getThingStatusDetail() {
        return thingStatusDetail;
    }
}
