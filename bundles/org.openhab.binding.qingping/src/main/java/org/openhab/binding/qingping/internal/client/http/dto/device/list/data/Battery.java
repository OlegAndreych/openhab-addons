package org.openhab.binding.qingping.internal.client.http.dto.device.list.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Battery {
    private final byte value;

    @JsonCreator
    public Battery(@JsonProperty("value") byte value) {
        this.value = value;
    }

    public byte getValue() {
        return value;
    }
}
