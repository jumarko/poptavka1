/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;

/**
 * @author Martin Slavkovsky
 */
public interface SearchModuleGateway {

    @Event(forwardToParent = true)
    void resetSearchBar(Widget newAttributeSearchWidget);
}
