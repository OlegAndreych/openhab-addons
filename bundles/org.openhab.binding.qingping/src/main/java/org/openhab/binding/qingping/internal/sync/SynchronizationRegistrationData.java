package org.openhab.binding.qingping.internal.sync;

import java.util.function.Consumer;

import org.openhab.binding.qingping.internal.client.http.dto.device.list.Device;

public record SynchronizationRegistrationData(String deviceMac, Consumer<Device> syncedDataConsumer) {
}
