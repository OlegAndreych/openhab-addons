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
package org.openhab.binding.qingping.internal.client.http;

import static org.openhab.binding.qingping.internal.QingpingBindingConstants.APP_KEY_PARAMETER;
import static org.openhab.binding.qingping.internal.QingpingBindingConstants.APP_SECRET_PARAMETER;

import java.io.IOException;
import java.util.Dictionary;
import java.util.Objects;

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.auth.client.oauth2.OAuthClientService;
import org.openhab.core.auth.client.oauth2.OAuthFactory;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component(service = QingpingOAuthClientProvider.class)
public class QingpingOAuthClientProvider {
    private final Logger logger = LoggerFactory.getLogger(QingpingOAuthClientProvider.class);

    private static final String ACCESS_TOKEN_URL = "https://oauth.cleargrass.com/oauth2/token";
    private static final String OAUTH_SCOPE = "device_full_access";
    private final OAuthFactory oAuthFactory;
    @Nullable
    private final String appKey;
    @Nullable
    private final String appSecret;
    private final QingpingOAuthClientService qingpingOAuthClientService;

    @Activate
    public QingpingOAuthClientProvider(@Reference OAuthFactory oAuthFactory,
            @Reference ConfigurationAdmin configurationAdmin) throws IOException {
        this.oAuthFactory = oAuthFactory;
        final Configuration configuration = configurationAdmin.getConfiguration("binding.qingping");
        final Dictionary<String, Object> properties = configuration.getProperties();

        this.appKey = (String) properties.get(APP_KEY_PARAMETER);
        this.appSecret = (String) properties.get(APP_SECRET_PARAMETER);

        this.qingpingOAuthClientService = createInstance();
    }

    private QingpingOAuthClientService createInstance() {
        if (appKey == null || appSecret == null) {
            return new QingpingOAuthClientServiceStub("OAuth credentials are not provided.", ThingStatus.OFFLINE,
                    ThingStatusDetail.CONFIGURATION_ERROR);
        }

        final OAuthClientService oAuthClientService;
        oAuthClientService = oAuthFactory.createOAuthClientService(this.getClass().getName(), ACCESS_TOKEN_URL, null,
                Objects.requireNonNull(appKey), appSecret, OAUTH_SCOPE, true);
        return new QingpingOAuthClientServiceImpl(oAuthClientService);
    }

    public QingpingOAuthClientService getInstance() {
        return qingpingOAuthClientService;
    }

    @Deactivate
    void deactivate() {
        try {
            this.qingpingOAuthClientService.close();
        } catch (Exception e) {
            logger.debug("Cannot close QingpingOAuthClientService due to error: {}.", e.getMessage());
        }
    }
}
