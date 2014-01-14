/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.interfaces;

import com.eprovement.poptavka.client.root.interfaces.HandleResizeEvent;
import com.eprovement.poptavka.client.root.toolbar.ProvidesToolbar;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.view.LazyView;

/**
 * @author Martin Slavkovsky
 *         Date: 14.01.2014
 */
public interface IClientDemandsModule {

    public interface Presenter extends HandleResizeEvent, NavigationConfirmationInterface {
    }

    public interface View extends LazyView, IsWidget, ProvidesToolbar {

        Button getClientNewDemandsButton();

        Button getClientOffersButton();

        Button getClientAssignedDemandsButton();

        Button getClientClosedDemandsButton();

        Button getClientRatingsButton();

        SimplePanel getContentContainer();

        void clientMenuStyleChange(int loadedView);

        IsWidget getWidgetView();
    }
}
