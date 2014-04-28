/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.detail.interfaces;

import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.detail.views.DemandDetailView;
import com.eprovement.poptavka.client.detail.views.OfferQuestionWindow;
import com.eprovement.poptavka.client.detail.views.RatingDetailView;
import com.eprovement.poptavka.client.detail.views.UserDetailView;
import com.eprovement.poptavka.client.root.interfaces.HandleResizeEvent;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.view.LazyView;

/**
 * Interface definitions for DetailModule.
 * @author Martin Slavkovsky
 *         Date: 14.01.2014
 */
public interface IDetailModule {

    public interface Gateway {

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

        @Event(forwardToParent = true)
        void registerQuestionSubmitHandler(ClickHandler handler);
    }

    public interface Presenter extends HandleResizeEvent {

        void onRegisterQuestionSubmitHandler(ClickHandler handler);
    }

    public interface View extends LazyView {

        Widget getWidgetView();

        TabLayoutPanel getContainer();

        SimplePanel getDemandDetailHolder();

        SimplePanel getUserDetailHolder();

        FluidContainer getConversationHolder();

        SimplePanel getRatingDetailHolder();

        SimplePanel getAdvertisementHolder();

        SimplePanel getConversationDetailHolder();

        DemandDetailView getDemandDetail();

        UserDetailView getUserDetail();

        RatingDetailView getRatingDetail();

        OfferQuestionWindow getReplyHolder();

        CellList getMessageList();

        ListDataProvider getMessageProvider();

        void loadingDivShow(Widget holderWidget);

        void loadingDivHide(Widget holderWidget);

        void setMessagePanelVisibility();

        void setUserHeaderLabelText(String text);
    }
}
