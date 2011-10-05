/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DatePickerCell;
import com.google.gwt.cell.client.EditTextCell;
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
 * @author Martin Slavkovsky
 */
public class AdminOurPaymentDetailsView extends Composite
    implements AdminOurPaymentDetailsPresenter.AdminOurPaymentDetailsInterface {

    private static AdminDemandsViewUiBinder uiBinder = GWT.create(AdminDemandsViewUiBinder.class);
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

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

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public DataGrid<FullDemandDetail> getDataGrid() {
        return dataGrid;
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

    interface AdminDemandsViewUiBinder extends UiBinder<Widget, AdminOurPaymentDetailsView> {
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
     * Detail of selected Demand.
     */
    @UiField
    SimplePanel adminDemandDetail;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<FullDemandDetail> selectionModel;
    /** Editable Columns in dataGrid. **/
    private Column<FullDemandDetail, String> demandTypeColumn;
    private Column<FullDemandDetail, String> demandTitleColumn;
    private Column<FullDemandDetail, String> demandStatusColumn;
    private Column<FullDemandDetail, Date> demandExpirationColumn;
    private Column<FullDemandDetail, Date> demandEndColumn;

    public AdminOurPaymentDetailsView() {
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
        dataGrid = new DataGrid<FullDemandDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        selectionModel = new SingleSelectionModel<FullDemandDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<FullDemandDetail>createCheckboxManager());

        // Initialize the columns.
        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {

        // Checkbox column. This table will uses a checkbox column for selection.
        // Alternatively, you can call dataGrid.setSelectionEnabled(true) to enable
        // mouse selection.
        addColumn(new CheckboxCell(true, false), "<br/>", 40, new GetValue<Boolean>() {

            @Override
            public Boolean getValue(FullDemandDetail object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        });

        // Demand ID.
        addColumn(new TextCell(), "DID", 50, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail object) {
                return String.valueOf(object.getDemandId());
            }
        });

        // Clietn ID.
        addColumn(new TextCell(), "CID", 50, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail object) {
                return String.valueOf(object.getClientId());
            }
        });

        // DemandTitle
        demandTitleColumn = addColumn(new EditTextCell(), "Title", 160, new GetValue<String>() {

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
        demandTypeColumn = addColumn(new SelectionCell(demandTypeNames), "Type", 100, new GetValue<String>() {

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
        demandStatusColumn = addColumn(new SelectionCell(demandStatusNames), "Status", 140, new GetValue<String>() {

            @Override
            public String getValue(FullDemandDetail object) {
                return object.getDemandStatus();
            }
        });

        // Demand expiration date.
        DateTimeFormat dateFormat = DateTimeFormat.getFormat(PredefinedFormat.DATE_MEDIUM);
        demandExpirationColumn = addColumn(new DatePickerCell(dateFormat), "Expiration", 40,
                new GetValue<Date>() {

                    @Override
                    public Date getValue(FullDemandDetail fullDemandDetail) {
                        return fullDemandDetail.getValidToDate();
                    }
                });

        // Demand end date.
        demandEndColumn = addColumn(new DatePickerCell(dateFormat), "End", 40,
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
    private <C> Column<FullDemandDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<FullDemandDetail, C> column = new Column<FullDemandDetail, C>(cell) {

            @Override
            public C getValue(FullDemandDetail object) {
                return getter.getValue(object);
            }
        };
        if (headerText.endsWith("<br/>")) {
            dataGrid.addColumn(column, SafeHtmlUtils.fromSafeConstant("<br/>"));
        } else {
            column.setSortable(true);
            dataGrid.addColumn(column, headerText);
        }
        dataGrid.setColumnWidth(column, width, Unit.PX);
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