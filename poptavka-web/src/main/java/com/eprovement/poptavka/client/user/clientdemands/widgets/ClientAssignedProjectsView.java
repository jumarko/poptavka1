package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientAssignedProjectsView extends Composite
        implements ClientAssignedProjectsPresenter.ClientAssignedProjectsLayoutInterface {

    private static ClientAssignedProjectsLayoutViewUiBinder uiBinder =
            GWT.create(ClientAssignedProjectsLayoutViewUiBinder.class);

    interface ClientAssignedProjectsLayoutViewUiBinder extends UiBinder<Widget, ClientAssignedProjectsView> {
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
