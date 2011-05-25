package cz.poptavka.sample.client.user.demands.tab;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
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

    public MyDemandsOperatorView() {
        initWidget(uiBinder.createAndBindUi(this));
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

    // The list of data to display.
    private static List<DemandDetail> demandsinfo = generateDemandDetail();

    final SingleSelectionModel<DemandDetail> selectionModel = new SingleSelectionModel<DemandDetail>();


    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        initCellTable(selectionModel);

    }

    public CellTable<DemandDetail> getCellTable() {
        return table;
    }

    public void setMyDemandOperatorDetail(String name) {
        Label header = new Label(name);

        myDemandOperatorDetail.setWidget(header);
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


    private void initCellTable(
            final SingleSelectionModel<DemandDetail> selectionModel) {

        table.setSelectionModel(selectionModel);
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

        // // Add the columns.
        // table.addColumn(nameColumn, "Name");
        // // TODO jaro pouzili sme addColumn metodu
        // // table.addColumn(dateColumn, "Date");
        // table.addColumn(priceColumn, "Price");

        // Create a data provider.
        ListDataProvider<DemandDetail> dataProvider = new ListDataProvider<DemandDetail>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(table);

        // Add the data to the data provider, which automatically pushes it to
        // the
        // widget.
        List<DemandDetail> list = dataProvider.getList();
        for (DemandDetail demands : demandsinfo) {
            list.add(demands);
        }

        table.setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

    }

    private static List<DemandDetail> generateDemandDetail() {
        DemandDetail detail1 = new DemandDetail();
        detail1.setId(1);
        detail1.setTitle("blala1 blala1 blala1 blala1 blala1 blala1");
        Date date1 = new Date(2010, 10, 2);
        detail1.setEndDate(date1);
        detail1.setPrice(new BigDecimal(2000));
        DemandDetail detail2 = new DemandDetail();
        detail2.setId(2);
        detail2.setTitle("blala2");
        Date date2 = new Date(2010, 10, 12);
        detail2.setEndDate(date2);
        detail2.setPrice(new BigDecimal(21000));
        DemandDetail detail3 = new DemandDetail();
        detail3.setId(3);
        detail3.setTitle("blala3");
        Date date3 = new Date(2010, 10, 22);
        detail3.setEndDate(date3);
        detail3.setPrice(new BigDecimal(1500));
        demandsinfo = new ArrayList<DemandDetail>();
        demandsinfo.add(detail1);
        demandsinfo.add(detail2);
        demandsinfo.add(detail3);
        return demandsinfo;
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

}
