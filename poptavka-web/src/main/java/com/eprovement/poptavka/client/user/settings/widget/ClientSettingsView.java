/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.forms.RatingInfoForm;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.shared.domain.settings.SettingDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * View consists of client's rating element.
 *
 * @author Martin Slavkovsky
 */
public class ClientSettingsView extends Composite
    implements ClientSettingsPresenter.ClientSettingsViewInterface, ProvidesValidate {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ClientSettingsView.ClientSettingsViewUiBinder uiBinder = GWT
            .create(ClientSettingsView.ClientSettingsViewUiBinder.class);

    interface ClientSettingsViewUiBinder extends UiBinder<Widget, ClientSettingsView> {
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    @UiField RatingInfoForm ratingInfoForm;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ClientSettings view's components.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* SETTERS                                                                */
    /**************************************************************************/
    /**
     * Sets client's profile data.
     * @param detail carring profile data
     */
    @Override
    public void setClientSettings(SettingDetail detail) {
        ratingInfoForm.setRating(detail.getClientRating());
    }

    /**
     * Updates client's profile data of given object with actual widget's data.
     * @param detail to be updated
     * @return updated detail object
     */
    @Override
    public SettingDetail updateClientSettings(SettingDetail detail) {
        // nothing to set - ratings will be calculated automatically.
        return detail;
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** @{inheritDoc} */
    @Override
    public boolean isValid() {
        return true;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}