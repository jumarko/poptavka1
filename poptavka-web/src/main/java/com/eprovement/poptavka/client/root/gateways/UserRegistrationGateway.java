/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;

/**
 * Gateway interface for Category selector module.
 * Defines which methods are accessible to the world.
 *
 * @author Martin Slavkovsky
 */
public interface UserRegistrationGateway {

    @Event(forwardToParent = true)
    void initUserRegistration(SimplePanel holderWidget);

    @Event(forwardToParent = true)
    void fillBusinessUserDetail(BusinessUserDetail userDetail);
}
