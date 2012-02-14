/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.EditTextCell;
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

import cz.poptavka.sample.shared.domain.ClientDetail;

/**
 *
 * @author Martin Slavkovsky
 */
public class AdminClientsView extends Composite implements AdminClientsPresenter.AdminClientsInterface {

    private static AdminClientsViewUiBinder uiBinder = GWT.create(AdminClientsViewUiBinder.class);

    interface AdminClientsViewUiBinder extends UiBinder<Widget, AdminClientsView> {
    }
    //
    //                          ***** ATTRIBUTES *****
    //
    @UiField Button commit, rollback, refresh;
    @UiField Label changesLabel;
    // DETAIL
    @UiField AdminClientInfoView adminClientDetail;
//    @UiField DemandDetailView demandDetail;
    // PAGER
    @UiField(provided = true) SimplePager pager;
    @UiField(provided = true) ListBox pageSizeCombo;
    // TABLE
    @UiField(provided = true) DataGrid<ClientDetail> dataGrid;
    private SingleSelectionModel<ClientDetail> selectionModel;
    // Editable Columns
    private Column<ClientDetail, String> idColumn;
    private Column<ClientDetail, String> companyColumn;
    private Column<ClientDetail, String> firstNameColumn;
    private Column<ClientDetail, String> lastNameColumn;
    private Column<ClientDetail, String> ratingColumn;
    // The key provider that provides the unique ID of a ClientDetail.
    private static final ProvidesKey<ClientDetail> KEY_PROVIDER = new ProvidesKey<ClientDetail>() {

        @Override
        public Object getKey(ClientDetail item) {
            return item == null ? null : item.getId();
        }
    };
    //
    //                          ***** INITIALIZATION *****
    //

    /**
     * creates WIDGET view.
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
     * Creates table with accessories - columns, pager, selection model.
     */
    private void initDataGrid() {
        GWT.log("init AdminClientsView DataGrid initialized");

        // TABLE
        dataGrid = new DataGrid<ClientDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // SELECTION MODEL
        selectionModel = new SingleSelectionModel<ClientDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel());

        // COLUMNS
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // ID
        idColumn = addColumn(new ClickableTextCell(), "ID", true, 50, new GetValue<String>() {

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
    private interface GetValue<C> {

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

    //******************* GETTER METHODS (defined by interface) ****************
    //
    //                          *** TABLE ***
    /**
     * @return TABLE (DataGrid)
     */
    @Override
    public DataGrid<ClientDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return table column: ID
     */
    @Override
    public Column<ClientDetail, String> getIdColumn() {
        return idColumn;
    }

    /**
     * @return table column: COMPANY
     */
    @Override
    public Column<ClientDetail, String> getCompanyColumn() {
        return companyColumn;
    }

    /**
     * @return table column: FIRST NAME
     */
    @Override
    public Column<ClientDetail, String> getFirstNameColumn() {
        return firstNameColumn;
    }

    /**
     * @return table column: LAST NAME
     */
    @Override
    public Column<ClientDetail, String> getLastNameColumn() {
        return lastNameColumn;
    }

    /**
     * @return table column: RATING
     */
    @Override
    public Column<ClientDetail, String> getRatingColumn() {
        return ratingColumn;
    }

    /**
     * @return table's selection model
     */
    @Override
    public SingleSelectionModel<ClientDetail> getSelectionModel() {
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
     * @return widget AdminClientDetailView as it is
     */
    @Override
    public AdminClientInfoView getAdminClientDetail() {
        return adminClientDetail;
    }

    /**
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}