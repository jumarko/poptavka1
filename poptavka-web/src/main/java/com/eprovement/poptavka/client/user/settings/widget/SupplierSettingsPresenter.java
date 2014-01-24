/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.smallPopups.SimpleConfirmPopup;
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter.SupplierSettingsViewInterface;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;

/**
 * SupplierSettings widget is part of Settings module widgets.
 * Displays supplier's profile data only.
 *
 * TODO LATER ivlcek: = check for number of categories according to payed service
 *      = authentication code when updating email
 *      = what else need to do when updating services? send invoice??.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = SupplierSettingsView.class, multiple = true)
public class SupplierSettingsPresenter extends LazyPresenter<SupplierSettingsViewInterface, SettingsEventBus> {

    /**************************************************************************/
    /* VIEW INTERFACE                                                         */
    /**************************************************************************/
    public interface SupplierSettingsViewInterface extends LazyView {

        /** SETTERS. **/
        void setSupplierSettings(SettingDetail detail);

        /** GETTERS. **/
        //Panels
        SimpleConfirmPopup getSelectorPopup();

        SimplePanel getServicePanel();

        List<ICatLocDetail> getCategories();

        List<ICatLocDetail> getLocalities();

        //Buttons
        Button getEditCatBtn();

        Button getEditLocBtn();

        //Others
        boolean isValid();

        SupplierSettingsView getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //history of changes
    private boolean editingCategories;

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    /**
     * Binds events needed for updating categories and localities - edit, submit buttons hanlders.
     */
    @Override
    public void bindView() {
        view.getEditCatBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                editingCategories = true;
                final CatLocSelectorBuilder builder = new CatLocSelectorBuilder.Builder(Constants.USER_SETTINGS_MODULE)
                        .initCategorySelector()
                        .initSelectorManager()
                        .withCheckboxesOnLeafsOnly()
                        .setSelectionRestriction(Constants.REGISTER_MAX_CATEGORIES)
                        .build();
                eventBus.initCatLocSelector(view.getSelectorPopup().getSelectorPanel(), builder);
                eventBus.setCatLocs(view.getCategories(), builder.getInstanceId());
                view.getSelectorPopup().show();
            }
        });
        view.getEditLocBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                editingCategories = false;
                final CatLocSelectorBuilder builder = new CatLocSelectorBuilder.Builder(Constants.USER_SETTINGS_MODULE)
                        .initLocalitySelector()
                        .initSelectorManager()
                        .withCheckboxesOnLeafsOnly()
                        .setSelectionRestriction(Constants.REGISTER_MAX_LOCALITIES)
                        .build();
                eventBus.initCatLocSelector(view.getSelectorPopup().getSelectorPanel(), builder);
                eventBus.setCatLocs(view.getLocalities(), builder.getInstanceId());
                view.getSelectorPopup().show();
            }
        });
        view.getSelectorPopup().getSubmitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (editingCategories) {
                    eventBus.fillCatLocs(view.getCategories(), Constants.USER_SETTINGS_MODULE);
                } else {
                    eventBus.fillCatLocs(view.getLocalities(), -Constants.USER_SETTINGS_MODULE);
                }
            }
        });
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Inits UserSettings widget.
     * @param holder panel
     */
    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
        if (Storage.getBusinessUserDetail() != null
            && Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.SUPPLIER)) {
            eventBus.initServicesWidget(ServiceType.SUPPLIER, view.getServicePanel());
        } else {
            eventBus.initServicesWidget(ServiceType.CLIENT, view.getServicePanel());
        }
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Sets SupplierSettings profile data.
     * @param detail object carrying supplier's profile data
     */
    public void onSetSupplierSettings(SettingDetail detail) {
        view.setSupplierSettings(detail);
        eventBus.selectService(detail.getSupplier().getServices().get(0));
    }

    /**
     * Updates supplier's settings data of given detail for current widget's data.
     * @param detail to be updated
     * @return updated detail object
     */
    public SettingDetail updateSupplierSettings(SettingDetail detail) {
        detail.getSupplier().setCategories(view.getCategories());
        detail.getSupplier().setLocalities(view.getLocalities());
        eventBus.fillServices(detail.getSupplier().getServices());
        return detail;
    }
}
