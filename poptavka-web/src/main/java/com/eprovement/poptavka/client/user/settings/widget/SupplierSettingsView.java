/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DisclosurePanel;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

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
    /* GETTERS                                                                */
    /**************************************************************************/
    /** PANELS. **/
    @Override
    public DisclosurePanel getCategories() {
        return categories;
    }

    @Override
    public DisclosurePanel getLocalities() {
        return localities;
    }

    @Override
    public DisclosurePanel getServices() {
        return services;
    }

    /** TEXTBOXES. **/
    @Override
    public TextBox getSupplierRating() {
        return supplierRating;
    }

    /** OTHERES. **/
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
