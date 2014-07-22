/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.interfaces.ISupplierSettings;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.SimplePanel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;

/**
 * SupplierSettings widget is part of Settings module widgets.
 * Displays supplier's profile data only.
 *
 * TODO LATER ivlcek: = check for number of categories according to payed service
 *      = authentication code when updating email
 *      = what else need to do when updating services? send invoice??.
 *
 * @author Martin Slavkovsky
 * @since 18.2.2014
 */
@Presenter(view = SupplierSettingsView.class)
public class SupplierSettingsPresenter extends LazyPresenter<ISupplierSettings.View, SettingsEventBus>
    implements ISupplierSettings.Presenter {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //history of changes
    private boolean editingCategories;
    private int instaceIdCategories;
    private int instaceIdLocalities;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onInitSupplierSettings(SimplePanel holder) {
        holder.setWidget(view);
        if (Storage.getBusinessUserDetail() != null
            && Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.SUPPLIER)) {
            eventBus.initServicesWidget(view.getServicePanel());
        } else {
            eventBus.initServicesWidget(view.getServicePanel());
        }
    }

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
                final CatLocSelectorBuilder builder =
                    new CatLocSelectorBuilder.Builder(Constants.USER_SETTINGS_MODULE)
                        .initCategorySelector()
                        .initSelectorManager()
                        .withCheckboxes()
                        .setSelectionRestriction(Constants.REGISTER_MAX_CATEGORIES)
                        .build();
                instaceIdCategories = builder.getInstanceId();
                eventBus.initCatLocSelector(view.getSelectorPopup().getSelectorPanel(), builder);
                eventBus.setCatLocs(view.getCategories(), instaceIdCategories);
                view.getCategoriesList().setVisible(!view.getCategories().isEmpty());
                view.getSelectorPopup().show();
            }
        });
        view.getEditLocBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                editingCategories = false;
                final CatLocSelectorBuilder builder =
                    new CatLocSelectorBuilder.Builder(Constants.USER_SETTINGS_MODULE)
                        .initLocalitySelector()
                        .initSelectorManager()
                        .withCheckboxes()
                        .setSelectionRestriction(Constants.REGISTER_MAX_LOCALITIES)
                        .build();
                instaceIdLocalities = builder.getInstanceId();
                eventBus.initCatLocSelector(view.getSelectorPopup().getSelectorPanel(), builder);
                eventBus.setCatLocs(view.getLocalities(), instaceIdLocalities);
                view.getLocalitiesList().setVisible(!view.getLocalities().isEmpty());
                view.getSelectorPopup().show();
            }
        });
        view.getSelectorPopup().getSubmitBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (editingCategories) {
                    eventBus.fillCatLocs(view.getCategories(), instaceIdCategories);
                } else {
                    eventBus.fillCatLocs(view.getLocalities(), instaceIdLocalities);
                }
            }
        });
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void onSetSupplierSettings(SettingDetail detail) {
        view.setSupplierSettings(detail);
        eventBus.selectService(detail.getSupplier().getServices().get(0));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onFillSupplierSettings(SettingDetail detail) {
        detail.getSupplier().setCategories(view.getCategories());
        detail.getSupplier().setLocalities(view.getLocalities());
        eventBus.fillServices(detail.getSupplier().getServices());
    }
}
