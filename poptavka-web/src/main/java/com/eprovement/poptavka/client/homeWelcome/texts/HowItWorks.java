package com.eprovement.poptavka.client.homeWelcome.texts;

import com.eprovement.poptavka.client.common.session.CssInjector;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiTemplate;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Widget uses two UiBinders, one for HowItWorks for demands, the other for HowItWorks for supplier.
 * Widget initialization is made by invoking create (static) methods.
 *
 * @author Martin Slavkovsky
 */
public class HowItWorks extends Composite {

    public enum HowItWorksViews {

        HOW_IT_WORKS_DEMAND("HowItWorksDemand"),
        HOW_IT_WORKS_SUPPLIER("HowItWorksSupplier");

        private HowItWorksViews(String value) {
            this.value = value;
        }
        private String value;

        public String getValue() {
            return value;
        }
    }

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
    @UiField Button registerBtn;
    @UiField SimplePanel footerContainer;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    protected HowItWorks(UiBinder<Widget, HowItWorks> uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
        CssInjector.INSTANCE.ensureCommonStylesInjected();
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
    public Button getRegisterBtn() {
        return registerBtn;
    }

    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    public Widget getWidgetView() {
        return this;
    }
}
