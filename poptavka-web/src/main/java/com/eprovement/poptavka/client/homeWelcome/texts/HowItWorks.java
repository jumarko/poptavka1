package com.eprovement.poptavka.client.homeWelcome.texts;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget uses two UiBinders, one for HowItWorks for demands, the other for HowItWorks for supplier.
 * Widget initialization is made by invoking create (static) methods.
 *
 * @author Martin Slavkovsky
 */
public class HowItWorks extends Composite {

    /**************************************************************************/
    /* UiBinders & UiTemplates                                                */
    /**************************************************************************/
    @UiTemplate("HowItWorksDemand.ui.xml")
    interface DemandBinder extends UiBinder<Widget, HowItWorks> {
    }
    private static DemandBinder demandBinder = GWT.create(DemandBinder.class);

    @UiTemplate("HowItWorksSupplier.ui.xml")
    interface SupplierBinder extends UiBinder<Widget, HowItWorks> {
    }
    private static SupplierBinder supplierBinder = GWT.create(SupplierBinder.class);

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    protected HowItWorks(UiBinder<Widget, HowItWorks> uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Creates widget using HowItWorksDemand.ui.xml.
     * @return widget instance
     */
    public static HowItWorks createHowItWorksDemand() {
        return new HowItWorks(demandBinder);
    }

    /**
     * Creates widget using HowItWorksSupplier.ui.xml.
     * @return widget instance
     */
    public static HowItWorks createHowItWorksSupplier() {
        return new HowItWorks(supplierBinder);
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    public Widget getWidgetView() {
        return this;
    }
}
