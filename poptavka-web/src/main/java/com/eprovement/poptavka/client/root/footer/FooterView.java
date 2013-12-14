/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.root.footer;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Widget;

import com.eprovement.poptavka.client.root.interfaces.IFooterView;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;

/**
 * Footer view includes links to Want-Something page and
 * links to Want-Something related pages (blogs, social network groups, etc.).
 *
 * @author Martin Slavkovsky
 */
public class FooterView extends Composite implements IFooterView {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static FooterViewUiBinder uiBinder = GWT.create(FooterViewUiBinder.class);

    interface FooterViewUiBinder extends UiBinder<Widget, FooterView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField Button contactUs, aboutUs, faq, privacyPolicy, termsAndConditions;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    /**
     * Creates footer view's composite.
     */
    public FooterView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the contactUs button
     */
    @Override
    public Button getContactUs() {
        return contactUs;
    }

    /**
     * @return the aboutUs button
     */
    @Override
    public Button getAboutUs() {
        return aboutUs;
    }

    /**
     * @return the faq button
     */
    @Override
    public Button getFAQ() {
        return faq;
    }

    /**
     * @return the privacy policy button
     */
    @Override
    public Button getPrivacyPolicy() {
        return privacyPolicy;
    }

    /**
     * @return the terms & conditions button
     */
    @Override
    public Button getTermsAndConditions() {
        return termsAndConditions;
    }

    /**
     * @return the widget's view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
