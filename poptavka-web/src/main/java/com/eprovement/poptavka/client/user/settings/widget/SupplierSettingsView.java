/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.ListChangeMonitor;
import com.eprovement.poptavka.client.common.category.CategoryCell;
import com.eprovement.poptavka.client.common.locality.LocalityCell;
import com.eprovement.poptavka.client.common.services.ServicesSelectorView;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.ChangeDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class SupplierSettingsView extends Composite implements SupplierSettingsPresenter.SupplierSettingsViewInterface {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static SupplierSettingsView.SupplierSettingsViewUiBinder uiBinder = GWT
            .create(SupplierSettingsView.SupplierSettingsViewUiBinder.class);

    interface SupplierSettingsViewUiBinder extends
            UiBinder<Widget, SupplierSettingsView> {
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField(provided = true) CellList categories, localities;
    @UiField(provided = true) ListChangeMonitor categoriesMonitor, localitiesMonitor;
    @UiField Label supplierRating;
    @UiField SimplePanel servicePanel;
    @UiField Anchor revert;
    @UiField Button editCatBtn, editLocBtn;
    //
    private PopupPanel selectorPopup;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        createCategorySelectorPopup();

        categories = new CellList<CategoryDetail>(new CategoryCell(CategoryCell.DISPLAY_COUNT_DISABLED));
        localities = new CellList<LocalityDetail>(new LocalityCell(LocalityCell.DISPLAY_COUNT_DISABLED));

        initValidationMonitors();

        initWidget(uiBinder.createAndBindUi(this));

        StyleResource.INSTANCE.common().ensureInjected();
    }

    private void initValidationMonitors() {
        categoriesMonitor = new ListChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(FullDemandDetail.DemandField.CATEGORIES.getValue()));
        localitiesMonitor = new ListChangeMonitor<FullDemandDetail>(
                FullDemandDetail.class, new ChangeDetail(FullDemandDetail.DemandField.LOCALITIES.getValue()));
    }

    /**************************************************************************/
    /* Initialization - helper methods                                        */
    /**************************************************************************/
    private void createCategorySelectorPopup() {
        selectorPopup = new PopupPanel(true);
        selectorPopup.setSize("300px", "300px");
        selectorPopup.setGlassEnabled(true);
        selectorPopup.hide();
    }

    /**************************************************************************/
    /* Change monitoring methods                                              */
    /**************************************************************************/
    @Override
    public void commit() {
        categoriesMonitor.commit();
        localitiesMonitor.commit();
        revert.setVisible(false);
        servicePanel.removeStyleName(Storage.RSCS.common().changed());
        //Sets also original value
        getServiceWidget().setService(getServiceWidget().getSelectedService());
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setChangeHandler(ChangeHandler handler) {
        categoriesMonitor.addChangeHandler(handler);
        localitiesMonitor.addChangeHandler(handler);
    }

    @Override
    public void setSupplierSettings(SettingDetail detail) {
        if (detail.getSupplier() != null
                && detail.getSupplier().getOverallRating() != null) {
            supplierRating.setText(Integer.toString(detail.getSupplier().getOverallRating()));
            this.categoriesMonitor.setBothValues(detail.getSupplier().getCategories());
            this.localitiesMonitor.setBothValues(detail.getSupplier().getLocalities());
        }

    }

    @Override
    public void setCategories(List<CategoryDetail> categoriesList) {
        this.categoriesMonitor.setValue(categoriesList);
    }

    @Override
    public void setLocalities(List<LocalityDetail> localitiesList) {
        this.localitiesMonitor.setValue(localitiesList);
    }

    @Override
    public SettingDetail updateSupplierSettings(SettingDetail detail) {
        detail.getSupplier().setCategories((List<CategoryDetail>) categoriesMonitor.getValue());
        detail.getSupplier().setCategories((List<CategoryDetail>) localitiesMonitor.getValue());
        detail.getSupplier().setServices(Arrays.asList(getServiceWidget().getSelectedService()));
        return detail;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public PopupPanel getSelectorWidgetPopup() {
        return selectorPopup;
    }

    @Override
    public SimplePanel getServicePanel() {
        return servicePanel;
    }

    /** BUTTONS. **/
    @Override
    public Button getEditCatBtn() {
        return editCatBtn;
    }

    @Override
    public Button getEditLocBtn() {
        return editLocBtn;
    }

    /** OTHERES. **/
    @Override
    public List<CategoryDetail> getCategories() {
        return (List<CategoryDetail>) categoriesMonitor.getValue();
    }

    @Override
    public List<LocalityDetail> getLocalities() {
        return (List<LocalityDetail>) localitiesMonitor.getValue();
    }

    @Override
    public ServicesSelectorView getServiceWidget() {
        return (ServicesSelectorView) servicePanel.getWidget();
    }

    @Override
    public Anchor getRevert() {
        return revert;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
