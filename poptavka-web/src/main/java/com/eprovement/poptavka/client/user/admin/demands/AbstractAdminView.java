/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.demands;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.interfaces.IAbstractAdmin;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.inject.Inject;
import java.util.Set;

/**
 * Abstract admin view consists common UI elements.
 * @author Martin Slavkovsky
 */
public class AbstractAdminView extends Composite implements IAbstractAdmin.View {

    /**************************************************************************/
    /* UiBinder                                                               */
    /**************************************************************************/
    private static AbstractAdminViewUiBinder uiBinder = GWT.create(AbstractAdminViewUiBinder.class);

    interface AbstractAdminViewUiBinder extends UiBinder<Widget, AbstractAdminView> {
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
    protected AdminToolbarView toolbar;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates AbstractAdmin view's compontents.
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
        this.toolbar.getPager().setVisible(true);
        this.toolbar.bindPager(this.table);
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the selected objects set
     */
    @Override
    public Set getSelectedObjects() {
        MultiSelectionModel model = (MultiSelectionModel) table.getSelectionModel();
        return model.getSelectedSet();
    }

    /**
     * @return the universal asynchronous grid
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
     * @return the detail panel
     */
    @Override
    public SimplePanel getDetailPanel() {
        return detailPanel;
    }

    /**
     * @return the AdminToolbar view
     */
    @Override
    public AdminToolbarView getToolbar() {
        return toolbar;
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgeView() {
        return this;
    }
}