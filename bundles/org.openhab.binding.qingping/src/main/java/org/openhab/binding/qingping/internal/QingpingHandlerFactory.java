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
package org.openhab.binding.qingping.internal;

import static org.openhab.binding.qingping.internal.QingpingBindingConstants.THING_TYPE_AIR_MONITOR;

import java.util.Set;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.openhab.binding.qingping.internal.client.http.QingpingHttpClient;
import org.openhab.binding.qingping.internal.client.http.QingpingOAuthClientProvider;
import org.openhab.binding.qingping.internal.client.http.QingpingOAuthClientService;
import org.openhab.core.io.net.http.HttpClientFactory;
import org.openhab.core.thing.Thing;
import org.openhab.core.thing.ThingTypeUID;
import org.openhab.core.thing.binding.BaseThingHandlerFactory;
import org.openhab.core.thing.binding.ThingHandler;
import org.openhab.core.thing.binding.ThingHandlerFactory;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * The {@link QingpingHandlerFactory} is responsible for creating things and thing
 * handlers.
 *
 * @author Oleg Andreych - Initial contribution
 */
@NonNullByDefault
@Component(configurationPid = "binding.qingping", service = ThingHandlerFactory.class)
public class QingpingHandlerFactory extends BaseThingHandlerFactory {

    private final Logger logger = LoggerFactory.getLogger(QingpingHandlerFactory.class);
    private static final Set<ThingTypeUID> SUPPORTED_THING_TYPES_UIDS = Set.of(THING_TYPE_AIR_MONITOR);
    private final QingpingHttpClient qingpingHttpClient;

    @Activate
    public QingpingHandlerFactory(@Reference HttpClientFactory httpClientFactory,
            @Reference QingpingOAuthClientProvider oauthClientFactory) {

        final QingpingOAuthClientService qingpingOAuthClientService = oauthClientFactory.getInstance();
        final ObjectMapper mapper = new ObjectMapper();
        this.qingpingHttpClient = new QingpingHttpClient(mapper, httpClientFactory.getCommonHttpClient(),
                qingpingOAuthClientService);
    }

    @Override
    public boolean supportsThingType(ThingTypeUID thingTypeUID) {
        return SUPPORTED_THING_TYPES_UIDS.contains(thingTypeUID);
    }

    @Override
    protected @Nullable ThingHandler createHandler(Thing thing) {
        ThingTypeUID thingTypeUID = thing.getThingTypeUID();

        if (THING_TYPE_AIR_MONITOR.equals(thingTypeUID)) {
            return new AirMonitorHandler(thing);
        }

        return null;
    }
}
