/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import java.util.List;

/**
 * Gateway interface for Service selector module.
 * Defines which methods are accessible to the rest of application.
 *
 * @author Martin Slavkovsky
 */
public interface ServiceSelectorGateway {

    @Event(forwardToParent = true)
    void initServicesWidget(SimplePanel embedToWidget);

    @Event(forwardToParent = true)
    void fillServices(List<ServiceDetail> services);

    @Event(forwardToParent = true)
    void selectService(ServiceDetail service);
}
