package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SupplierProjectsView extends Composite
        implements SupplierProjectsPresenter.SupplierProjectsLayoutInterface {

    private static SupplierProjectsLayoutViewUiBinder uiBinder =
            GWT.create(SupplierProjectsLayoutViewUiBinder.class);

    interface SupplierProjectsLayoutViewUiBinder extends UiBinder<Widget, SupplierProjectsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField
    SimplePanel contentPanel;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
