package cz.poptavka.sample.client.user.demands.tab;

import java.util.Arrays;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import cz.poptavka.sample.client.user.demands.widgets.MyDemandsInfo;

public class MyDemandsView extends Composite implements
        MyDemandsPresenter.MyDemandsInterface {

    private static MyDemandsViewUiBinder uiBinder = GWT
            .create(MyDemandsViewUiBinder.class);

    interface MyDemandsViewUiBinder extends UiBinder<Widget, MyDemandsView> {
    }

    private Button answerBtn;
    private Button editBtn;
    private Button closeBtn;
    private Button cancelBtn;

    @UiField
    CellTable<MyDemandsInfo> table;

    // The list of data to display.
    private static final List<MyDemandsInfo> DEMANDSINFO = Arrays.asList(
            new MyDemandsInfo("John", "123 Fourth Road"),
            new MyDemandsInfo("Mary", "222 Lancer Lane"),
            new MyDemandsInfo("Zander", "94 Road Street"));

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        initCellTable();

    }

    private void initCellTable() {
        // Create name column.
        TextColumn<MyDemandsInfo> nameColumn = new TextColumn<MyDemandsInfo>() {
            @Override
            public String getValue(MyDemandsInfo myDemandsInfo) {
                return myDemandsInfo.getName();
            }
        };

        // Create price column.
        TextColumn<MyDemandsInfo> priceColumn = new TextColumn<MyDemandsInfo>() {
            @Override
            public String getValue(MyDemandsInfo myDemandsInfo) {
                return myDemandsInfo.getPrice();
            }
        };

        // Add the columns.
        table.addColumn(nameColumn, "Name");
        table.addColumn(priceColumn, "Price");

        // Create a data provider.
        ListDataProvider<MyDemandsInfo> dataProvider = new ListDataProvider<MyDemandsInfo>();

        // Connect the table to the data provider.
        dataProvider.addDataDisplay(table);

        // Add the data to the data provider, which automatically pushes it to
        // the
        // widget.
        List<MyDemandsInfo> list = dataProvider.getList();
        for (MyDemandsInfo demands : DEMANDSINFO) {
            list.add(demands);
        }

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
}
