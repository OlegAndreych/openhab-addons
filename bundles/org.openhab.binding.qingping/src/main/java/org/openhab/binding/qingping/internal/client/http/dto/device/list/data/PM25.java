package org.openhab.binding.qingping.internal.client.http.dto.device.list.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class PM25 {
    private final int value;

    @JsonCreator
    public PM25(@JsonProperty("value") int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
