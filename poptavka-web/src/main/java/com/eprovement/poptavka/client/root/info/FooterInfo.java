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

    public enum FooterInfoViews {

        FAQ("FAQ"),
        ABOUT_US("AboutUs"),
        PRIVACY_POLICY("PrivacyPolicy");

        private FooterInfoViews(String value) {
            this.value = value;
        }
        private String value;

        public String getValue() {
            return value;
        }
    }

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

    @UiTemplate("PrivacyPolicy.ui.xml")
    interface PrivacyPolicyBinder extends UiBinder<Widget, FooterInfo> {
    }
    private static PrivacyPolicyBinder privacyPolicyBinder = GWT.create(PrivacyPolicyBinder.class);

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
     * Creates widget using FAQ.ui.xml.
     * @return widget instance
     */
    public static FooterInfo createFAQ() {
        return new FooterInfo(faqBinder);
    }

    /**
     * Creates widget using AboutUs.ui.xml.
     * @return widget instance
     */
    public static FooterInfo createAboutUs() {
        return new FooterInfo(aboutUsBinder);
    }

    /**
     * Creates widget using PrivacyPolicy.ui.xml.
     * @return widget instance
     */
    public static FooterInfo createPrivacyPolicy() {
        return new FooterInfo(privacyPolicyBinder);
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
