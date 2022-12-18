package org.openhab.binding.qingping.internal.client.http.dto.device.list.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceProductInfo {
    private final String id;
    private final String code;
    private final String name;
    private final String enName;
    private final boolean noBleSetting;

    @JsonCreator
    public DeviceProductInfo(@JsonProperty("id") String id, @JsonProperty("code") String code,
            @JsonProperty("name") String name, @JsonProperty("en_name") String enName,
            @JsonProperty("noBleSetting") boolean noBleSetting) {
        this.id = id;
        this.code = code;
        this.name = name;
        this.enName = enName;
        this.noBleSetting = noBleSetting;
    }

    public String getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getEnName() {
        return enName;
    }

    public boolean isNoBleSetting() {
        return noBleSetting;
    }
}
