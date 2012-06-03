package com.eprovement.poptavka.client.home.createSupplier.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.Widget;

public class SupplierServiceWidget extends Composite {

    private static SupplierServiceUiBinder uiBinder = GWT.create(SupplierServiceUiBinder.class);
    interface SupplierServiceUiBinder extends UiBinder<Widget, SupplierServiceWidget> {
    }

    @UiField RadioButton serviceOne;
    @UiField RadioButton serviceTwo;
    @UiField RadioButton serviceThree;


    public SupplierServiceWidget() {
        initWidget(uiBinder.createAndBindUi(this));
    }

}
