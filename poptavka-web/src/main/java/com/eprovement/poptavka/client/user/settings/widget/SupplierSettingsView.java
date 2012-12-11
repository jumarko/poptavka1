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
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public class SupplierSettingsView extends Composite implements SupplierSettingsPresenter.SupplierSettingsViewInterface {

    private static SupplierSettingsView.SupplierSettingsViewUiBinder uiBinder = GWT
            .create(SupplierSettingsView.SupplierSettingsViewUiBinder.class);

    interface SupplierSettingsViewUiBinder extends
            UiBinder<Widget, SupplierSettingsView> {
    }
    @UiField
    TextBox supplierRating;
    @UiField
    TextArea servicesBox;
    @UiField
    DisclosurePanel categories, localities;

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public DisclosurePanel getCategories() {
        return categories;
    }

    @Override
    public DisclosurePanel getLocalities() {
        return localities;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
