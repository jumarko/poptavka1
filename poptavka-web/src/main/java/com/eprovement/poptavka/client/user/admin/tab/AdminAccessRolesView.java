/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.admin.tab;

import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid.GetValue;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ProvidesKey;

import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.adminModule.PermissionDetail;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminAccessRolesView extends Composite implements AdminAccessRolesPresenter.AdminAccessRolesInterface {

    private static AdminAccessRolesViewUiBinder uiBinder = GWT.create(AdminAccessRolesViewUiBinder.class);

    interface AdminAccessRolesViewUiBinder extends UiBinder<Widget, AdminAccessRolesView> {
    }
    //*************************************************************************/
    //                              ATTRIBUTES                                */
    //*************************************************************************/
    //Table constants
    private static final int ID_COL_WIDTH = 50;
    private static final int CODE_COL_WIDTH = 50;
    private static final int NAME_COL_WIDTH = 100;
    private static final int DESCRIPTION_COL_WIDTH = 160;
    private static final int PREFERENCES_COL_WIDTH = 140;
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
    UniversalAsyncGrid<AccessRoleDetail> dataGrid;
    // Editable Columns
    private Column<AccessRoleDetail, String> nameColumn;
    private Column<AccessRoleDetail, String> descriptionColumn;
    private Column<AccessRoleDetail, String> permissionsColumn;
    private List<String> gridColumns = Arrays.asList(
            new String[]{
                "id", "code", "name", "description", "permissions"
            });
    // The key provider that provides the unique ID of a AccessRoleDetail.
    private static final ProvidesKey<AccessRoleDetail> KEY_PROVIDER = new ProvidesKey<AccessRoleDetail>() {

        @Override
        public Object getKey(AccessRoleDetail item) {
            return item == null ? null : item.getId();
        }
    };
    //*************************************************************************/
    //                          INITIALIZATOIN                                */
    //*************************************************************************/
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
        dataGrid = new UniversalAsyncGrid<AccessRoleDetail>(KEY_PROVIDER, gridColumns);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // COLUMNS
        initTableColumns();
    }

    /**
     * Add columns to the table.
     */
    private void initTableColumns() {

        // AccessRole ID.
        dataGrid.addColumn(new TextCell(), Storage.MSGS.id(), true, ID_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return String.valueOf(((AccessRoleDetail) object).getId());
                    }
                });

        // Code
        dataGrid.addColumn(new TextCell(), Storage.MSGS.code(), true, CODE_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((AccessRoleDetail) object).getCode();
                    }
                });

        // Name
        nameColumn = dataGrid.addColumn(new EditTextCell(), Storage.MSGS.name(), true, NAME_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((AccessRoleDetail) object).getName();
                    }
                });

        // Description
        descriptionColumn = dataGrid.addColumn(
                new EditTextCell(), Storage.MSGS.description(), true, DESCRIPTION_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        return ((AccessRoleDetail) object).getDescription();
                    }
                });

        // Preferences
        permissionsColumn = dataGrid.addColumn(
                new TextCell(), Storage.MSGS.permissions(), false, PREFERENCES_COL_WIDTH,
                new GetValue<String>() {

                    @Override
                    public String getValue(Object object) {
                        StringBuilder str = new StringBuilder();
                        for (PermissionDetail item : ((AccessRoleDetail) object).getPermissions()) {
                            str.append(item.getName());
                            str.append(", ");
                        }
                        if (str.length() != 0) {
                            str.delete(str.length() - 2, str.length());
                        }
                        return str.toString();
                    }
                });
    }

    //*************************************************************************/
    //                      GETTER METHODS (defined by interface)             */
    //*************************************************************************/
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public UniversalAsyncGrid<AccessRoleDetail> getDataGrid() {
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