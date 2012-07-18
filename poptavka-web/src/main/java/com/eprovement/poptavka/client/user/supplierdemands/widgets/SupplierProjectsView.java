package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalTableWidget;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class SupplierProjectsView extends Composite
        implements SupplierProjectsPresenter.SupplierProjectsLayoutInterface {

    private static SupplierProjectsLayoutViewUiBinder uiBinder =
            GWT.create(SupplierProjectsLayoutViewUiBinder.class);

    interface SupplierProjectsLayoutViewUiBinder extends UiBinder<Widget, SupplierProjectsView> {
    }
    /**************************************************************************/
    /* DemandContestTable Attrinbutes                                         */
    /**************************************************************************/
    //table definition
    @UiField(provided = true)
    UniversalTableWidget tableWidget;
    /**************************************************************************/
    /* Attrinbutes                                                            */
    /**************************************************************************/
    //table handling buttons
    @UiField
    Button offerBtn, replyBtn;
    @UiField(provided = true)
    ListBox actions;
    //detail WrapperPanel
    @UiField
    SimplePanel wrapperPanel;
    @UiField
    Label titlelabel;
    @UiField
    VerticalPanel header;

    /**************************************************************************/
    /* Initialization                                                            */
    /**************************************************************************/
    @Override
    public void createView() {
        //load custom grid cssStyle
        Storage.RSCS.grid().ensureInjected();

        tableWidget = new UniversalTableWidget(Constants.SUPPLIER_POTENTIAL_PROJECTS);

        actions = new ListBox();
        actions.addItem(Storage.MSGS.action());
        actions.addItem(Storage.MSGS.read());
        actions.addItem(Storage.MSGS.unread());
        actions.addItem(Storage.MSGS.star());
        actions.addItem(Storage.MSGS.unstar());
        actions.setSelectedIndex(0);

        initWidget(uiBinder.createAndBindUi(this));
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    //Table
    @Override
    public UniversalTableWidget getTableWidget() {
        return tableWidget;
    }

    //Buttons
    @Override
    public Button getOfferBtn() {
        return offerBtn;
    }

    @Override
    public Button getReplyBtn() {
        return replyBtn;
    }

    //ListBox
    @Override
    public ListBox getActions() {
        return actions;
    }

    @Override
    public SimplePanel getWrapperPanel() {
        return wrapperPanel;
    }

    @Override
    public IsWidget getWidgetView() {
        return this;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setTitleLabel(String text) {
        titlelabel.setText(text);
    }
}
