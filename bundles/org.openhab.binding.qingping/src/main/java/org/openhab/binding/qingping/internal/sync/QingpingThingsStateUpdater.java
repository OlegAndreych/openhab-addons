package org.openhab.binding.qingping.internal.sync;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Consumer;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.qingping.internal.client.http.QingpingClient;
import org.openhab.binding.qingping.internal.client.http.QingpingServiceInteractionException;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.Device;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.DeviceListResponse;

/**
 * A mean to update things states.
 * <p/>
 * Updating states for all things is made in this sole place because Qingping returns states for all things at once.
 */
public class QingpingThingsStateUpdater {
    private final QingpingClient qingpingClient;
    private final Map<String, Consumer<Device>> registrations = new HashMap<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final ScheduledExecutorService scheduler;
    @Nullable
    private ScheduledFuture<?> scheduledFuture;

    public QingpingThingsStateUpdater(QingpingClient qingpingClient, ScheduledExecutorService scheduler) {
        this.qingpingClient = qingpingClient;
        this.scheduler = scheduler;
    }

    public Runnable register(SynchronizationRegistrationData synchronizationRegistrationData) {
        lock.writeLock().lock();
        try {
            final String deviceMac = synchronizationRegistrationData.deviceMac();
            registrations.put(deviceMac, synchronizationRegistrationData.syncedDataConsumer());
            if (scheduledFuture == null) {
                // TODO: make period configurable.
                scheduledFuture = scheduler.scheduleAtFixedRate(this::sync, 0, 30, TimeUnit.SECONDS);
            }
            // TODO: make meaningful interface for a registration handle.
            return () -> {
                lock.writeLock().lock();
                try {
                    registrations.remove(deviceMac);
                } finally {
                    lock.writeLock().unlock();
                }
            };
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void sync() {
        try {
            final DeviceListResponse devices = qingpingClient.listDevices();
            lock.readLock().lock();
            try {
                for (Device device : devices.devices()) {
                    final String mac = device.getInfo().getMac();
                    final Consumer<Device> deviceConsumer = registrations.get(mac);
                    if (deviceConsumer != null) {
                        deviceConsumer.accept(device);
                    }
                }
            } finally {
                lock.readLock().unlock();
            }
        } catch (QingpingServiceInteractionException e) {
            // TODO: pass errors to things' states
            throw new RuntimeException(e);
        }
    }
}
