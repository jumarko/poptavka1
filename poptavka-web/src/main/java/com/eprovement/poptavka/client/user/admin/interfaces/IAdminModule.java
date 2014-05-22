/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.interfaces;

import com.eprovement.poptavka.client.root.interfaces.HandleResizeEvent;
import com.eprovement.poptavka.client.root.toolbar.ProvidesToolbar;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.view.LazyView;

/**
 * @author Martin Slavkovsky
 *         Date: 14.01.2014
 */
public interface IAdminModule {

    public enum AdminWidget {

        DASHBOARD,
        NEW_DEMANDS,
        ASSIGNED_DEMANDS,
        ACTIVE_DEMANDS,
        CLIENTS;
    }

    public interface Gateway {

        @Event(forwardToParent = true)
        void goToAdminModule(SearchModuleDataHolder searchDataHolder, AdminWidget loadWidget);

    }

    public interface Presenter extends HandleResizeEvent, NavigationConfirmationInterface {

        void onGoToAdminModule(SearchModuleDataHolder filter, AdminWidget loadWidget);

        void onSetClientMenuActStyle(AdminWidget widget);
    }

    public interface View extends LazyView, IsWidget, ProvidesToolbar {

        void setClientMenuActStyle(AdminWidget widget);

        void setContent(IsWidget contentWidget);

        Button getNewDemandsBtn();

        Button getAssignedDemandsBtn();

        Button getActiveDemandsBtn();

        Button getClientsBtn();

        SimplePanel getContentContainer();
    }
}
