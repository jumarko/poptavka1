/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
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
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ClientDemandType;
import cz.poptavka.sample.shared.domain.type.DemandStatusType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *
 * @author ivan.vlcek, edited by Martin Slavkovsky
 */
public class AdminDemandsView extends Composite implements AdminDemandsPresenter.AdminDemandsInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminDemandsView> {
    }
    //
    //                          ***** ATTRIBUTES *****
    //
    @UiField Button commit, rollback, refresh;
    @UiField Label changesLabel;
    // PAGER
    @UiField(provided = true) SimplePager pager;
    @UiField(provided = true) ListBox pageSizeCombo;
    // DETAIL
    @UiField SimplePanel adminDemandDetail;
    // TABLE
    @UiField(provided = true) DataGrid<FullDemandDetail> dataGrid;
    private SingleSelectionModel<FullDemandDetail> selectionModel;
    // Editable Columns in dataGrid
    private Column<FullDemandDetail, String> idColumn;
    private Column<FullDemandDetail, String> cidColumn;
    private Column<FullDemandDetail, String> demandTypeColumn;
    private Column<FullDemandDetail, String> demandTitleColumn;
    private Column<FullDemandDetail, String> statusColumn;
    private Column<FullDemandDetail, Date> demandExpirationColumn;
    private Column<FullDemandDetail, Date> demandEndColumn;
    // The key provider that provides the unique ID of a FullDemandDetail.
    private static final ProvidesKey<FullDemandDetail> KEY_PROVIDER = new ProvidesKey<FullDemandDetail>() {

        @Override
        public Object getKey(FullDemandDetail item) {
            return item == null ? null : item.getDemandId();
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
        GWT.log("init AdminDemands DataGrid initialized");

        // TABLE
        dataGrid = new DataGrid<FullDemandDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("100%");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // PAGER
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // SELECTION MODEL
        selectionModel = new SingleSelectionModel<FullDemandDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<FullDemandDetail>createCheckboxManager());

        // COLUMNS
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // Demand ID.
        idColumn = addColumn(new ClickableTextCell(), "ID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail object) {
                return String.valueOf(object.getDemandId());
            }
        });

        // Client ID.
        cidColumn = addColumn(new ClickableTextCell(), "CID", true, 50, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail object) {
                return String.valueOf(object.getClientId());
            }
        });

        // DemandTitle
        demandTitleColumn = addColumn(new EditTextCell(), "Title", true, 160, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail object) {
                return String.valueOf(object.getTitle());
            }
        });

        // DemandType.
        List<String> demandTypeNames = new ArrayList<String>();
        for (ClientDemandType clientDemandType : ClientDemandType.values()) {
            // TODO ivlcek - add Localizable name of ClientDemandType enum
            demandTypeNames.add(clientDemandType.getValue());
        }
        demandTypeColumn = addColumn(new SelectionCell(demandTypeNames), "Type", true, 100, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail object) {
                return object.getDemandType();
            }
        });

        // DemandStatus.
        List<String> demandStatusNames = new ArrayList<String>();
        for (DemandStatusType demandStatusType : DemandStatusType.values()) {
            demandStatusNames.add(demandStatusType.getValue());
        }
        statusColumn = addColumn(new SelectionCell(demandStatusNames), "Status", true, 140, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail object) {
                return object.getDemandStatus();
            }
        });

        // Demand expiration date.
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        demandExpirationColumn = addColumn(new DatePickerCell(dateFormat), "Expiration", true, 40,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(FullDemandDetail fullDemandDetail) {
                        return fullDemandDetail.getValidToDate();
                    }
                });

        // Demand end date.
        demandEndColumn = addColumn(new DatePickerCell(dateFormat), "End", true, 40,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(FullDemandDetail fullDemandDetail) {
                        return fullDemandDetail.getEndDate();
                    }
                });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private interface GetValue<C> {

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
    private <C> Column<FullDemandDetail, C> addColumn(Cell<C> cell, String headerText, boolean sort, int width,
            final GetValue<C> getter) {
        Column<FullDemandDetail, C> column = new Column<FullDemandDetail, C>(cell) {

            @Override
            public C getValue(FullDemandDetail object) {
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
    public DataGrid<FullDemandDetail> getDataGrid() {
        return dataGrid;
    }

    /**
     * @return table column: ID
     */
    @Override
    public Column<FullDemandDetail, String> getIdColumn() {
        return idColumn;
    }

    /**
     * @return table column: CID
     */
    @Override
    public Column<FullDemandDetail, String> getCidColumn() {
        return cidColumn;
    }

    /**
     * @return table column: TYPE
     */
    @Override
    public Column<FullDemandDetail, String> getDemandTypeColumn() {
        return demandTypeColumn;
    }

    /**
     * @return table column: TITLE
     */
    @Override
    public Column<FullDemandDetail, String> getDemandTitleColumn() {
        return demandTitleColumn;
    }

    /**
     * @return table column: STATUS
     */
    @Override
    public Column<FullDemandDetail, String> getDemandStatusColumn() {
        return statusColumn;
    }

    /**
     * @return table column: EXPIRATION
     */
    @Override
    public Column<FullDemandDetail, Date> getDemandExpirationColumn() {
        return demandExpirationColumn;
    }

    /**
     * @return table column: END DATE
     */
    @Override
    public Column<FullDemandDetail, Date> getDemandEndColumn() {
        return demandEndColumn;
    }

    /**
     * @return table's selection model
     */
    @Override
    public SingleSelectionModel<FullDemandDetail> getSelectionModel() {
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
     * @return table/pager size: value
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
     * @return widget AdminDemandInfoView as it is
     */
    @Override
    public SimplePanel getAdminDemandDetail() {
        return adminDemandDetail;
    }

    /**
     * @return this widget as it is
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}