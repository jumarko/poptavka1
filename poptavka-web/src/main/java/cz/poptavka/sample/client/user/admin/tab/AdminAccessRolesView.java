/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

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
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.AccessRoleDetail;
import cz.poptavka.sample.shared.domain.PermissionDetail;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminAccessRolesView extends Composite implements AdminAccessRolesPresenter.AdminAccessRolesInterface {

    private static AdminAccessRolesViewUiBinder uiBinder = GWT.create(AdminAccessRolesViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

    /**
     * @return the nameColumn
     */
    @Override
    public Column<AccessRoleDetail, String> getNameColumn() {
        return nameColumn;
    }

    /**
     * @return the descriptionColumn
     */
    @Override
    public Column<AccessRoleDetail, String> getDescriptionColumn() {
        return descriptionColumn;
    }

    /**
     * @return the permissionsColumn
     */
    @Override
    public Column<AccessRoleDetail, String> getPermissionsColumn() {
        return permissionsColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<AccessRoleDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<AccessRoleDetail> getSelectionModel() {
        return selectionModel;
    }

    interface AdminAccessRolesViewUiBinder extends UiBinder<Widget, AdminAccessRolesView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<AccessRoleDetail> dataGrid;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    /**
     * Detail of selected Demand.
     */
    @UiField
    SimplePanel adminDemandDetail;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<AccessRoleDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<AccessRoleDetail, String> nameColumn;
    private Column<AccessRoleDetail, String> descriptionColumn;
    private Column<AccessRoleDetail, String> permissionsColumn;

    public AdminAccessRolesView() {
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

    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<AccessRoleDetail>();
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        selectionModel = new SingleSelectionModel<AccessRoleDetail>();
        dataGrid.setSelectionModel(getSelectionModel());

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
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
    private static interface GetValue<C> {

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

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    @Override
    public int getPageSize() {
        return Integer.valueOf(pageSizeCombo.getItemText(pageSizeCombo.getSelectedIndex()));
    }

    @Override
    public Button getCommitBtn() {
        return commit;
    }

    @Override
    public Button getRollbackBtn() {
        return rollback;
    }

    @Override
    public Button getRefreshBtn() {
        return refresh;
    }

    @Override
    public Label getChangesLabel() {
        return changesLabel;
    }
}