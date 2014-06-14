/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.interfaces.IAbstractSupplier;
import com.eprovement.poptavka.client.user.supplierdemands.toolbar.SupplierToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.inject.Inject;
import java.util.Set;

/**
 * Common SupplierDemands' views UI compotnents.
 *
 * @author Martin Slavkovsky
 */
public class AbstractSupplierView extends Composite implements IAbstractSupplier.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static AbstractSupplierViewUiBinder uiBinder = GWT.create(AbstractSupplierViewUiBinder.class);

    interface AbstractSupplierViewUiBinder extends UiBinder<Widget, AbstractSupplierView> {
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
    @UiField(provided = true) UniversalAsyncGrid table;
    @UiField SimplePanel footerContainer, detailPanel;
    /** Class attributes. **/
    @Inject
    protected SupplierToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates AbstractSupplier view's components.
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
     * @param table
     */
    @Override
    public void initTable(UniversalAsyncGrid table) {
        this.table = table;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return list of selected objects.
     */
    @Override
    public Set getSelectedObjects() {
        if (table.getSelectionModel() != null) {
            return ((SetSelectionModel) table.getSelectionModel()).getSelectedSet();
        } else {
            return null;
        }
    }

    /**
     * @return the universal asynchronous table
     */
    @Override
    public UniversalAsyncGrid getTable() {
        return table;
    }

    /**
     * @return the footer container
     */
    @Override
    public SimplePanel getFooterContainer() {
        return footerContainer;
    }

    /**
     * @return the detail container
     */
    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    /**
     * @return the SupplierToolbarView
     */
    @Override
    public SupplierToolbarView getToolbar() {
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