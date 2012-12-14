/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.services.ServicesSelectorView;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.ArrayList;
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
    @UiField
    TextBox supplierRating;
    @UiField
    DisclosurePanel categories, localities, services;
    @UiField(provided = true)
    PopupPanel categorySelectorPopup, localitySelectorPopup;
    //
    private List<CategoryDetail> categoriesList;
    private List<LocalityDetail> localitiesList;
    private List<String> originalsStorage = new ArrayList<String>();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        createCategorySelectorPopup();
        createLocalitySelectorPopup();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Initialization - helper methods                                        */
    /**************************************************************************/
    private void createCategorySelectorPopup() {
        categorySelectorPopup = new PopupPanel(true);
        categorySelectorPopup.setSize("300px", "300px");
        categorySelectorPopup.setGlassEnabled(true);
        categorySelectorPopup.hide();
    }

    private void createLocalitySelectorPopup() {
        localitySelectorPopup = new PopupPanel(true);
        localitySelectorPopup.setSize("300px", "300px");
        localitySelectorPopup.setGlassEnabled(true);
        localitySelectorPopup.hide();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setSupplierSettings(SettingDetail detail) {
        if (detail.getSupplier().getOverallRating() != null) {
            supplierRating.setText(Integer.toString(detail.getSupplier().getOverallRating()));
        }
        categoriesList = detail.getSupplier().getCategories();
        setCategoriesHeader(detail.getSupplier().getCategories().toString());
        localitiesList = detail.getSupplier().getLocalities();
        setLocalitiesHeader(detail.getSupplier().getLocalities().toString());
        setServicesHeader(detail.getSupplier().getServices().toString());
    }

    @Override
    public SettingDetail updateSupplierSettings(SettingDetail detail) {
        detail.getSupplier().setCategories(categoriesList);
        detail.getSupplier().setLocalities(localitiesList);
        SimplePanel servicesHolder = (SimplePanel) services.getContent();
        ServicesSelectorView servicesWidget = (ServicesSelectorView) servicesHolder.getWidget();
        if (servicesWidget != null) {
            detail.getSupplier().setServices(Arrays.asList(servicesWidget.getSelectedService()));
        }
        return detail;
    }

    /** HEADER. **/
    @Override
    public void setCategoriesHeader(String headerText) {
        if (originalsStorage.contains(headerText)) {
            originalsStorage.remove(headerText);
        } else {
            originalsStorage.add(headerText);
        }
        ((HasText) categories.getHeader().asWidget()).setText(
                Storage.MSGS.categories() + ": " + headerText);
    }

    @Override
    public void setLocalitiesHeader(String headerText) {
        if (originalsStorage.contains(headerText)) {
            originalsStorage.remove(headerText);
        } else {
            originalsStorage.add(headerText);
        }
        ((HasText) localities.getHeader().asWidget()).setText(
                Storage.MSGS.localities() + ": " + headerText);
    }

    @Override
    public void setServicesHeader(String headerText) {
        if (originalsStorage.contains(headerText)) {
            originalsStorage.remove(headerText);
        } else {
            originalsStorage.add(headerText);
        }
        ((HasText) services.getHeader().asWidget()).setText(
                Storage.MSGS.services() + ": " + headerText);
    }

    @Override
    public void setCategoriesList(List<CategoryDetail> categoriesList) {
        this.categoriesList = categoriesList;
    }

    @Override
    public void setLocalitiesList(List<LocalityDetail> localitiesList) {
        this.localitiesList = localitiesList;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public DisclosurePanel getCategoriesPanel() {
        return categories;
    }

    @Override
    public DisclosurePanel getLocalitiesPanel() {
        return localities;
    }

    @Override
    public DisclosurePanel getServicesPanel() {
        return services;
    }

    @Override
    public PopupPanel getCategorySelectorPopup() {
        return categorySelectorPopup;
    }

    @Override
    public PopupPanel getLocalitySelectorPopup() {
        return localitySelectorPopup;
    }

    /** TEXTBOXES. **/
    @Override
    public TextBox getSupplierRating() {
        return supplierRating;
    }

    /** OTHERES. **/
    @Override
    public List<CategoryDetail> getCategories() {
        return categoriesList;
    }

    @Override
    public List<LocalityDetail> getLocalities() {
        return localitiesList;
    }

    @Override
    public boolean isSettingChange() {
        return !originalsStorage.isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
