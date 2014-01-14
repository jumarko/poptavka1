/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail.interfaces;

import com.eprovement.poptavka.client.detail.views.DemandDetailView;
import com.eprovement.poptavka.client.detail.views.OfferQuestionWindow;
import com.eprovement.poptavka.client.detail.views.RatingDetailView;
import com.eprovement.poptavka.client.detail.views.UserDetailView;
import com.eprovement.poptavka.client.root.interfaces.HandleResizeEvent;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.mvp4g.client.view.LazyView;

/**
 * Interface definitions for DetailModule.
 * @author Martin Slavkovsky
 *         Date: 14.01.2014
 */
public interface IDetailModule {

    public interface Presenter extends HandleResizeEvent {
    }

    public interface View extends LazyView {

        Widget getWidgetView();

        TabLayoutPanel getContainer();

        SimplePanel getDemandDetailHolder();

        SimplePanel getAdvertisementHolder();

        DemandDetailView getDemandDetail();

        UserDetailView getSupplierDetail();

        RatingDetailView getRatingDetail();

        OfferQuestionWindow getReplyHolder();

        CellList getMessageList();

        ListDataProvider getMessageProvider();

        FluidContainer getConversationHolder();

        void loadingDivShow(Widget holderWidget);

        void loadingDivHide(Widget holderWidget);

        void setMessagePanelVisibility();
    }
}
