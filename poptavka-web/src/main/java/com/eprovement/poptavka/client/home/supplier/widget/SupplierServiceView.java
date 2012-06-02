package com.eprovement.poptavka.client.home.supplier.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SupplierServiceView extends Composite implements SupplierServicePresenter.SupplierServiceInterface {

    private static SupplierServiceUiBinder uiBinder = GWT
            .create(SupplierServiceUiBinder.class);

    interface SupplierServiceUiBinder extends UiBinder<Widget, SupplierServiceView> {
    }

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

}
