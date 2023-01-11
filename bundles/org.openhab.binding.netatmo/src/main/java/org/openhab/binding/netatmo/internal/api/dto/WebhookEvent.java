/**
 * Copyright (c) 2010-2023 Contributors to the openHAB project
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
package org.openhab.binding.netatmo.internal.api.dto;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.netatmo.internal.api.data.EventType;
import org.openhab.binding.netatmo.internal.deserialization.NAObjectMap;
import org.openhab.binding.netatmo.internal.deserialization.NAPushType;

/**
 * The {@link WebhookEvent} is responsible to hold
 * data given back by the Netatmo API when calling the webhook
 *
 * @author Gaël L'hopital - Initial contribution
 *
 */
@NonNullByDefault
public class WebhookEvent extends Event {
    private NAPushType pushType = NAPushType.UNKNOWN;
    private String homeId = "";
    private String deviceId = "";
    private @Nullable String snapshotUrl;
    private @Nullable String vignetteUrl;
    private NAObjectMap<Person> persons = new NAObjectMap<>();
    // Webhook does not provide the event generation time, so we'll use the event reception time
    private ZonedDateTime time = ZonedDateTime.now();

    public String getHomeId() {
        return homeId;
    }

    public NAObjectMap<Person> getPersons() {
        return persons;
    }

    @Override
    public EventType getEventType() {
        return pushType.getEvent();
    }

    @Override
    public ZonedDateTime getTime() {
        return time;
    }

    @Override
    public @Nullable String getPersonId() {
        return persons.size() > 0 ? persons.keySet().iterator().next() : null;
    }

    @Override
    public @Nullable String getSnapshotUrl() {
        return snapshotUrl;
    }

    public @Nullable String getVignetteUrl() {
        return vignetteUrl;
    }

    public List<String> getNAObjectList() {
        List<String> result = new ArrayList<>();
        result.add(getCameraId());
        addNotBlank(result, homeId);
        addNotBlank(result, deviceId);
        addNotBlank(result, getCameraId());
        result.addAll(getPersons().keySet());
        return result;
    }

    private void addNotBlank(List<String> list, String value) {
        if (!value.isBlank()) {
            list.add(value);
        }
    }
}
