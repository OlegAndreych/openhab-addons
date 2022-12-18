package org.openhab.binding.qingping.internal.client.http.dto.device.list;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceListResponse {
    private final int total;
    private final List<Device> devices;

    @JsonCreator
    public DeviceListResponse(@JsonProperty("total") int total, @JsonProperty("devices") List<Device> devices) {
        this.total = total;
        this.devices = devices;
    }

    public int getTotal() {
        return total;
    }

    public List<Device> getDevices() {
        return devices;
    }
}
