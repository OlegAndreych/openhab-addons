package org.openhab.binding.qingping.internal.client.http.dto.device.list.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceStatus {
    private final boolean offline;

    @JsonCreator
    public DeviceStatus(@JsonProperty("offline") boolean offline) {
        this.offline = offline;
    }

    public boolean isOffline() {
        return offline;
    }
}
