/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.interfaces;

import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.view.LazyView;
import java.util.Set;

/**
 * @author Martin Slavkovsky
 *         Date: 14.01.2014
 */
public interface IAbstractAdmin {

    public interface Presenter extends HandleAdminResizeEvent {
    }

    public interface View extends LazyView, IsWidget {

        void initTable(UniversalAsyncGrid table);

        UniversalAsyncGrid getTable();

        Set getSelectedObjects();

        SimplePanel getFooterContainer();

        SimplePanel getDetailPanel();

        AdminToolbarView getToolbar();

        Widget getWidgeView();
    }
}
