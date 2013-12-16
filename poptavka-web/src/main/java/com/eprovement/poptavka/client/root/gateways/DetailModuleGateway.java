/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.gateways;

import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;

/**
 *
 * @author Martin Slavkovsky
 */
public interface DetailModuleGateway {

    @Event(forwardToParent = true)
    void initDetailSection(UniversalAsyncGrid grid, SimplePanel detailSection);

    @Event(forwardToParent = true)
    void buildDetailSectionTabs(DetailModuleBuilder builder);

    @Event(forwardToParent = true)
    void displayAdvertisement();

    @Event(forwardToParent = true)
    void setCustomWidget(int tabIndex, Widget customWidget);

    @Event(forwardToParent = true)
    void setCustomSelectionHandler(SelectionHandler selectionHandler);

    @Event(forwardToParent = true)
    void allowSendingOffer();
}
