package org.openhab.binding.qingping.internal.client.http;

import java.io.IOException;

import org.openhab.core.auth.client.oauth2.AccessTokenResponse;
import org.openhab.core.auth.client.oauth2.OAuthClientService;
import org.openhab.core.auth.client.oauth2.OAuthException;
import org.openhab.core.auth.client.oauth2.OAuthResponseException;
import org.openhab.core.thing.ThingStatus;
import org.openhab.core.thing.ThingStatusDetail;

public class QingpingOAuthClientServiceImpl implements QingpingOAuthClientService {

    private final OAuthClientService oAuthClientService;

    public QingpingOAuthClientServiceImpl(OAuthClientService oAuthClientService) {
        this.oAuthClientService = oAuthClientService;
    }

    @Override
    public String getOAuthAccessToken() throws QingpingServiceInteractionException {
        try {
            final AccessTokenResponse oAuthTokenResponse = oAuthClientService.getAccessTokenByClientCredentials(null);
            return oAuthTokenResponse.getAccessToken();
        } catch (OAuthException | IOException | OAuthResponseException e) {
            throw new QingpingServiceInteractionException("Error while getting an oAuth token for the Qingping API.",
                    ThingStatus.ONLINE, ThingStatusDetail.COMMUNICATION_ERROR, e);
        }
    }

    @Override
    public void close() {
        oAuthClientService.close();
    }
}
