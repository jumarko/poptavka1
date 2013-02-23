package com.eprovement.poptavka.client.user.problems;

import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.google.gwt.view.client.SingleSelectionModel;

import java.util.Date;
import java.util.List;

public class MyProblemsView extends Composite implements
        MyProblemsPresenter.MyProblemsViewInterface {

    private static MyProblemsUiBinder uiBinder = GWT.create(MyProblemsUiBinder.class);

    private final SingleSelectionModel<Problem> selectionModel = new SingleSelectionModel<Problem>();

    @UiField(provided = true)
    CellTable<Problem> cellTable;

    @UiField(provided = true)
    SimplePager pager;

    @UiField
    Button replyBtn, editBtn, closeBtn, cancelBtn, refuseBtn;

    @UiField
    SimplePanel detailSection;

    private LocalizableMessages messages = GWT.create(LocalizableMessages.class);

    private NumberFormat currencyFormat = NumberFormat.getFormat(messages.formatCurrency());

    private AsyncDataProvider dataProvider = new AsyncDataProvider() {

        @Override
        protected void onRangeChanged(HasData display) {
            //implemented in presenter
        }
    };

    interface MyProblemsUiBinder extends UiBinder<Widget, MyProblemsView> {
    }

    public MyProblemsView() {
        this.initCellTable();

        initWidget(uiBinder.createAndBindUi(this));
    }

    public MyProblemsView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public void displayProblems(List<Problem> problems) {
        // Set the total row count. This isn't strictly necessary, but it
        // affects paging calculations, so its good habit to keep the row count
        // up to
        // date.
        cellTable.setRowCount(problems.size(), true);

        // Push the data into the widget.
        cellTable.setRowData(0, problems);
    }

    @Override
    public CellTable<Problem> getCellTable() {
        return cellTable;
    }

    private void initCellTable() {
        cellTable = new CellTable<Problem>(2);
        cellTable.setSelectionModel(selectionModel);
        dataProvider.addDataDisplay(cellTable);
        cellTable.setWidth("100%", true);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
                true);
        pager.setDisplay(cellTable);

        initTableColumns();
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns() {
        // ****************** SELECTION MODEL *******************************
        // cellTable.setSelectionModel(selectionModel);

        // ******************* CHECK BOX *************************************
//        Column<Problem, Boolean> checkColumn = new Column<Problem, Boolean>(
//                new CheckboxCell()) {
//            @Override
//            public Boolean getValue(Problem object) {
//                // Get the value from the selection model.
//                return selectionModel.isSelected(object);
//            }
//        };

//        cellTable.addColumn(checkColumn, "   ");

        // ******************* DEMAND NAME *************************************
        Column<Problem, String> demandName = new Column<Problem, String>(
                new TextCell()) {

            @Override
            public String getValue(Problem object) {
                return object.getDemandName();
            }
        };

        cellTable.addColumn(demandName, "Demand title");

        // ******************* STATE *************************************
        Column<Problem, String> state = new Column<Problem, String>(
                new TextCell()) {

            @Override
            public String getValue(Problem object) {
                return object.getState();
            }
        };

        cellTable.addColumn(state, "State");

        // ****************** DATE *******************************
        Column<Problem, Date> date = new Column<Problem, Date>(new DateCell()) {

            @Override
            public Date getValue(Problem object) {
                return object.getDate();
            }
        };
        cellTable.addColumn(date, "Date");

        // ****************** CENA *******************************
        Column<Problem, String> price = new Column<Problem, String>(
                new TextCell()) {

            @Override
            public String getValue(Problem object) {
                return currencyFormat.format(Double.valueOf(object.getPrice()));
            }
        };

        cellTable.addColumn(price, "Price");
    }

    @Override
    public SingleSelectionModel<Problem> getSelectionModel() {
        return selectionModel;
    }

    @Override
    public Button getReplyBtn() {
        return replyBtn;
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

    @Override
    public Button getRefuseBtn() {
        return refuseBtn;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public SimplePanel getDetailSection() {
        return detailSection;
    }
}
