package cz.poptavka.sample.client.user.problems;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.cell.client.DateCell;
import com.google.gwt.cell.client.TextCell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionModel;

import cz.poptavka.sample.client.common.messages.MessageView;
import cz.poptavka.sample.domain.mail.Message;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;


public class MyProblemsView extends Composite implements
        MyProblemsPresenter.MyProblemsViewInterface {

    private static MyProblemsUiBinder uiBinder = GWT
            .create(MyProblemsUiBinder.class);

    private static final Logger LOGGER = Logger.getLogger(MyProblemsView.class.getName());

    @UiField
    CellTable<ContactInfo> cellTable;

    @UiField
    VerticalPanel messagesPanel;

    @UiField
    Button respond;
    @UiField
    Button edit;
    @UiField
    Button close;
    @UiField
    Button cancel;
    @UiField
    Button activate;
    @UiField
    Button deny;

    @Override
    public void createView() {
        //initWidget(uiBinder.createAndBindUi(this));
        // Add a selection model so we can select cells.
        SelectionModel<ContactInfo> selectionModel = new MultiSelectionModel<ContactInfo>();
        cellTable.setSelectionModel(selectionModel);
        this.initTableColumns(selectionModel);
        this.setData();
    }

    interface MyProblemsUiBinder extends UiBinder<Widget, MyProblemsView> {
    }

    public MyProblemsView() {
        initWidget(uiBinder.createAndBindUi(this));

        // Add a selection model so we can select cells.
        SelectionModel<ContactInfo> selectionModel = new MultiSelectionModel<ContactInfo>();
        cellTable.setSelectionModel(selectionModel);

        this.initTableColumns(selectionModel);
        this.setData();
    }

    public MyProblemsView(String firstName) {
        initWidget(uiBinder.createAndBindUi(this));
    }

    private void setData() {
        List<ContactInfo> contacts = Arrays.asList(
                new ContactInfo("demand 1", "ok", new Date(), "123 000"),
                new ContactInfo("Joe", "ok", new Date(), "10 000"),
                new ContactInfo("George", "not ok", new Date(), "1600"));

        // Set the total row count. This isn't strictly necessary, but it
        // affects paging calculations, so its good habit to keep the row count
        // up to
        // date.
        cellTable.setRowCount(contacts.size(), true);

        // Push the data into the widget.
        cellTable.setRowData(0, contacts);
    }

    /**
     * Add the columns to the table.
     */
    private void initTableColumns(
            final SelectionModel<ContactInfo> selectionModel) {

        // ******************* CHECK BOX *************************************
        Column<ContactInfo, Boolean> checkColumn = new Column<ContactInfo, Boolean>(
                new CheckboxCell()) {
            @Override
            public Boolean getValue(ContactInfo object) {
                // Get the value from the selection model.
                return selectionModel.isSelected(object);
            }
        };

        cellTable.addColumn(checkColumn, "TODO");

        // ******************* DEMAND NAME *************************************
        Column<ContactInfo, String> demandName = new Column<ContactInfo, String>(
                new TextCell()) {
            @Override
            public String getValue(ContactInfo object) {
                return object.getDemandName();
            }
        };

        cellTable.addColumn(demandName, "Demand name");

        // ******************* STATE *************************************
        Column<ContactInfo, String> state = new Column<ContactInfo, String>(
                new TextCell()) {
            @Override
            public String getValue(ContactInfo object) {
                return object.getState();
            }
        };

        cellTable.addColumn(state, "State");

        // ****************** DATE *******************************
        Column<ContactInfo, Date> date = new Column<ContactInfo, Date>(
                new DateCell()) {
            @Override
            public Date getValue(ContactInfo object) {
                return object.getDate();
            }
        };
        cellTable.addColumn(date, "Date");

        // ****************** CENA *******************************
        Column<ContactInfo, String> price = new Column<ContactInfo, String>(
                new TextCell()) {
            @Override
            public String getValue(ContactInfo object) {
                return object.getPrice();
            }
        };

        cellTable.addColumn(price, "Price");
    }

    @Override
    public void displayMessages(List<Message> messages) {
        int i = 0;
        MessageView message;
        for (Message m : messages) {
            message = new MessageView(m);
            if (i == messages.size() - 1) {
                message.getPanelBody().setOpen(true);
            }
            LOGGER.info("Adding to Panel");
            messagesPanel.add(message);
            i++;
        }
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}

/**
 * Nothing serious. Just experimenting :).
 *
 * @author Martin Slavkovsky
 *
 */
class SelectorCell extends AbstractCell<String> {

    // @Override
    public void render(String value, Object key, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        final String[] categories = {"Vsetky", "Ziaden", "Precitane",
                                     "Neprecitane", "Nove", "Aktivovane", "Neschvalene",
                                     "S dodavatelom", "Bez dodavatela", "Realizuju sa",
                                     "Zrealizovane", "Uzavrete", "Neuzavrete", "Ohodnotene",
                                     "Neohodnotene" };

        sb.appendHtmlConstant("<select>");
        for (String string : categories) {
            sb.appendHtmlConstant("<option>");
            sb.appendEscaped(string);
            sb.appendHtmlConstant("</option>");
        }
        sb.appendHtmlConstant("</select>");
    }

    @Override
    public void render(com.google.gwt.cell.client.Cell.Context arg0,
            String arg1, SafeHtmlBuilder arg2) {
        // TODO Auto-generated method stub

    }
}

/**
 * Private class for Fake Data.
 *
 * @author Martin Slavkovsky
 *
 */
class ContactInfo {
    private String demandName;
    private String state;
    private Date date;
    private String price;

    public ContactInfo(String demandName, String state, Date date, String price) {
        this.demandName = demandName;
        this.state = state;
        this.date = date;
        this.price = price;
    }

    public String getDemandName() {
        return demandName;
    }

    public String getState() {
        return state;
    }

    public Date getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }
}
