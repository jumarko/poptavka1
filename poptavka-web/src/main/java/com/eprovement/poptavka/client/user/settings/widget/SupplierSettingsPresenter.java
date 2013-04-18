/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.ListChangeMonitor;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.category.CategorySelectorView;
import com.eprovement.poptavka.client.common.locality.LocalitySelectorView;
import com.eprovement.poptavka.client.common.services.ServicesSelectorView;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.settings.SettingsEventBus;
import com.eprovement.poptavka.client.user.settings.widget.SupplierSettingsPresenter.SupplierSettingsViewInterface;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IntegerBox;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;

/**
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
        void setChangeHandler(ChangeHandler changeHandler);

        void setSupplierSettings(SettingDetail detail);

        void setCategories(List<CategoryDetail> categoriesList);

        void setLocalities(List<LocalityDetail> localitiesList);

        SettingDetail updateSupplierSettings(SettingDetail detail);

        /** GETTERS. **/
        //Panels
        PopupPanel getSelectorWidgetPopup();

        SimplePanel getServicePanel();

        List<CategoryDetail> getCategories();

        List<LocalityDetail> getLocalities();

        //TextBoxes
        IntegerBox getSupplierRating();

        //Buttons
        Button getEditCatBtn();

        Button getEditLocBtn();

        //Others
        void commit();

        ServicesSelectorView getServiceWidget();

        Anchor getRevert();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    //history of changes
    private ArrayList<ChangeDetail> updatedFields = new ArrayList<ChangeDetail>();
    private SettingDetail settingsDetail;
    private boolean serviceChanged = false;
    private ChangeHandler changeDetail  = new ChangeHandler() {
            @Override
            public void onChange(ChangeEvent event) {
                if (event.getSource() instanceof ListChangeMonitor) {
                    listChangedMonitorsManager((ListChangeMonitor) event.getSource());
                } else {
                    serviceChangeMonitorManager((ServicesSelectorView) event.getSource());
                }

                eventBus.updateSupplierStatus(isSupplierSettingChanged());
            }
        };

    /**************************************************************************/
    /* BIND                                                                   */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getEditCatBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initCategoryWidget(
                        view.getSelectorWidgetPopup(),
                        Constants.WITH_CHECK_BOXES_ONLY_ON_LEAFS,
                        CategoryCell.DISPLAY_COUNT_DISABLED,
                        view.getCategories(), true);
                view.getSelectorWidgetPopup().center();
            }
        });
        view.getEditLocBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.initLocalityWidget(
                        view.getSelectorWidgetPopup(),
                        Constants.WITH_CHECK_BOXES,
                        CategoryCell.DISPLAY_COUNT_DISABLED,
                        view.getLocalities(), true);
                view.getSelectorWidgetPopup().center();
            }
        });
        view.getSelectorWidgetPopup().addCloseHandler(
                new CloseHandler<PopupPanel>() {
                    @Override
                    public void onClose(CloseEvent<PopupPanel> event) {
                        if (view.getSelectorWidgetPopup()
                                .getWidget() instanceof CategorySelectorView) {
                            view.setCategories(
                                    ((CategorySelectorView) view.getSelectorWidgetPopup().getWidget())
                                    .getCellListDataProvider().getList());
                        } else if (view.getSelectorWidgetPopup()
                                .getWidget() instanceof LocalitySelectorView) {
                            view.setLocalities(
                                    ((LocalitySelectorView) view.getSelectorWidgetPopup().getWidget())
                                    .getCellListDataProvider().getList());
                        }
                    }
                });
        view.setChangeHandler(changeDetail);
        view.getRevert().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getServicePanel().removeStyleName(Storage.RSCS.common().changed());
                view.getRevert().setVisible(false);
                serviceChanged = false;
                if (settingsDetail.getSupplier() != null
                        && settingsDetail.getSupplier().getServices() != null
                        && !settingsDetail.getSupplier().getServices().isEmpty()) {
                    view.getServiceWidget().setService(settingsDetail.getSupplier().getServices().get(0));
                }
                eventBus.updateSupplierStatus(isSupplierSettingChanged());
            }
        });
    }

    private void listChangedMonitorsManager(ListChangeMonitor source) {
        source.getChangeDetail().setValue(source.getValue());
        if (source.isModified()) {
            //if contains already - remove before adding new
            if (updatedFields.contains(source.getChangeDetail())) {
                updatedFields.remove(source.getChangeDetail());
            }
            updatedFields.add(source.getChangeDetail());
        } else {
            updatedFields.remove(source.getChangeDetail());
        }
    }

    private void serviceChangeMonitorManager(ServicesSelectorView source) {
        serviceChanged = source.isChanged();
        onServiceChanged(source.isChanged());
        eventBus.updateSupplierStatus(isSupplierSettingChanged());
    }

    private void onServiceChanged(boolean isChange) {
        view.getRevert().setVisible(isChange);
        if (isChange) {
            view.getServicePanel().addStyleName(Storage.RSCS.common().changed());
        } else {
            view.getServicePanel().removeStyleName(Storage.RSCS.common().changed());
        }
    }

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    public void initUserSettings(SimplePanel holder) {
        holder.setWidget(view.getWidgetView());
        eventBus.initServicesWidget(view.getServicePanel());
    }

    /**************************************************************************/
    /* METHODS                                                                */
    /**************************************************************************/
    public void onSetSupplierSettings(SettingDetail detail) {
        settingsDetail = detail;
        view.setSupplierSettings(detail);
    }

    public void onNofityServicesWidgetListeners() {
        if (settingsDetail.getSupplier() != null
                && settingsDetail.getSupplier().getServices() != null
                && !settingsDetail.getSupplier().getServices().isEmpty()) {
            view.getServiceWidget().setService(settingsDetail.getSupplier().getServices().get(0));
        }
        view.getServiceWidget().addChangeHandler(changeDetail);
    }

    public SettingDetail updateSupplierSettings(SettingDetail settingDetail) {
        return view.updateSupplierSettings(settingDetail);
    }

    public boolean isSupplierSettingChanged() {
        return serviceChanged || !updatedFields.isEmpty();
    }
}
