/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.user.admin.tab;

import com.google.gwt.user.client.ui.SimplePanel;
import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.EditTextCell;
import com.google.gwt.cell.client.SelectionCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.ProvidesKey;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.type.OfferStateType;

/**
 *
 * @author ivan.vlcek
 */
public class AdminOffersView extends Composite implements
        AdminOffersPresenter.AdminOffersInterface {

    private static AdministrationViewUiBinder uiBinder =
            GWT.create(AdministrationViewUiBinder.class);
    @UiField(provided = true)
    ListBox pageSizeCombo;
    @UiField
    Button commit, rollback, refresh;
    @UiField
    Label changesLabel;

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public SimplePanel getAdminSupplierDetail() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    interface AdministrationViewUiBinder extends UiBinder<Widget, AdminOffersView> {
    }
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    DataGrid<OfferDetail> dataGrid;
    /**
     * The pager used to change the range of data. It must be created before uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;
    /**
     * Data provider that will cell table with data.
     */
    private SingleSelectionModel<OfferDetail> selectionModel;
    /** Editable Columns in CellTable. **/
    private Column<OfferDetail, String> priceColumn;
    private Column<OfferDetail, String> offerStatusColumn;
    private Column<OfferDetail, Date> offerCreationDateColumn;
    private Column<OfferDetail, Date> offerFinishDateColumn;

//    @Override
//    public void createView() {
    public AdminOffersView() {
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

    @Override
    public Column<OfferDetail, String> getPriceColumn() {
        return priceColumn;
    }

    @Override
    public Column<OfferDetail, String> getOfferStatusColumn() {
        return offerStatusColumn;
    }

    @Override
    public Column<OfferDetail, Date> getOfferCreationDateColumn() {
        return offerCreationDateColumn;
    }

    @Override
    public Column<OfferDetail, Date> getOfferFinishDateColumn() {
        return offerFinishDateColumn;
    }

    private void initDataGrid() {
        // Create a dataGrid.
        GWT.log("Admin Offers initDataGrid initialized");
        // Set a key provider that provides a unique key for each contact. If key is
        // used to identify contacts when fields (such as the name and address)
        // change.
        dataGrid = new DataGrid<OfferDetail>(KEY_PROVIDER);
        dataGrid.setPageSize(this.getPageSize());
        dataGrid.setWidth("700px");
        dataGrid.setHeight("500px");
        dataGrid.setEmptyTableWidget(new Label("No data available."));

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(dataGrid);

        // Add a selection model to handle user selection.
        selectionModel = new SingleSelectionModel<OfferDetail>(KEY_PROVIDER);
        dataGrid.setSelectionModel(getSelectionModel(),
                DefaultSelectionEventManager.<OfferDetail>createCheckboxManager());

        // Initialize the columns.
        initGridColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initGridColumns() {
        // Offer ID
        addColumn(new TextCell(), "OID", 40, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return Long.toString(offerDetail.getId());
            }
        });
        // Demand ID.
        addColumn(new TextCell(), "DID", 40, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return Long.toString(offerDetail.getDemandId());
            }
        });
        // Supplier ID
        addColumn(new TextCell(), "SID", 40, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return Long.toString(offerDetail.getSupplierId());
            }
        });
        priceColumn = addColumn(new EditTextCell(), "Price", 40, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return offerDetail.getPrice().toString();
            }
        });

        // OfferStatus.
        ArrayList<String> stateList = new ArrayList<String>();
        for (OfferStateType offerStatusDetail : OfferStateType.values()) {
            stateList.add(offerStatusDetail.getValue());
        }
        offerStatusColumn = addColumn(new SelectionCell(stateList), "State", 60, new GetValue<String>() {

            @Override
            public String getValue(OfferDetail offerDetail) {
                return offerDetail.getState();
            }
        });

        // Creation date
        offerCreationDateColumn = addColumn(new DateCell(), "Created", 60, new GetValue<Date>() {

            @Override
            public Date getValue(OfferDetail offerDetail) {
                return offerDetail.getFinishDate(); //TODO Martin - dorobit ceration date
            }
        });

        // Demand end date.
        offerFinishDateColumn = addColumn(new DateCell(), "Finnish", 60, new GetValue<Date>() {

            @Override
            public Date getValue(OfferDetail offerDetail) {
                return offerDetail.getFinishDate();
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C> the cell type
     */
    private static interface GetValue<C> {

        C getValue(OfferDetail offerDetail);
    }

    /**
     * Add a column with a header.
     *
     * @param <C> the cell type
     * @param cell the cell used to render the column
     * @param headerText the header string
     * @param getter the value getter for the cell
     */
    private <C> Column<OfferDetail, C> addColumn(Cell<C> cell, String headerText, int width,
            final GetValue<C> getter) {
        Column<OfferDetail, C> column = new Column<OfferDetail, C>(cell) {

            @Override
            public C getValue(OfferDetail object) {
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
     * The key provider that provides the unique ID of a DemandDetail.
     */
    private static final ProvidesKey<OfferDetail> KEY_PROVIDER = new ProvidesKey<OfferDetail>() {

        @Override
        public Object getKey(OfferDetail item) {
            return item == null ? null : item.getId();
        }
    };

    @Override
    public DataGrid<OfferDetail> getDataGrid() {
        return dataGrid;
    }

    @Override
    public ListBox getPageSizeCombo() {
        return pageSizeCombo;
    }

    @Override
    public SimplePager getPager() {
        return pager;
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

    /**
     * @return the selectionModel
     */
    @Override
    public SingleSelectionModel<OfferDetail> getSelectionModel() {
        return selectionModel;
    }
}
