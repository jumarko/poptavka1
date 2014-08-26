/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.home.createSupplier.interfaces;

import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
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
public interface ISupplierCreationModule {

    String NAME = "supplierCreation";
    String GA_EVENT_USER = "supplierCreation_User";
    String GA_EVENT_CATEGORY = "supplierCreation_Category";
    String GA_EVENT_LOCALITY = "supplierCreation_Locality";
    String GA_EVENT_SERVICES = "supplierCreation_Services";
    String GA_EVENT_NEW_SUPPLIER = "supplierCreation_NewSupplier";

    public interface Gateway {

        /**
         * @see Presenter#goToCreateSupplierModule()
         */
        @Event(forwardToParent = true)
        void goToCreateSupplierModule();
    }

    public interface Handler {

        /**
         * Method registers new supplier.
         * @param newSupplier newly created supplier
         */
        void onRequestRegisterSupplier(final FullSupplierDetail newSupplier);
    }

    public interface Presenter extends NavigationConfirmationInterface {

        /**
         * Initialize SupplierCreation module.
         */
        void onGoToCreateSupplierModule();

        void onResponseRegisterSupplier(FullSupplierDetail newSupplier);
    }

    public interface View extends LazyView, IsWidget, ProvidesValidate {

        /** Panels. **/
        TabLayoutPanel getMainPanel();

        SimplePanel getHolderPanel(int order);

        SimplePanel getFooterPanel();

        /** Buttons. **/
        Button getRegisterButton();

        Anchor getTermsAndConditionsButton();

        /** Other. **/
        Tooltip getNextBtnTooltip(int order);

        CheckBox getAgreedCheck();
    }

}
