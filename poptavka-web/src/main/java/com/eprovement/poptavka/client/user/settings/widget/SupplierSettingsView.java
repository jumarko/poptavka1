/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.DomEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    TextBox supplierRating, status;
    @UiField
    DisclosurePanel categories, localities, services;
    @UiField(provided = true)
    PopupPanel categorySelectorPopup, localitySelectorPopup;
    //
    private String stringStorage;
    private List<CategoryDetail> categoriesList;
    private List<LocalityDetail> localitiesList;
    private List<Integer> servicesList;
    private Map<String, String> originalsStorage = new HashMap<String, String>();

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    @Override
    public void createView() {
        createCategorySelectorPopup();
        createLocalitySelectorPopup();
        initWidget(uiBinder.createAndBindUi(this));
        supplierRating.setEnabled(false);
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
    /* Methods handled by view                                                */
    /**************************************************************************/
    @Override //.substring(1, list.toString().length() -1)
    public void initChangeCheking(String originalString) {
        stringStorage = originalString;
    }

    @Override
    public void evaluateChanges(String key, String newString) {
        if (!stringStorage.equals(newString)) {
            if (originalsStorage.containsKey(key)) {
                if (originalsStorage.get(key).equals(newString)) {
                    originalsStorage.remove(newString);
                }
            } else {
                originalsStorage.put(key, stringStorage);
            }
        }
        updateStatus();
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    @Override
    public void setSupplierSettings(SettingDetail detail) {
        originalsStorage.clear();
        if (detail.getSupplier().getOverallRating() != null) {
            supplierRating.setText(Integer.toString(detail.getSupplier().getOverallRating()));
        }
        categoriesList = detail.getSupplier().getCategories();
        setCategoriesHeader(detail.getSupplier().getCategories().toString());
        localitiesList = detail.getSupplier().getLocalities();
        setLocalitiesHeader(detail.getSupplier().getLocalities().toString());
        servicesList = detail.getSupplier().getServices();
        setServicesHeader(detail.getSupplier().getServices().toString());
    }

    @Override
    public SettingDetail updateSupplierSettings(SettingDetail detail) {
        detail.getSupplier().setCategories(categoriesList);
        detail.getSupplier().setLocalities(localitiesList);
        detail.getSupplier().setServices(servicesList);
        return detail;
    }

    /** HEADER. **/
    @Override
    public void setCategoriesHeader(String headerText) {
        ((HasText) categories.getHeader().asWidget()).setText(
                Storage.MSGS.categories() + ": " + headerText);
    }

    @Override
    public void setLocalitiesHeader(String headerText) {
        ((HasText) localities.getHeader().asWidget()).setText(
                Storage.MSGS.localities() + ": " + headerText);
    }

    @Override
    public void setServicesHeader(String headerText) {
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

    @Override
    public void setServicesList(List<Integer> servicesList) {
        this.servicesList = servicesList;
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

    @Override
    public TextBox getStatus() {
        return status;
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
    public List<Integer> getServices() {
        return servicesList;
    }

    @Override
    public boolean isSettingChange() {
        return !originalsStorage.isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void updateStatus() {
        status.setText(originalsStorage.toString());
        DomEvent.fireNativeEvent(Document.get().createChangeEvent(), status);
    }
}
