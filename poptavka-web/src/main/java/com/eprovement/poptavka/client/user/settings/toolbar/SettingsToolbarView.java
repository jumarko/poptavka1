package com.eprovement.poptavka.client.user.settings.toolbar;

import com.github.gwtbootstrap.client.ui.Tooltip;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Singleton;

/**
 *
 * @author Martin Slavkovsky
 */
@Singleton
public class SettingsToolbarView extends Composite implements IsWidget {

    private static SettingsToolbarViewUiBinder uiBinder = GWT.create(SettingsToolbarViewUiBinder.class);

    interface SettingsToolbarViewUiBinder extends UiBinder<Widget, SettingsToolbarView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    /** UiBinder attribute. **/
    @UiField Button updateButton;
    @UiField Tooltip updateBtnTooltip;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    public SettingsToolbarView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public Button getUpdateButton() {
        return updateButton;
    }

    public Tooltip getUpdateBtnTooltip() {
        return updateBtnTooltip;
    }
}