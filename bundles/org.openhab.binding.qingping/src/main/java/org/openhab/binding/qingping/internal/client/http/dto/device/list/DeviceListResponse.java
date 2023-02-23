package org.openhab.binding.qingping.internal.client.http.dto.device.list;

import java.util.List;

public record DeviceListResponse(int total, List<Device> devices) {
}
