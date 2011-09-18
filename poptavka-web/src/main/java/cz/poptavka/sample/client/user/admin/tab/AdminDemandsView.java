/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.AbstractEditableCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;

import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ClientDemandType;
import cz.poptavka.sample.shared.domain.type.DemandStatusType;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ivan.vlcek
 */
public class AdminDemandsView extends Composite implements AdminDemandsPresenter.AdminDemandsInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);

    @UiField
    Button commit;

    /**
     * @return the dataProvider
     */
    @Override
    public ListDataProvider<FullDemandDetail> getDataProvider() {
//        ColumnSortEvent fire = ColumnSortEvent.fire(this, dataGrid.getColumnSortList());
//        dataGrid.fireEvent(fire);
        return dataProvider;
    }

    /**
     * @return the clientIdColumn
     */
    @Override
    public Column<FullDemandDetail, String> getClientIdColumn() {
        return clientIdColumn;
    }

    /**
     * @return the demandTypeColumn
     */
    @Override
    public Column<FullDemandDetail, String> getDemandTypeColumn() {
        return demandTypeColumn;
    }

    /**
     * @return the demandTypeColumn
     */
    @Override
    public Column<FullDemandDetail, String> getDemandTitleColumn() {
        return demandTitleColumn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<FullDemandDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return the demandTypes
     */
    @Override
    public ClientDemandType[] getDemandTypes() {
        return demandTypes;
    }

    /**
     * @return the demandStatuses
     */
    @Override
    public DemandStatusType[] getDemandStatuses() {
        return demandStatuses;
    }

    /**
     * @return the demandStatusColumn
     */
    @Override
    public Column<FullDemandDetail, String> getDemandStatusColumn() {
        return demandStatusColumn;
    }

    /**
     * @return the demandExpirationColumn
     */
    @Override
    public Column<FullDemandDetail, Date> getDemandExpirationColumn() {
        return demandExpirationColumn;
    }

    /**
     * @return the demandEndColumn
     */
    @Override
    public Column<FullDemandDetail, Date> getDemandEndColumn() {
        return demandEndColumn;
    }

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<FullDemandDetail> getSelectionModel() {
        return selectionModel;
    }

    /**
     * @return the adminDemandDetail
     */
    @Override
    public SimplePanel getAdminDemandDetail() {
        return adminDemandDetail;
    }

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminDemandsView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<FullDemandDetail> dataGrid;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    @UiField(provided = true)
    ListBox pageSizeCombo;
    /**
     * The list of cells that are editable.
     */
    private List<AbstractEditableCell<?, ?>> editableCells;
    /**
     * Detail of selected Demand.
     */
    @UiField
    SimplePanel adminDemandDetail;
    /**
     * Data provider that will cell table with data.
     */
    private ListDataProvider<FullDemandDetail> dataProvider = new ListDataProvider<FullDemandDetail>();
    private SingleSelectionModel<FullDemandDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<FullDemandDetail, String> clientIdColumn;
    private Column<FullDemandDetail, String> demandTypeColumn;
    private Column<FullDemandDetail, String> demandTitleColumn;
    private Column<FullDemandDetail, String> demandStatusColumn;
    private Column<FullDemandDetail, Date> demandExpirationColumn;
    private Column<FullDemandDetail, Date> demandEndColumn;
    private final ClientDemandType[] demandTypes = ClientDemandType.values();
    private final DemandStatusType[] demandStatuses = DemandStatusType.values();

    public AdminDemandsView() {
        pageSizeCombo = new ListBox();
        pageSizeCombo.addItem("10");
        pageSizeCombo.addItem("15");
        pageSizeCombo.addItem("20");
        pageSizeCombo.addItem("25");
        pageSizeCombo.addItem("30");
        pageSizeCombo.setSelectedIndex(1);
        initDataGrid();
        initWidget(uiBinder.createAndBindUi(this));
    }

//    @Override
//    public void createView() {
//        initDataGrid();
//        initWidget(uiBinder.createAndBindUi(this));
//    }
    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<FullDemandDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
//        dataGrid.setRowCount(2, true);

        // TODO ivlcek - premysliet kedy a kde sa ma vytvarat DataProvider
        // Connect the table to the data provider.
        dataProvider.addDataDisplay(dataGrid);

        // TODO ivlcek - make it working without keyprovider
        // Attach a column sort handler to the ListDataProvider to sort the list.
        ListHandler<FullDemandDetail> sortHandler = new ListHandler<FullDemandDetail>(
                dataProvider.getList());
        dataGrid.addColumnSortHandler(sortHandler);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);
        // TODO ivlcek - nastavit pocet zaznamov v pagery na mensi pocet ako 15
//        pager.setPageSize(5);

        // Add a selection model to handle user selection.
//        final MultiSelectionModel<FullDemandDetail> selectionModel =
//        new MultiSelectionModel<FullDemandDetail>(KEY_PROVIDER);
        // Add a single selection model to handle user selection.
        selectionModel = new SingleSelectionModel<FullDemandDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<FullDemandDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns(getSelectionModel(), sortHandler);
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns(final SelectionModel<FullDemandDetail> selectionModel,
            ListHandler<FullDemandDetail> sortHandler) {

        // Checkbox column. This table will uses a checkbox column for selection.
        // Alternatively, you can call dataGrid.setSelectionEnabled(true) to enable
        // mouse selection.
        Column<FullDemandDetail, Boolean> checkColumn = new Column<FullDemandDetail, Boolean>(
                new CheckboxCell(true, false)) {

            @Override
            public Boolean getValue(FullDemandDetail object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };
        dataGrid.addColumn(checkColumn, SafeHtmlUtils.fromSafeConstant("<br/>"));
        dataGrid.setColumnWidth(checkColumn, 40, Unit.PX);

        // Demand ID.
        Column<FullDemandDetail, String> idColumn = new Column<FullDemandDetail, String>(new TextCell()) {

            @Override
            public String getValue(FullDemandDetail object) {
                return String.valueOf(object.getDemandId());
            }
        };
        idColumn.setSortable(true);
        sortHandler.setComparator(idColumn, new Comparator<FullDemandDetail>() {

            @Override
            public int compare(FullDemandDetail o1, FullDemandDetail o2) {
                return Long.valueOf(o1.getDemandId()).compareTo(Long.valueOf(o2.getDemandId()));
            }
        });
        dataGrid.addColumn(idColumn, "DID");
        dataGrid.setColumnWidth(idColumn, 50, Unit.PX);

        // Clietn ID.
        clientIdColumn = new Column<FullDemandDetail, String>(
                new EditTextCell()) {

            @Override
            public String getValue(FullDemandDetail object) {
                return String.valueOf(object.getClientId());
            }
        };
        getClientIdColumn().setSortable(true);
        sortHandler.setComparator(getClientIdColumn(), new Comparator<FullDemandDetail>() {

            @Override
            public int compare(FullDemandDetail o1, FullDemandDetail o2) {
                return Long.valueOf(o1.getClientId()).compareTo(Long.valueOf(o2.getClientId()));
            }
        });
        dataGrid.addColumn(getClientIdColumn(), "CID");
        dataGrid.setColumnWidth(getClientIdColumn(), 50, Unit.PX);

        // DemandTitle
        demandTitleColumn = new Column<FullDemandDetail, String>(
                new EditTextCell()) {

            @Override
            public String getValue(FullDemandDetail object) {
                return String.valueOf(object.getTitle());
            }
        };
        dataGrid.addColumn(demandTitleColumn, "Title");
        dataGrid.setColumnWidth(demandTitleColumn, 160, Unit.PX);

        // DemandType.
        List<String> demandTypeNames = new ArrayList<String>();
        for (ClientDemandType clientDemandType : demandTypes) {
            // TODO ivlcek - add Localizable name of ClientDemandType enum
            demandTypeNames.add(clientDemandType.getValue());
        }
        SelectionCell demandTypeCell = new SelectionCell(demandTypeNames);
        demandTypeColumn = new Column<FullDemandDetail, String>(
                demandTypeCell) {

            @Override
            public String getValue(FullDemandDetail object) {
                // TODO ivlcek - localize message
                return object.getDemandType();
            }
        };
        dataGrid.addColumn(demandTypeColumn, "Type");
        dataGrid.setColumnWidth(demandTypeColumn, 100, Unit.PX);

        // DemandStatus.
        List<String> demandStatusNames = new ArrayList<String>();
        for (DemandStatusType demandStatusType : demandStatuses) {
            // TODO ivlcek - add Localizable name of ClientDemandType enum
            demandStatusNames.add(demandStatusType.getValue());
        }
        SelectionCell demandStatusCell = new SelectionCell(demandStatusNames);
        demandStatusColumn = new Column<FullDemandDetail, String>(
                demandStatusCell) {

            @Override
            public String getValue(FullDemandDetail object) {
                // TODO ivlcek - localize message
                return object.getDemandStatus();
            }
        };
        dataGrid.addColumn(demandStatusColumn, "Status");
        dataGrid.setColumnWidth(demandStatusColumn, 140, Unit.PX);

        // Demand expiration date.
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        demandExpirationColumn = addColumn(new DatePickerCell(dateFormat), "Expiration",
                new GetValue<Date>() {

                    @Override
                    public Date getValue(FullDemandDetail fullDemandDetail) {
                        return fullDemandDetail.getValidToDate();
                    }
                }, null);

        // Demand end date.
        demandEndColumn = addColumn(new DatePickerCell(dateFormat), "End",
                new GetValue<Date>() {

                    @Override
                    public Date getValue(FullDemandDetail fullDemandDetail) {
                        return fullDemandDetail.getEndDate();
                    }
                }, null);
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(FullDemandDetail fullDemandDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<FullDemandDetail, C> addColumn(Cell<C> cell, String headerText,
            final GetValue<C> getter, FieldUpdater<FullDemandDetail, C> fieldUpdater) {
        Column<FullDemandDetail, C> column = new Column<FullDemandDetail, C>(cell) {

            @Override
            public C getValue(FullDemandDetail object) {
                return getter.getValue(object);
            }
        };
        column.setFieldUpdater(fieldUpdater);
//        if (cell instanceof AbstractEditableCell<?, ?>) {
//            editableCells.add((AbstractEditableCell<?, ?>) cell);
//        }
        dataGrid.addColumn(column, headerText);
        return column;
    }
    /**
     * The key provider that provides the unique ID of a FullDemandDetail.
     */
    private static final ProvidesKey<FullDemandDetail> KEY_PROVIDER = new ProvidesKey<FullDemandDetail>() {

        @Override
        public Object getKey(FullDemandDetail item) {
            return item == null ? null : item.getDemandId();
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
    public SimplePanel getAdminOfferDetail() {
        // TODO Auto-generated method stub
        return null;
    }
}
