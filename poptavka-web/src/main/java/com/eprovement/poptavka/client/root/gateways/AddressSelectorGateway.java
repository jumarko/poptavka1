/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.eprovement.poptavka.shared.domain.AddressDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import java.util.List;

/**
 * Gateway interface for Address selector module.
 * Defines which methods are accessible to the world.
 *
 * @author Martin Slavkovsky
 */
public interface AddressSelectorGateway {

    @Event(forwardToParent = true)
    void initAddressSelector(SimplePanel embedToWidget);

    @Event(forwardToParent = true)
    void fillAddresses(List<AddressDetail> addresses);

    @Event(forwardToParent = true)
    void setAddresses(List<AddressDetail> addresses);
}
