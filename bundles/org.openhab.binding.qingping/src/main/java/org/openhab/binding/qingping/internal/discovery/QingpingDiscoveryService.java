package org.openhab.binding.qingping.internal.discovery;

import java.util.Set;

import org.openhab.binding.qingping.internal.QingpingBindingConstants;
import org.openhab.core.config.discovery.AbstractDiscoveryService;
import org.openhab.core.config.discovery.DiscoveryService;
import org.osgi.service.component.annotations.Component;

@Component(service = DiscoveryService.class, immediate = true, configurationPid = "discovery.qingping")
public class QingpingDiscoveryService extends AbstractDiscoveryService {

    public QingpingDiscoveryService() throws IllegalArgumentException {
        super(Set.of(QingpingBindingConstants.THING_TYPE_AIR_MONITOR), 15, true);
    }

    @Override
    protected void startScan() {
    }
}
