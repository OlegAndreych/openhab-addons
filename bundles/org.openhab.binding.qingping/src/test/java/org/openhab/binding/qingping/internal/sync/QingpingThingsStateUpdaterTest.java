package org.openhab.binding.qingping.internal.sync;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.openhab.binding.qingping.internal.client.http.QingpingClient;
import org.openhab.binding.qingping.internal.client.http.QingpingServiceInteractionException;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.Device;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.DeviceListResponse;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.info.DeviceInfo;

import io.netty.util.concurrent.ScheduledFuture;

@ExtendWith(MockitoExtension.class)
@DisplayName("QingpingThingsStateUpdater")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class QingpingThingsStateUpdaterTest {
    @Mock
    private QingpingClient qingpingClient;
    @Mock
    private ScheduledExecutorService scheduler;
    @Captor
    private ArgumentCaptor<Runnable> runnableArgumentCaptor;

    @Test
    @SuppressWarnings("unchecked")
    void should_register_state_handler_for_things_and_deregister_it() throws QingpingServiceInteractionException {
        // Creating test data
        final ScheduledFuture scheduledFuture = mock(ScheduledFuture.class);
        when(scheduler.scheduleAtFixedRate(any(), anyLong(), anyLong(), any())).thenReturn(scheduledFuture);

        final String deviceMac1 = "DM1";
        final String deviceMac2 = "DM2";
        @SuppressWarnings("DataFlowIssue")
        final Device device1 = new Device(new DeviceInfo(deviceMac1, null, null, null, 0, 0, null, null, null), null);
        @SuppressWarnings("DataFlowIssue")
        final Device device2 = new Device(new DeviceInfo(deviceMac2, null, null, null, 0, 0, null, null, null), null);
        final List<Device> devices = List.of(device1, device2);
        when(qingpingClient.listDevices()).thenReturn(new DeviceListResponse(2, devices));

        // Creating object under test
        final QingpingThingsStateUpdater qingpingThingsStateUpdater = new QingpingThingsStateUpdater(qingpingClient,
                scheduler);

        // Checking state refresh action scheduling
        final Consumer<Device> consumer = mock(Consumer.class);
        final Runnable registration1 = qingpingThingsStateUpdater
                .register(new SynchronizationRegistrationData(deviceMac1, consumer));
        verify(scheduler).scheduleAtFixedRate(runnableArgumentCaptor.capture(), eq(0L), eq(30L), eq(TimeUnit.SECONDS));
        final Runnable registration2 = qingpingThingsStateUpdater
                .register(new SynchronizationRegistrationData(deviceMac2, consumer));
        verifyNoMoreInteractions(scheduler);

        // Checking consumer interaction during scheduled action execution
        final Runnable scheduledAction = runnableArgumentCaptor.getValue();
        scheduledAction.run();
        verify(consumer).accept(device1);
        verify(consumer).accept(device2);

        registration1.run();
        registration2.run();

        verify(scheduledFuture).cancel(false);
    }
}