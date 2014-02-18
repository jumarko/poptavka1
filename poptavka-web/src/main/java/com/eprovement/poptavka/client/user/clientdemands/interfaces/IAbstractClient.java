/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.interfaces;

import com.eprovement.poptavka.client.user.clientdemands.toolbar.ClientToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.view.LazyView;
import java.util.Set;

/**
 * @author Martin Slavkovsky
 *         Date: 14.01.2014
 */
public interface IAbstractClient {

    public interface Presenter extends HandleClientResizeEvent {
    }

    public interface View extends LazyView, IsWidget {

        void initTables(UniversalAsyncGrid parentTable, UniversalAsyncGrid childTable);

        UniversalAsyncGrid getParentTable();

        UniversalAsyncGrid getChildTable();

        Set getChildTableSelectedObjects();

        SimplePanel getFooterContainer();

        SimplePanel getDetailPanel();

        ClientToolbarView getToolbar();

        IsWidget getWidgetView();

        // Setters
        void setParentTableVisible(boolean visible);

        void setChildTableVisible(boolean visible);

        void setDemandTitleLabel(String text);
    }
}
