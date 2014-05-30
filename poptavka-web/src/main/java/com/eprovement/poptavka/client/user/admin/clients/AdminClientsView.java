/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.clients;

import com.eprovement.poptavka.client.common.monitors.ValidationMonitor;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.CssInjector;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.toolbar.AdminToolbarView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.eprovement.poptavka.client.user.widget.grid.cell.CreatedDateCell;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserDataField;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.UserField;
import com.eprovement.poptavka.shared.domain.FullClientDetail.ClientField;
import com.eprovement.poptavka.shared.domain.adminModule.AdminClientDetail;
import com.eprovement.poptavka.shared.domain.demand.OriginDetail;
import com.eprovement.poptavka.shared.search.SortDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.github.gwtbootstrap.client.ui.FluidContainer;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author Martin Slavkovsky
 */
public class AdminClientsView extends Composite implements AdminClientsPresenter.AdminClientsInterface {

    /**************************************************************************/
    /*                              UIBINDER                                  */
    /**************************************************************************/
    private static AdminClientsViewUiBinder uiBinder = GWT.create(AdminClientsViewUiBinder.class);

    interface AdminClientsViewUiBinder extends UiBinder<Widget, AdminClientsView> {
    }

    /**************************************************************************/
    /*                              ATTRIBUTES                                */
    /**************************************************************************/
    /** Constants. **/
    private static final DataGridResources GRSCS = GWT.create(DataGridResources.class);
    //Table constants
    private static final String ID_COL_WIDTH = "60px";
    private static final String DATE_COL_WIDTH = "80px";
    private static final String EMAIL_COL_WIDTH = "50%";
    private static final String FIRST_NAME_COL_WIDTH = "25%";
    private static final String LAST_NAME_COL_WIDTH = "25%";

    /** UiBinder attributes. **/
    @UiField(provided = true) ValidationMonitor emailMonitor;
    @UiField(provided = true) UniversalAsyncGrid<AdminClientDetail> table;
    @UiField SimplePanel footerContainer, advertisementContainer;
    @UiField FluidContainer changeEmailContainer, changeOriginContainer;
    @UiField Button saveEmailBtn, saveOriginBtn;
    @UiField ListBox originListBox;

    /** Class attributes. **/
    private List<SortPair> sortColumns = new ArrayList<SortPair>();
    private SingleSelectionModel<AdminClientDetail> selectionModel
        = new SingleSelectionModel(AdminClientDetail.KEY_PROVIDER);

    @Inject
    protected AdminToolbarView toolbar;

    /**************************************************************************/
    /*                              CSS                                       */
    /**************************************************************************/
    static {
        CssInjector.INSTANCE.ensureDetailStylesInjected();
        CssInjector.INSTANCE.ensureCommonStylesInjected();
    }

    /**************************************************************************/
    /*                              INITIALIZATION                            */
    /**************************************************************************/
    /**
     * creates WIDGET view.
     */
    @Override
    public void createView() {
        initDataGrid();
        initValidationMonitors();
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Initialize validation monitors for each field we want to validate.
     */
    private void initValidationMonitors() {
        emailMonitor = new ValidationMonitor<BusinessUserDetail>(BusinessUserDetail.class, UserField.EMAIL.getValue());
    }

    /**
     * Creates table with accessories - columns, pager, selection model.
     */
    private void initDataGrid() {
        GWT.log("init AdminClientsView DataGrid initialized");

        // TABLE
        CssInjector.INSTANCE.ensureGridStylesInjected(GRSCS);
        table = new UniversalAsyncGrid(Constants.PAGER_SIZE_DEFAULT, GRSCS);
        table.setSelectionModel(selectionModel);
        table.setWidth("100%");
        table.setHeight("100%");

        // PAGER
        this.toolbar.getPager().setVisible(true);
        this.toolbar.bindPager(this.table);

        // COLUMNS
        initTableColumns();
        initTableSort();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // ID
        sortColumns.add(SortPair.asc(ClientField.ID));
        table.addColumn(new TextCell(),
            Storage.MSGS.columnID(), true, ID_COL_WIDTH,
            new GetValue<String>() {

                @Override
                public String getValue(Object object) {
                    return String.valueOf(((AdminClientDetail) object).getId());
                }
            });
        // User ID
        //TOOD
        sortColumns.add(SortPair.asc(UserField.ID));
        table.addColumn(new TextCell(),
            "User Id", true, ID_COL_WIDTH,
            new GetValue<String>() {

                @Override
                public String getValue(Object object) {
                    return String.valueOf(((AdminClientDetail) object).getUserId());
                }
            });

        // created
        sortColumns.add(SortPair.asc(UserField.CREATED));
        table.addColumn(new CreatedDateCell(), Storage.MSGS.columnCreatedDate(), true, DATE_COL_WIDTH,
            new GetValue<Date>() {

                @Override
                public Date getValue(Object object) {
                    return ((AdminClientDetail) object).getCreated();
                }
            });
        // email
        sortColumns.add(SortPair.asc(UserField.EMAIL));
        table.addColumn(new TextCell(),
            Storage.MSGS.columnEmail(), true, EMAIL_COL_WIDTH,
            new GetValue<String>() {

                @Override
                public String getValue(Object object) {
                    return ((AdminClientDetail) object).getEmail();
                }
            });
        // fistName
        sortColumns.add(SortPair.asc(UserDataField.FIRST_NAME));
        table.addColumn(new TextCell(),
            Storage.MSGS.columnFirstName(), true, FIRST_NAME_COL_WIDTH,
            new GetValue<String>() {

                @Override
                public String getValue(Object object) {
                    return ((AdminClientDetail) object).getFirstName();
                }
            });

        // lastName
        sortColumns.add(SortPair.asc(UserDataField.LAST_NAME));
        table.addColumn(new TextCell(),
            Storage.MSGS.columnLastName(), true, LAST_NAME_COL_WIDTH,
            new GetValue<String>() {

                @Override
                public String getValue(Object object) {
                    return ((AdminClientDetail) object).getLastName();
                }
            });
    }

    /**
     * Inits sort settings for table like default sort and sortable column names.
     */
    private void initTableSort() {
        List<SortPair> defaultSort = Arrays.asList(SortPair.desc(UserField.CREATED));
        table.setGridColumns(new SortDataHolder(defaultSort, sortColumns));
    }

    /**************************************************************************/
    /*                              Setters                                   */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public void setOrigins(List<OriginDetail> result) {
        originListBox.clear();
        originListBox.addItem("", "0");
        for (OriginDetail originDetail : result) {
            originListBox.addItem(originDetail.getName(), originDetail.getId().toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setClientDetail(AdminClientDetail client) {
        boolean isClientSet = client != null;
        advertisementContainer.setVisible(!isClientSet);
        changeEmailContainer.setVisible(isClientSet);
        changeOriginContainer.setVisible(isClientSet);

        if (isClientSet) {
            emailMonitor.setValue(client.getEmail());
            if (client.getOrigin() == null) {
                originListBox.setSelectedIndex(0);
            } else {
                for (int i = 0; i < originListBox.getItemCount(); i++) {
                    if (client.getOrigin().getId() == Long.parseLong(originListBox.getValue(i))) {
                        originListBox.setSelectedIndex(i);
                        break;
                    }
                }
            }
        }
    }

    /**************************************************************************/
    /*                              Getters                                   */
    /**************************************************************************/
    /**
     * {@inheritDoc}
     */
    @Override
    public UniversalAsyncGrid<AdminClientDetail> getTable() {
        return table;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public SingleSelectionModel<AdminClientDetail> getSelectionModel() {
        return selectionModel;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Button getSaveEmailBtn() {
        return saveEmailBtn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Button getSaveOriginBtn() {
        return saveOriginBtn;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getNewEmail() {
        return (String) emailMonitor.getValue();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Long getSelectedOriginId() {
        return Long.parseLong(originListBox.getValue(originListBox.getSelectedIndex()));
    }

    /**
     * @return the AdminToolbar view
     */
    @Override
    public AdminToolbarView getToolbar() {
        return toolbar;
    }
}
