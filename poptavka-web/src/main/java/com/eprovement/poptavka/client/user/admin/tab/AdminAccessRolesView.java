/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminAccessRolesView extends Composite implements AdminAccessRolesPresenter.AdminAccessRolesInterface {

    private static AdminAccessRolesViewUiBinder uiBinder = GWT.create(AdminAccessRolesViewUiBinder.class);

    interface AdminAccessRolesViewUiBinder extends UiBinder<Widget, AdminAccessRolesView> {
    }
    //
    //                          ***** ATTRIBUTES *****
    //
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;
    // PAGER
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    // TABLE
    @UiField(provided = true)
    DataGrid<AccessRoleDetail> dataGrid;
    private SingleSelectionModel<AccessRoleDetail> selectionModel;
    // Editable Columns
    private Column<AccessRoleDetail, String> nameColumn;
    private Column<AccessRoleDetail, String> descriptionColumn;
    private Column<AccessRoleDetail, String> permissionsColumn;
    // The key provider that provides the unique ID of a AccessRoleDetail.
    private static final ProvidesKey<AccessRoleDetail> KEY_PROVIDER = new ProvidesKey<AccessRoleDetail>() {

        @Override
        public Object getKey(AccessRoleDetail item) {
            return item == null ? null : item.getId();
        }
    };
    //
    //                          ***** INITIALIZATION *****
    //

    /**
     * creates WIDGET view
     */
    @Override
    public void createView() {
        pageSizeCombo = new ListBox();
        pageSizeCombo.addItem("10");
        pageSizeCombo.addItem("15");
        pageSizeCombo.addItem("20");
        pageSizeCombo.addItem("25");
        pageSizeCombo.addItem("30");
        pageSizeCombo.setSelectedIndex(1);
        initDataGrid();
        initWidget(uiBinder.createAndBindUi(this));
        changesLabel.setText("0");
    }

    /**
     * Creates table with accessories - columns, pager, selection model
     */
    private void initDataGrid() {
        GWT.log("init AdminAccessRoles DataGrid initialized");

        // TABLE
        dataGrid = new DataGrid<AccessRoleDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // SELECTION MODEL
        selectionModel = new SingleSelectionModel<AccessRoleDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel());

        // COLUMNS
        initTableColumns();
    }

    /**
     * Add columns to the table.
     */
    private void initTableColumns() {

        // AccessRole ID.
        addColumn(new TextCell(), "ID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(AccessRoleDetail object) {
                return String.valueOf(object.getId());
            }
        });

        // Code
        addColumn(new TextCell(), "Code", true, 50, new GetValue<String>() {

            @Override
            public String getValue(AccessRoleDetail object) {
                return object.getCode();
            }
        });

        // Name
        nameColumn = addColumn(new EditTextCell(), "Name", true, 100, new GetValue<String>() {

            @Override
            public String getValue(AccessRoleDetail object) {
                return object.getName();
            }
        });

        // Description
        descriptionColumn = addColumn(new EditTextCell(), "Description", true, 160, new GetValue<String>() {

            @Override
            public String getValue(AccessRoleDetail object) {
                return object.getDescription();
            }
        });

        // Preferences
        permissionsColumn = addColumn(new TextCell(), "Permissions", false, 140, new GetValue<String>() {

            @Override
            public String getValue(AccessRoleDetail object) {
                StringBuilder str = new StringBuilder();
                for (PermissionDetail item : object.getPermissions()) {
                    str.append(item.getName());
                    str.append(", ");
                }
                str.delete(str.length() - 2, str.length());
                return str.toString();
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private interface GetValue<C> {

        C getValue(AccessRoleDetail accessRoleDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<AccessRoleDetail, C> addColumn(Cell<C> cell, String headerText, boolean sort, int width,
            final GetValue<C> getter) {
        Column<AccessRoleDetail, C> column = new Column<AccessRoleDetail, C>(cell) {

            @Override
            public C getValue(AccessRoleDetail object) {
                return getter.getValue(object);
            }
        };
        if (sort) {
            column.setSortable(true);
        }
        sort = false;
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }

    //******************* GETTER METHODS (defined by interface) ****************
    //
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public DataGrid<AccessRoleDetail> getDataGrid() {
        return dataGrid;
    }

    /*
     * @return table column: NAME
     */
    @Override
    public Column<AccessRoleDetail, String> getNameColumn() {
        return nameColumn;
    }

    /**
     * @return table column: DESCRIPTION
     */
    @Override
    public Column<AccessRoleDetail, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    /**
     * @return table column: PERMISSIONS
     */
    @Override
    public Column<AccessRoleDetail, String> getPermissionsColumn() {
        return permissionsColumn;
    }

    /**
     * @return table's selection model
     */
    @Override
    public SingleSelectionModel<AccessRoleDetail> getSelectionModel() {
        return selectionModel;
    }

    //                         *** PAGER ***
    /*
     * @return pager
     */
    @Override
    public SimplePager getPager() {
        return pager;
    }

    /**
     * @return table/pager size: COMBO
     */
    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    /**
     * @return table/pager size: VALUE
     */
    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }

    //                          *** BUTTONS ***
    /**
     * @return COMMIT button
     */
    @Override
    public Button getCommitBtn() {
        return commit;
    }

    /**
     * @return ROLLBACK button
     */
    @Override
    public Button getRollbackBtn() {
        return rollback;
    }

    /**
     * @return REFRESH button
     */
    @Override
    public Button getRefreshBtn() {
        return refresh;
    }

    //                          *** OTHER ***
    /**
     * @return label for displaying informations for user
     */
    @Override
    public Label getChangesLabel() {
        return changesLabel;
    }

    /**
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}