package org.openhab.binding.qingping.internal.client.http.dto.device.list.info;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class DeviceSetting {
    private final int reportInterval;
    private final int collectInterval;

    @JsonCreator
    public DeviceSetting(@JsonProperty("report_interval") int reportInterval,
            @JsonProperty("collect_interval") int collectInterval) {
        this.reportInterval = reportInterval;
        this.collectInterval = collectInterval;
    }

    public int getReportInterval() {
        return reportInterval;
    }

    public int getCollectInterval() {
        return collectInterval;
    }
}
