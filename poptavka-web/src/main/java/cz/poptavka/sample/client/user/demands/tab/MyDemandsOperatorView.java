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
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
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

    @UiField
    CellTable<DemandDetail> table;

    @UiField
    SimplePanel myDemandOperatorDetail;

    /**
     * Data provider that will cell table with data.
     */
    private ListDataProvider<DemandDetail> dataProvider = new ListDataProvider<DemandDetail>();

    final SingleSelectionModel<DemandDetail> selectionModel = new SingleSelectionModel<DemandDetail>();


    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        initCellTable(selectionModel);

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
            final SingleSelectionModel<DemandDetail> selectionModel) {
        table = new CellTable<DemandDetail>(15);
        table.setSelectionModel(selectionModel);
        dataProvider.addDataDisplay(table);
        // // Checkbox column. This table will uses a checkbox column for
        // // selection.
        // // Alternatively, you can call cellTable.setSelectionEnabled(true) to
        // // enable
        // // mouse selection.
        // Column<DemandDetail, Boolean> checkColumn = new Column<DemandDetail,
        // Boolean>(
        // new CheckboxCell(true, false)) {
        // @Override
        // public Boolean getValue(DemandDetail object) {
        // // Get the value from the selection model.
        // return selectionModel.isSelected(object);
        // }
        // };
        // table.addColumn(checkColumn,
        // SafeHtmlUtils.fromSafeConstant("<br/>"));
        table.setWidth("100%");

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
                return demandDetail.getPrice().toString() + " CZK";
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
        return myDemandOperatorDetail;
    }

}
