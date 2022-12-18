package org.openhab.binding.qingping.internal.client.http.dto.device.list.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TVOC {
    private final int value;

    @JsonCreator
    public TVOC(@JsonProperty("value") int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
