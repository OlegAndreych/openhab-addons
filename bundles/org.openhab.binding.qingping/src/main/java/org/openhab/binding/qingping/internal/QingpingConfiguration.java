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

/**
 * The {@link QingpingConfiguration} class contains fields mapping thing configuration parameters.
 *
 * @author Oleg Andreych - Initial contribution
 */
@NonNullByDefault
public class QingpingConfiguration {
    /**
     * PIN to bind the device to Qingping account. Is not used after binding.
     */
    public String pin = "";
    /**
     * Report interval (second), minimum is 10 second, an integer multiple of collect_interval.
     */
    public int reportInterval = 30;
    /**
     * Data acquisition and report interval (second).
     */
    public int collectInterval = 10;
}
