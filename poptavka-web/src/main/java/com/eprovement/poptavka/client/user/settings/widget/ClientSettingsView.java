/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.settings.widget;

import com.eprovement.poptavka.client.common.forms.RatingInfoForm;
import com.eprovement.poptavka.client.user.settings.interfaces.IClientSettings;
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
public class ClientSettingsView extends Composite implements IClientSettings.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static ClientSettingsViewUiBinder uiBinder = GWT.create(ClientSettingsViewUiBinder.class);

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
     * @{inheritDoc}
     */
    @Override
    public void setClientSettings(SettingDetail detail) {
        ratingInfoForm.setRating(detail.getClientRating());
    }

    /**
     * @{inheritDoc}
     */
    @Override
    public void fillClientSettings(SettingDetail detail) {
        // nothing to set - ratings will be calculated automatically.
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void reset() {
        //nothing by default
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public boolean isValid() {
        return true;
    }
}
