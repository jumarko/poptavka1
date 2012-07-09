package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientProjectsView extends Composite
        implements ClientProjectsPresenter.ClientProjectsLayoutInterface {

    private static ClientProjectsLayoutViewUiBinder uiBinder = GWT.create(ClientProjectsLayoutViewUiBinder.class);

    interface ClientProjectsLayoutViewUiBinder extends UiBinder<Widget, ClientProjectsView> {
    }
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    @UiField
    SimplePanel contentPanel;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
