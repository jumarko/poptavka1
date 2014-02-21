/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.interfaces.IAbstractClient;
import com.eprovement.poptavka.client.user.clientdemands.toolbar.ClientToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import java.util.Set;

/**
 * Common UI elements for ClientDemandsModule.
 * @author Martin Slavkovsky
 */
public class AbstractClientView extends Composite implements IAbstractClient.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static AbstractClientViewUiBinder uiBinder = GWT.create(AbstractClientViewUiBinder.class);

    interface AbstractClientViewUiBinder extends UiBinder<Widget, AbstractClientView> {
    }

    /**************************************************************************/
    /* CSS                                                                    */
    /**************************************************************************/
    static {
        //for change monitors
        Storage.RSCS.common().ensureInjected();
        //for popups created of image hover in datagrid
        Storage.RSCS.modal().ensureInjected();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField(provided = true) UniversalAsyncGrid parentTable;
    @UiField(provided = true) UniversalAsyncGrid childTable;
    @UiField Label childTableLabel;
    @UiField SimplePanel footerContainer, detailPanel;
    /** Class attributes. **/
    @Inject
    protected ClientToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates AbstractClient view's components.
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * This is called before createView because it is called from createPresenter,
     * which is called before createView (See: https://code.google.com/p/mvp4g/wiki/Mvp4gOptimization).
     * It is done this way, because tables are provided form presenter and binding table events
     * must by afterwards they are created. And View by default is initializated before presenter,
     * therefore we need to use LazyView, but in that case
     * we are not able to pass tables as arguments while creating view.
     * @param parentTable
     * @param childTable
     */
    @Override
    public void initTables(UniversalAsyncGrid parentTable, UniversalAsyncGrid childTable) {
        this.parentTable = parentTable;
        this.childTable = childTable;
        this.toolbar.getPager().setVisible(true);
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets parent table visibility.
     * @param visible true to show, false to hide.
     */
    @Override
    public void setParentTableVisible(boolean visible) {
        SetSelectionModel selectionModel = (SetSelectionModel) parentTable.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.clear();
        }
        parentTable.setVisible(visible);
        parentTable.redraw();
    }

    /**
     * Sets child table visibility.
     * @param visible true to show, false to hide.
     */
    @Override
    public void setChildTableVisible(boolean visible) {
        SetSelectionModel selectionModel = (SetSelectionModel) childTable.getSelectionModel();
        if (selectionModel != null) {
            selectionModel.clear();
        }
        childTable.setVisible(visible);
        childTable.redraw();
        childTableLabel.setVisible(visible);
    }

    /**
     * Sets demand title label text.
     * @param text
     */
    @Override
    public void setDemandTitleLabel(String text) {
        childTableLabel.setText(text);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return child table selected objects
     */
    @Override
    public Set getChildTableSelectedObjects() {
        if (childTable.getSelectionModel() != null) {
            return ((SetSelectionModel) childTable.getSelectionModel()).getSelectedSet();
        } else {
            return null;
        }
    }

    /**
     * @return the parent table
     */
    @Override
    public UniversalAsyncGrid getParentTable() {
        return parentTable;
    }

    /**
     * @return the child table
     */
    @Override
    public UniversalAsyncGrid getChildTable() {
        return childTable;
    }

    /**
     * @return the footer container
     */
    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    /**
     * @return the detail panel
     */
    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    /**
     * @return the ClientToolbarView
     */
    @Override
    public ClientToolbarView getToolbar() {
        return toolbar;
    }

    /**
     * @return the widget view
     */
    @Override
    public IsWidget getWidgetView() {
        return this;
    }
}