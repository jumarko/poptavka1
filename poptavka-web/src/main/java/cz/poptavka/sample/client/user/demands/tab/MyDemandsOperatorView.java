package cz.poptavka.sample.client.user.demands.tab;

import java.util.Date;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.SelectionModel;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.shared.domain.DemandDetail;

public class MyDemandsOperatorView extends Composite
    implements MyDemandsOperatorPresenter.MyDemandsOperatorViewInterface {

    private static MyDemandsOperatorUiBinder uiBinder = GWT
            .create(MyDemandsOperatorUiBinder.class);

    interface MyDemandsOperatorUiBinder extends
            UiBinder<Widget, MyDemandsOperatorView> {
    }

    private Button answerBtn;
    private Button editBtn;
    private Button closeBtn;
    private Button cancelBtn;
    private Button refreshBtn;
    private Button activateBtn;
    private Button rejectBtn;

    @UiField(provided = true)
    CellTable<DemandDetail> table;

    @UiField
    SimplePanel detailSection;

    /**
     * The pager used to change the range of data. It must be created before
     * uiBinder.createAndBindUi(this)
     */
    @UiField(provided = true)
    SimplePager pager;

    /**
     * Data provider that will cell table with data.
     */
    private ListDataProvider<DemandDetail> dataProvider = new ListDataProvider<DemandDetail>();

    final SingleSelectionModel<DemandDetail> selectionModel = new SingleSelectionModel<DemandDetail>();


    @Override
    public void createView() {
        initCellTable(selectionModel);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public CellTable<DemandDetail> getCellTable() {
        return table;
    }

    public SingleSelectionModel<DemandDetail> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Button getAnswerBtn() {
        return answerBtn;
    }

    @Override
    public Button getEditBtn() {
        return editBtn;
    }

    @Override
    public Button getCloseBtn() {
        return closeBtn;
    }

    @Override
    public Button getCancelBtn() {
        return cancelBtn;
    }

    public Button getRefreshBtn() {
        return refreshBtn;
    }


    public Button getActivateBtn() {
        return activateBtn;
    }


    public Button getRejectBtn() {
        return rejectBtn;
    }

    /**
     * @return the dataProvider
     */
    public ListDataProvider<DemandDetail> getDataProvider() {
        return dataProvider;
    }


    private void initCellTable(
            final SingleSelectionModel<DemandDetail> tableSelectionModel) {
        table = new CellTable<DemandDetail>(2);
        table.setSelectionModel(tableSelectionModel);
        dataProvider.addDataDisplay(table);
        table.setWidth("100%", true);

        // TODO ivlcek - make it working without keyprovider
        // Attach a column sort handler to the ListDataProvider to sort the
        // list.
        ListHandler<DemandDetail> sortHandler = new ListHandler<DemandDetail>(
                dataProvider.getList());
        table.addColumnSortHandler(sortHandler);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT
                .create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
                true);
        pager.setDisplay(table);

        initTableColumns(getSelectionModel(), sortHandler);

    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns(
            final SelectionModel<DemandDetail> tableSelectionModel,
            ListHandler<DemandDetail> sortHandler) {

        // Create name column.
        addColumn(new TextCell(), "Title", new GetValue<String>() {
            public String getValue(DemandDetail demandDetail) {
                return demandDetail.getTitle();
            }
        });

        // DateCell.
        DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_SHORT);
        addColumn(new DateCell(dateFormat), "EndDate", new GetValue<Date>() {
            public Date getValue(DemandDetail demandDetail) {
                return demandDetail.getEndDate();
            }
        });

        // Create name column.
        addColumn(new TextCell(), "Price", new GetValue<String>() {
            public String getValue(DemandDetail demandDetail) {
                return demandDetail.getPrice().toString() + " czk";
            }
        });
    }

    /**
     * Get a cell value from a record.
     *
     * @param <C>
     *            the cell type
     */
    private static interface GetValue<C> {
        C getValue(DemandDetail contact);
    }

    /**
     * Add a column with a header.
     *
     * @param <C>
     *            the cell type
     * @param cell
     *            the cell used to render the column
     * @param headerText
     *            the header string
     * @param getter
     *            the value getter for the cell
     */
    private <C> Column<DemandDetail, C> addColumn(Cell<C> cell,
            String headerText, final GetValue<C> getter) {
        Column<DemandDetail, C> column = new Column<DemandDetail, C>(cell) {
            @Override
            public C getValue(DemandDetail object) {
                return getter.getValue(object);
            }
        };
        table.addColumn(column, headerText);
        return column;
    }

    @Override
    public SimplePanel getDetailSection() {
        return detailSection;
    }

}