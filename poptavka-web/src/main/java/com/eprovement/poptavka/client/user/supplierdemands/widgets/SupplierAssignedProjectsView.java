package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class SupplierAssignedProjectsView extends Composite
        implements SupplierAssignedProjectsPresenter.SupplierAssignedProjectsLayoutInterface {

    private static SupplierAssignedProjectsLayoutViewUiBinder uiBinder =
            GWT.create(SupplierAssignedProjectsLayoutViewUiBinder.class);

    interface SupplierAssignedProjectsLayoutViewUiBinder extends UiBinder<Widget, SupplierAssignedProjectsView> {
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
