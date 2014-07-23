/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.userRegistration;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

/**
 * Used registration presenter.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = UserRegistrationView.class)
public class UserRegistrationPresenter
        extends LazyPresenter<UserRegistrationPresenter.AccountFormInterface, UserRegistrationEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface AccountFormInterface extends LazyView, ProvidesValidate, IsWidget {
        SimplePanel getAddressHolder();

        ValidationMonitor getEmailBox();

        void initVisualFreeEmailCheck(Boolean isAvailable);

        Button getPersonBtn();

        Button getCompanyBtn();

        void createBusinessUserDetail(BusinessUserDetail user);

        boolean getCompanySelected();

        void setCompanyPanelVisibility(boolean showCompanyPanel);
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        // nothing by default
    }

    public void onForward() {
        // nothing by default
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Init widget and call init address selector widget.
     * @param embedToWidget
     */
    public void onInitUserRegistration(SimplePanel embedToWidget) {
        view.reset();
        embedToWidget.setWidget(view);
        if (view.getAddressHolder().getWidget() == null) {
            eventBus.initAddressSelector(view.getAddressHolder());
        }
    }

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        ((TextBox) view.getEmailBox().getWidget()).addValueChangeHandler(
                new ValueChangeHandler<String>() {
                    @Override
                    public void onValueChange(ValueChangeEvent<String> value) {
                        if (view.getEmailBox().isValid() || view.getEmailBox().isExternalValidation()) {
                            eventBus.checkFreeEmail(value.getValue().trim());
                        }
                    }
                });
        addPersonButtonClickHandler();
        addCompanyButtonClickHandler();
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Shows email check validation.
     * @param isAvailable true if available, false otherwise
     */
    public void onCheckFreeEmailResponse(Boolean isAvailable) {
        view.initVisualFreeEmailCheck(isAvailable);
    }

    /**
     * Fills given business user detail with current widget's detail.
     * @param userDetail to be updated
     */
    public void onFillBusinessUserDetail(BusinessUserDetail userDetail) {
        eventBus.fillAddresses(userDetail.getAddresses());
        view.createBusinessUserDetail(userDetail);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Binds person buttons handler.
     * Hides CompanyInfo form.
     */
    private void addPersonButtonClickHandler() {
        view.getPersonBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.setCompanyPanelVisibility(false);
                eventBus.setUserRegistrationHeight(false);
            }
        });
    }

    /**
     * Binds compoany buttons handler.
     * Shows CompanyInfo form.
     */
    private void addCompanyButtonClickHandler() {
        view.getCompanyBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.setCompanyPanelVisibility(true);
                eventBus.setUserRegistrationHeight(true);
            }
        });
    }

    /**
     * Check if company button is selected.
     */
    public void onCheckCompanySelected() {
        eventBus.setUserRegistrationHeight(view.getCompanySelected());
    }
}
