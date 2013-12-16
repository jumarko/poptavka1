/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import java.util.List;

/**
 * @author Martin Slavkovsky
 */
public interface ServiceSelectorGateway {

    @Event(forwardToParent = true)
    void initServicesWidget(ServiceType serviceType, SimplePanel embedToWidget);

    @Event(forwardToParent = true)
    void fillServices(List<ServiceDetail> services);

    @Event(forwardToParent = true)
    void selectService(ServiceDetail service);
}
