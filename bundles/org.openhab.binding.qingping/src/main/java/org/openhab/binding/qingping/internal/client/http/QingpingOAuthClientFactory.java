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

import org.eclipse.jdt.annotation.Nullable;
import org.openhab.core.auth.client.oauth2.OAuthClientService;
import org.openhab.core.auth.client.oauth2.OAuthFactory;

public class QingpingOAuthClientFactory {
    private static final String ACCESS_TOKEN_URL = "https://oauth.cleargrass.com/oauth2/token";
    private static final String OAUTH_SCOPE = "device_full_access";
    private final OAuthFactory oAuthFactory;

    public QingpingOAuthClientFactory(OAuthFactory oAuthFactory) {
        this.oAuthFactory = oAuthFactory;
    }

    public QingpingOAuthClientService createInstance(String clientID, @Nullable String clientSecret) {
        final OAuthClientService oAuthClientService = oAuthFactory.createOAuthClientService(this.getClass().getName(),
                ACCESS_TOKEN_URL, null, clientID, clientSecret, OAUTH_SCOPE, true);
        return new QingpingOAuthClientService(oAuthClientService);
    }
}
