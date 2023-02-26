/**
 * Copyright (c) 2010-2022 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.qingping.internal;

import static java.util.Objects.requireNonNull;
import static org.openhab.binding.qingping.internal.QingpingBindingConstants.*;

import java.util.Map;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.qingping.internal.client.http.QingpingClient;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.Device;
import org.openhab.binding.qingping.internal.client.http.dto.device.list.data.DeviceData;
import org.openhab.binding.qingping.internal.sync.QingpingThingsStateUpdater;
import org.openhab.binding.qingping.internal.sync.SynchronizationRegistrationData;
import org.openhab.core.library.types.DecimalType;
import org.openhab.core.library.types.PercentType;
import org.openhab.core.thing.ChannelUID;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.binding.BaseThingHandler;
import org.openhab.core.types.Command;
import org.openhab.core.types.RefreshType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@NonNullByDefault
public class AirMonitorHandler extends BaseThingHandler {

    private final Logger logger = LoggerFactory.getLogger(AirMonitorHandler.class);

    private @Nullable QingpingConfiguration config;
    private final QingpingClient qingpingClient;
    private final QingpingThingsStateUpdater qingpingThingsStateUpdater;
    private @Nullable Runnable registrationHandle;

    public AirMonitorHandler(Thing thing, QingpingClient qingpingClient,
            QingpingThingsStateUpdater qingpingThingsStateUpdater) {
        super(thing);
        this.qingpingClient = qingpingClient;
        this.qingpingThingsStateUpdater = qingpingThingsStateUpdater;
    }

    @Override
    public void handleCommand(ChannelUID channelUID, Command command) {
        if (BATTERY_CHANNEL.equals(channelUID.getId())) {
            if (command instanceof RefreshType) {
                // TODO: handle data refresh
            }

            // TODO: handle command

            // Note: if communication with thing fails for some reason,
            // indicate that by setting the status with detail information:
            // updateStatus(ThingStatus.OFFLINE, ThingStatusDetail.COMMUNICATION_ERROR,
            // "Could not control device at IP address x.x.x.x");
        }
    }

    @Override
    public void initialize() {
        config = getConfigAs(QingpingConfiguration.class);

        // TODO: Initialize the handler.
        // The framework requires you to return from this method quickly, i.e. any network access must be done in
        // the background initialization below.
        // Also, before leaving this method a thing status from one of ONLINE, OFFLINE or UNKNOWN must be set. This
        // might already be the real thing status in case you can decide it directly.
        // In case you can not decide the thing status directly (e.g. for long running connection handshake using WAN
        // access or similar) you should set status UNKNOWN here and then decide the real status asynchronously in the
        // background.

        // set the thing status to UNKNOWN temporarily and let the background task decide for the real status.
        // the framework is then able to reuse the resources from the thing handler initialization.
        // we set this upfront to reliably check status updates in unit tests.
        updateStatus(ThingStatus.UNKNOWN);

        final Map<String, String> properties = thing.getProperties();
        final String mac = properties.get(MAC_PROPERTY_NAME);
        requireNonNull(mac, "MAC property value must be present for an Air Monitor thing.");
        registrationHandle = qingpingThingsStateUpdater
                .register(new SynchronizationRegistrationData(mac, this::applyNewState));
    }

    private void applyNewState(Device device) {
        updateStatus(ThingStatus.ONLINE);
        final DeviceData data = device.getData();
        updateState(BATTERY_CHANNEL, new PercentType(data.getBattery().getValue()));
        updateState(TEMPERATURE_CHANNEL, new DecimalType(data.getTemperature().getValue()));
        updateState(HUMIDITY_CHANNEL, new PercentType(data.getHumidity().getValue()));
        updateState(TVOC_CHANNEL, new PercentType(data.getTvoc().getValue()));
        updateState(CO2_CHANNEL, new DecimalType(data.getCo2().getValue()));
        updateState(PM25_CHANNEL, new DecimalType(data.getPm25().getValue()));
        updateState(PM10_CHANNEL, new DecimalType(data.getPm10().getValue()));
    }

    @Override
    public void dispose() {
        if (registrationHandle != null) {
            registrationHandle.run();
        }
        super.dispose();
    }
}
