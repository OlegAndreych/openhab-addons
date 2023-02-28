package org.openhab.binding.qingping.internal.sync;

import java.util.*;
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
    private final Map<String, Consumer<Device>> singleDeviceSubscriptions = new HashMap<>();
    private final Set<Consumer<Collection<Device>>> allDevicesSubscriptions = new HashSet<>();
    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

    private final ScheduledExecutorService scheduler;
    @Nullable
    private ScheduledFuture<?> scheduledFuture;

    public QingpingThingsStateUpdater(QingpingClient qingpingClient, ScheduledExecutorService scheduler) {
        this.qingpingClient = qingpingClient;
        this.scheduler = scheduler;
    }

    /**
     * Register callback for a device state updates.
     *
     * @param deviceMac MAC of the device for subscription;
     * @param syncedDataConsumer callback for updates processing;
     * @return handle to unsubscribe from updates.
     */
    public Runnable subscribeForSingleDevice(String deviceMac, Consumer<Device> syncedDataConsumer) {
        lock.writeLock().lock();
        try {
            singleDeviceSubscriptions.put(deviceMac, syncedDataConsumer);
            if (scheduledFuture == null) {
                // TODO: make period configurable.
                scheduledFuture = scheduler.scheduleAtFixedRate(this::sync, 0, 30, TimeUnit.SECONDS);
            }
            // TODO: make meaningful interface for a registration handle.
            return () -> unsubscribeSingleDevice(deviceMac);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void unsubscribeSingleDevice(String deviceMac) {
        lock.writeLock().lock();
        try {
            singleDeviceSubscriptions.remove(deviceMac);
            unscheduleSyncIfNecessary();
        } finally {
            lock.writeLock().unlock();
        }
    }

    public Runnable subscribeForAllDevices(Consumer<Collection<Device>> syncedDataConsumer) {
        lock.writeLock().lock();
        try {
            allDevicesSubscriptions.add(syncedDataConsumer);
            if (scheduledFuture == null) {
                // TODO: make period configurable.
                scheduledFuture = scheduler.scheduleAtFixedRate(this::sync, 0, 30, TimeUnit.SECONDS);
            }
            // TODO: make meaningful interface for a registration handle.
            return () -> unsubscribeAllDevices(syncedDataConsumer);
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void unsubscribeAllDevices(Consumer<Collection<Device>> syncedDataConsumer) {
        lock.writeLock().lock();
        try {
            allDevicesSubscriptions.remove(syncedDataConsumer);
            unscheduleSyncIfNecessary();
        } finally {
            lock.writeLock().unlock();
        }
    }

    private void unscheduleSyncIfNecessary() {
        if (singleDeviceSubscriptions.isEmpty() && allDevicesSubscriptions.isEmpty() && scheduledFuture != null) {
            scheduledFuture.cancel(false);
            scheduledFuture = null;
        }
    }

    private void sync() {
        try {
            final DeviceListResponse devicesResponse = qingpingClient.listDevices();
            lock.readLock().lock();
            try {
                List<Device> devicesList = devicesResponse.devices();
                for (Consumer<Collection<Device>> allDevicesSubscription : allDevicesSubscriptions) {
                    allDevicesSubscription.accept(devicesList);
                }
                for (Device device : devicesList) {
                    final String mac = device.getInfo().getMac();
                    final Consumer<Device> deviceConsumer = singleDeviceSubscriptions.get(mac);
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
