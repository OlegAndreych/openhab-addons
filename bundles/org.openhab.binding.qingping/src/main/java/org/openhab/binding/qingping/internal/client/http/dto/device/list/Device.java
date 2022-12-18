package org.openhab.binding.qingping.internal.client.http.dto.device.list;

import org.openhab.binding.qingping.internal.client.http.dto.device.list.data.DeviceData;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.info.DeviceInfo;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Device {
    private final DeviceInfo info;
    private final DeviceData data;

    @JsonCreator
    public Device(@JsonProperty("info") DeviceInfo info, @JsonProperty("data") DeviceData data) {
        this.info = info;
        this.data = data;
    }

    public DeviceInfo getInfo() {
        return info;
    }

    public DeviceData getData() {
        return data;
    }
}
