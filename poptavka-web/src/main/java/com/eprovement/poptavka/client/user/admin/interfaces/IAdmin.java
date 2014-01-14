/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.interfaces;

import com.eprovement.poptavka.client.root.interfaces.HandleResizeEvent;
import com.eprovement.poptavka.client.root.toolbar.ProvidesToolbar;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.view.LazyView;

/**
 * @author Martin Slavkovsky
 *         Date: 14.01.2014
 */
public interface IAdmin {

    public interface Presenter extends HandleResizeEvent, NavigationConfirmationInterface {
    }

    public interface View extends LazyView, IsWidget, ProvidesToolbar {

        void setContent(Widget contentWidget);

        Button getDemandsButton();

        Button getActiveDemandsBtn();

        Button getClientsButton();

        Button getOffersButton();

        Button getSuppliersButton();

        Button getAccessRoleButton();

        Button getEmailActivationButton();

        Button getInvoiceButton();

        Button getMessageButton();

        Button getNewDemandsBtn();

        Button getPaymentMethodButton();

        Button getPermissionButton();

        Button getPreferenceButton();

        Button getProblemButton();

        SimplePanel getContentContainer();

        Widget getWidgetView();
    }
}
