package cz.poptavka.sample.client.user.demands.tab;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.HasKeyboardSelectionPolicy.KeyboardSelectionPolicy;
import com.google.gwt.user.cellview.client.TextColumn;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;

import cz.poptavka.sample.shared.domain.DemandDetail;

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
    CellTable<DemandDetail> table;

    @UiField
    SimplePanel myDemandDetail;

//     The list of data to display.
    private static List<DemandDetail> demandsinfo = generateDemandDetail();

    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));

        initCellTable();

    }

    private void initCellTable() {
        // Create name column.
        TextColumn<DemandDetail> nameColumn = new TextColumn<DemandDetail>() {
            @Override
            public String getValue(DemandDetail myDemandsInfo) {
                return myDemandsInfo.getTitle();
            }
        };

        // Create price column.
        TextColumn<DemandDetail> priceColumn = new TextColumn<DemandDetail>() {
            @Override
            public String getValue(DemandDetail myDemandsInfo) {
                return myDemandsInfo.getPrice().toString();
            }
        };

        // Add the columns.
        table.addColumn(nameColumn, "Name");
        table.addColumn(priceColumn, "Price");

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

    public CellTable<DemandDetail> getCellTable() {
        return table;
    }

    public void setMyDemandDetail(String name) {
        Label header = new Label(name);

        myDemandDetail.setWidget(header);
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
    private static List<DemandDetail> generateDemandDetail() {
        DemandDetail detail1 = new DemandDetail();
        detail1.setId(1);
        detail1.setTitle("blala1");
        detail1.setPrice(new BigDecimal(2000));
        DemandDetail detail2 = new DemandDetail();
        detail2.setId(2);
        detail2.setTitle("blala2");
        detail2.setPrice(new BigDecimal(21000));
        DemandDetail detail3 = new DemandDetail();
        detail3.setId(3);
        detail3.setTitle("blala3");
        detail3.setPrice(new BigDecimal(1500));
        demandsinfo = new ArrayList<DemandDetail>();
        demandsinfo.add(detail1);
        demandsinfo.add(detail2);
        demandsinfo.add(detail3);
        return demandsinfo;
    }
}
