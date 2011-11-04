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
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.ClientDetail;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminClientsView extends Composite implements AdminClientsPresenter.AdminClientsInterface {

    private static AdminClientsViewUiBinder uiBinder = GWT.create(AdminClientsViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

    /**
     * @return the companyColumn
     */
    @Override
    public Column<ClientDetail, String> getCompanyColumn() {
        return companyColumn;
    }

    /**
     * @return the firstNameColumn
     */
    @Override
    public Column<ClientDetail, String> getFirstNameColumn() {
        return firstNameColumn;
    }

    /**
     * @return the lastNameColumn
     */
    @Override
    public Column<ClientDetail, String> getLastNameColumn() {
        return lastNameColumn;
    }

    /**
     * @return the ratingColumn
     */
    @Override
    public Column<ClientDetail, String> getRatingColumn() {
        return ratingColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<ClientDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<ClientDetail> getSelectionModel() {
        return selectionModel;
    }

    /**
     * @return the adminClientDetail
     */
    @Override
    public SimplePanel getAdminClientDetail() {
        return adminClientDetail;
    }

    interface AdminClientsViewUiBinder extends UiBinder<Widget, AdminClientsView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<ClientDetail> dataGrid;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    /**
     * Detail of selected Client.
     */
    @UiField
    SimplePanel adminClientDetail;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<ClientDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<ClientDetail, String> companyColumn;
    private Column<ClientDetail, String> firstNameColumn;
    private Column<ClientDetail, String> lastNameColumn;
    private Column<ClientDetail, String> ratingColumn;

    public AdminClientsView() {
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
        dataGrid = new DataGrid<ClientDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        selectionModel = new SingleSelectionModel<ClientDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel());

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // ID
        addColumn(new TextCell(), "ID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(ClientDetail object) {
                return String.valueOf(object.getId());
            }
        });

        // company
        companyColumn = addColumn(new EditTextCell(), "Company", true, 50, new GetValue<String>() {

            @Override
            public String getValue(ClientDetail object) {
                return String.valueOf(object.getUserDetail().getCompanyName());
            }
        });

        // firstName
        firstNameColumn = addColumn(new EditTextCell(), "Name", true, 80, new GetValue<String>() {

            @Override
            public String getValue(ClientDetail object) {
                return object.getUserDetail().getFirstName();
            }
        });

        // lastName
        lastNameColumn = addColumn(new EditTextCell(), "Surname", true, 80, new GetValue<String>() {

            @Override
            public String getValue(ClientDetail object) {
                return object.getUserDetail().getLastName();
            }
        });

        // rating
        ratingColumn = addColumn(new EditTextCell(), "Rating", true, 40, new GetValue<String>() {

            @Override
            public String getValue(ClientDetail object) {
                if (object.getOveralRating() == -1) {
                    return "";
                } else {
                    return Integer.toString(object.getOveralRating());
                }
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(ClientDetail clientDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<ClientDetail, C> addColumn(Cell<C> cell, String headerText, boolean sort, int width,
            final GetValue<C> getter) {
        Column<ClientDetail, C> column = new Column<ClientDetail, C>(cell) {

            @Override
            public C getValue(ClientDetail object) {
                return getter.getValue(object);
            }
        };
        if (sort) {
            column.setSortable(true);
        }
        dataGrid.addColumn(column, headerText);
        dataGrid.setColumnWidth(column, width, Unit.PX);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a ClientDetail.
     */
    private static final ProvidesKey<ClientDetail> KEY_PROVIDER = new ProvidesKey<ClientDetail>() {

        @Override
        public Object getKey(ClientDetail item) {
            return item == null ? null : item.getId();
        }
    };

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