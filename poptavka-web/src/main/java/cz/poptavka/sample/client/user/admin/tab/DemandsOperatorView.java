package cz.poptavka.sample.client.user.admin.tab;

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

import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;

public class DemandsOperatorView extends Composite
    implements DemandsOperatorPresenter.MyDemandsOperatorViewInterface {

    private static DemandsOperatorUiBinder uiBinder = GWT
            .create(DemandsOperatorUiBinder.class);

    interface DemandsOperatorUiBinder extends
            UiBinder<Widget, DemandsOperatorView> {
    }

    private Button answerBtn;
    private Button editBtn;
    private Button closeBtn;
    private Button cancelBtn;
    private Button refreshBtn;
    private Button activateBtn;
    private Button rejectBtn;

    @UiField(provided = true)
    CellTable<FullDemandDetail> table;

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
    private ListDataProvider<FullDemandDetail> dataProvider = new ListDataProvider<FullDemandDetail>();

    final SingleSelectionModel<FullDemandDetail> selectionModel = new SingleSelectionModel<FullDemandDetail>();


    @Override
    public void createView() {
        initCellTable(selectionModel);
        initWidget(uiBinder.createAndBindUi(this));
    }

    public CellTable<FullDemandDetail> getCellTable() {
        return table;
    }

    public SingleSelectionModel<FullDemandDetail> getSelectionModel() {
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
    public ListDataProvider<FullDemandDetail> getDataProvider() {
        return dataProvider;
    }


    private void initCellTable(
            final SingleSelectionModel<FullDemandDetail> tableSelectionModel) {
        table = new CellTable<FullDemandDetail>(2);
        table.setSelectionModel(tableSelectionModel);
        dataProvider.addDataDisplay(table);
        table.setWidth("100%", true);

        // TODO ivlcek - make it working without keyprovider
        // Attach a column sort handler to the ListDataProvider to sort the
        // list.
        ListHandler<FullDemandDetail> sortHandler = new ListHandler<FullDemandDetail>(
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
            final SelectionModel<FullDemandDetail> tableSelectionModel,
            ListHandler<FullDemandDetail> sortHandler) {

        // Create name column.
        addColumn(new TextCell(), "Title", new GetValue<String>() {
            public String getValue(FullDemandDetail fullDemandDetail) {
                return fullDemandDetail.getTitle();
            }
        });

        // DateCell.
        DateTimeFormat dateFormat = DateTimeFormat
                .getFormat(PredefinedFormat.DATE_SHORT);
        addColumn(new DateCell(dateFormat), "EndDate", new GetValue<Date>() {
            public Date getValue(FullDemandDetail fullDemandDetail) {
                return fullDemandDetail.getEndDate();
            }
        });

        // Create name column.
        addColumn(new TextCell(), "Price", new GetValue<String>() {
            public String getValue(FullDemandDetail fullDemandDetail) {
                return fullDemandDetail.getPrice().toString() + " czk";
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
        C getValue(FullDemandDetail contact);
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
    private <C> Column<FullDemandDetail, C> addColumn(Cell<C> cell,
            String headerText, final GetValue<C> getter) {
        Column<FullDemandDetail, C> column = new Column<FullDemandDetail, C>(cell) {
            @Override
            public C getValue(FullDemandDetail object) {
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
