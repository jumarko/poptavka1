package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;

public class ClientAssignedDemandsView extends Composite
        implements ClientAssignedDemandsPresenter.ClientAssignedDemandsLayoutInterface {

    private static ClientAssignedDemandsLayoutViewUiBinder uiBinder =
            GWT.create(ClientAssignedDemandsLayoutViewUiBinder.class);

    interface ClientAssignedDemandsLayoutViewUiBinder extends UiBinder<Widget, ClientAssignedDemandsView> {
    }
    /**************************************************************************/
    /* DemandOfferTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalTableWidget tableWidget;
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //detail WrapperPanel
    @UiField
    SimplePanel wrapperPanel;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        tableWidget = new UniversalTableWidget(Constants.CLIENT_ASSIGNED_DEMANDS);
        //TODO Martin - premenovat tiez i18n - Project -> Demand
        tableWidget.getTableNameLabel().setText(Storage.MSGS.clientAssignedProjectsTitle());

        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    //Table Widget
    @Override
    public UniversalTableWidget getTableWidget() {
        return tableWidget;
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}
