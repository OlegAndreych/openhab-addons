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

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.openhab.core.thing.ThingTypeUID;

/**
 * The {@link QingpingBindingConstants} class defines common constants, which are
 * used across the whole binding.
 *
 * @author Oleg Andreych - Initial contribution
 */
@NonNullByDefault
public class QingpingBindingConstants {
    private static final String BINDING_ID = "qingping";

    // List of all Thing Type UIDs
    public static final ThingTypeUID THING_TYPE_AIR_MONITOR = new ThingTypeUID(BINDING_ID, "air-monitor");

    public static final String MAC_PROPERTY_NAME = "mac";

    // List of all Channel ids
    public static final String BATTERY_CHANNEL = "battery";
    public static final String TEMPERATURE_CHANNEL = "temperature";
    public static final String HUMIDITY_CHANNEL = "humidity";
    public static final String TVOC_CHANNEL = "tvoc";
    public static final String CO2_CHANNEL = "co2";
    public static final String PM25_CHANNEL = "pm25";
    public static final String PM10_CHANNEL = "pm10";

    // Binding parameters
    public static final String APP_KEY_PARAMETER = "App Key";
    public static final String APP_SECRET_PARAMETER = "App Secret";
}
