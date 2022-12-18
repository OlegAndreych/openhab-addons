package org.openhab.binding.qingping.internal.client.http.dto.device.list.data;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Timestamp {
    private final long value;

    @JsonCreator
    public Timestamp(@JsonProperty("value") long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
