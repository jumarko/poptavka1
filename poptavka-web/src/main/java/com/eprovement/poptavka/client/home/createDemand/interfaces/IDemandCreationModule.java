/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createDemand.interfaces;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.history.NavigationConfirmationInterface;
import com.mvp4g.client.view.LazyView;

/**
 * @author Martin Slavkovsky
 * @since 28.5.2014
 */
public interface IDemandCreationModule {

    String NAME = "demandCreation";
    String GA_EVENT_LOAD = "demandCreation_Load";
    String GA_EVENT_LOGIN = "demandCreation_Login";
    String GA_EVENT_SIGNUP = "demandCreation_SignUp";
    String GA_EVENT_NEW_CLIENT = "demandCreation_NewClient";
    String GA_EVENT_PROJECT = "demandCreation_Project";
    String GA_EVENT_CATEGORY = "demandCreation_Category";
    String GA_EVENT_LOCALITY = "demandCreation_Locality";
    String GA_EVENT_PROJECT2 = "demandCreation_Project2";
    String GA_EVENT_NEW_DEMAND = "demandCreation_NewProject";

    public interface Gateway {

        /**
         * @see Presenter#onGoToCreateDemandModule()
         */
        @Event(forwardToParent = true)
        void goToCreateDemandModule();
    }

    public interface Handler {

        /**
         * Method registers new client and afterwards creates new demand.
         * @param newClient newly created client
         */
        void onRegisterNewClient(final BusinessUserDetail newClient);

        /**
         * Creates new demand.
         * @param detail front-end entity of demand
         * @param clientId client id
         */
        void onCreateDemand(FullDemandDetail detail, Long clientId);
    }

    public interface Presenter extends NavigationConfirmationInterface {

        /**
         * Initialize DemandCreation module.
         */
        void onGoToCreateDemandModule();
    }

    public interface View extends LazyView, IsWidget {

        void setFirstTabVisibility(boolean visible);

        void setLoginLayout();

        void setRegisterLayout();

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        SimplePanel getHolderPanel(int order);

        /** Buttons. **/
        //Comment - what's the difference in compile report if using return values: HasClickHandlers vs Button
        Button getLoginBtn();

        Button getRegisterBtn();

        //register client button
        Button getNextButtonTab1();

        //create demand button
        Button getNextButtonTab5();

        //restore first tab defaults
        Button getBackButtonTab1();

        /** Headers. **/
        HTML getHeaderLabelTab1();

        /** Other. **/
        Tooltip getNextBtnTooltip(int order);

        SimplePanel getFooterPanel();
    }

}
