/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.info;

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
public class FooterInfo extends Composite {

    /**************************************************************************/
    /* UiBinders & UiTemplates                                                  */
    /**************************************************************************/
    @UiTemplate("FAQ.ui.xml")
    interface FAQBinder extends UiBinder<Widget, FooterInfo> {
    }
    private static FAQBinder faqBinder = GWT.create(FAQBinder.class);

    @UiTemplate("AboutUs.ui.xml")
    interface AboutUsBinder extends UiBinder<Widget, FooterInfo> {
    }
    private static AboutUsBinder aboutUsBinder = GWT.create(AboutUsBinder.class);

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    protected FooterInfo(UiBinder<Widget, FooterInfo> uiBinder) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Creates widget using HowItWorksDemand.ui.xml.
     * @return widget instance
     */
    public static FooterInfo createFAQ() {
        return new FooterInfo(faqBinder);
    }

    /**
     * Creates widget using HowItWorksSupplier.ui.xml.
     * @return widget instance
     */
    public static FooterInfo createAboutUs() {
        return new FooterInfo(aboutUsBinder);
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
