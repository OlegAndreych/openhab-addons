package org.openhab.binding.qingping.internal.client.http.dto.device.list.data;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Temperature {
    private final BigDecimal value;

    @JsonCreator
    public Temperature(@JsonProperty("value") BigDecimal value) {
        this.value = value;
    }

    public BigDecimal getValue() {
        return value;
    }
}
