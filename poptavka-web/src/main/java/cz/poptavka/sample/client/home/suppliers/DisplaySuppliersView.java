package cz.poptavka.sample.client.home.suppliers;

import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.ValueUpdater;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.domain.user.Supplier;
import java.util.Arrays;
import java.util.List;

public class DisplaySuppliersView extends OverflowComposite
        implements DisplaySuppliersPresenter.DisplaySuppliersViewInterface {

    private static DisplaySuppliersViewUiBinder uiBinder = GWT.create(DisplaySuppliersViewUiBinder.class);

    interface DisplaySuppliersViewUiBinder extends UiBinder<Widget, DisplaySuppliersView> {
    }
    private static final Logger LOGGER = Logger.getLogger("    SupplierCreationView");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    @UiField
    FlexTable table;
    @UiField(provided = true)
    CellList list;
    @UiField(provided = true)
    SimplePager pager;
    @UiField
    Label path;

    public DisplaySuppliersView() {
        initCellList();
        initWidget(uiBinder.createAndBindUi(this));
        LOGGER.info("CreateView pre DisplaySuppliers");
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public Label getPath() {
        return path;
    }

    @Override
    public FlexTable getTable() {
        return table;
    }

    @Override
    public CellList getList() {
        return list;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }
    private static final List<Supplier> SUPPLIERS = Arrays.asList(
            new Supplier(), new Supplier(), new Supplier(), new Supplier());

    private void initCellList() {
        // Use the cell in a CellList.
        list = new CellList<Supplier>(new SupplierCell());

        // Push the data into the widget.
        list.setRowData(0, SUPPLIERS);

        // Create a Pager to control the table.
        SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
        pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
        pager.setDisplay(list);
    }
}

/**
 * A custom {@link Cell} used to render a {@link Contact}. We extend
 * {@link AbstractCell} because it provides reasonable implementations of
 * methods that work for most use cases.
 */
class SupplierCell extends AbstractCell<Supplier> {

    public SupplierCell() {
        /*
         * Let the parent class know that our cell responds to click events and
         * keydown events.
         */
        super("click", "keydown");
    }

    @Override
    public void onBrowserEvent(Context context, Element parent, Supplier value,
            NativeEvent event, ValueUpdater<Supplier> valueUpdater) {
        // Check that the value is not null.
        if (value == null) {
            return;
        }

        // Call the super handler, which handlers the enter key.
        super.onBrowserEvent(context, parent, value, event, valueUpdater);

        // On click, perform the same action that we perform on enter.
        if ("click".equals(event.getType())) {
            this.onEnterKeyDown(context, parent, value, event, valueUpdater);
        }
    }

    @Override
    public void render(Context context, Supplier value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
//        if (value == null) {
//            return;
//        }

        // Display the name in big letters.
        sb.appendHtmlConstant("<div><a href=\"http://firmy.sk/66161/ivan-genda-s-car/\"> "
                + "<img style=\"float: left; margin-right: 10px;padding: 0px; width: 80px;\" "
                + "src=\"http://mattkendrick.com/wp-content/uploads/2009/07/w3schools.jpg\"  "
                + "alt=\"http://s-car.sk\"  > </a>");

        sb.appendHtmlConstant("<a href=\"http://firmy.sk/66161/ivan-genda-s-car/\"> "
                + "<strong>1.) Ivan Genda, S-Car</strong> </a>");
//        sb.appendHtmlConstant("</div>");
        sb.appendHtmlConstant("<div style=\"width:800px;\"> Výroba autoplachiet "
                + "na všetky druhy áut, markízy, stánky, prístrešky, bilboardy, "
                + "výroba REKLÁM pomocou digitálnej plnej potlače. Elektrické a "
                + "quartzové infra ohrievače.</div>");

        sb.appendHtmlConstant("<A href=\"http://www.s-car.sk\" target=\"_blank\"  "
                + " style=\"FONT-FAMILY: Arial, sans-serif; COLOR:green;\"> www.s-car.sk</A>");

        sb.appendHtmlConstant("<span style=\"FONT-FAMILY: Arial, sans-serif; COLOR:gray;\"> "
                + "&nbsp;&nbsp;-&nbsp; Minská 7/6, Martin, 036 01</span></div><br/>");
        // Display the address in normal text.
//        sb.appendHtmlConstant("<div style=\"padding-left:10px;\">");
//        sb.appendEscaped("value.address");
//        sb.appendHtmlConstant("</div>");

        // Format that birthday and display it in light gray.
//        sb.appendHtmlConstant("<div style=\"padding-left:10px;color:#aaa;\">");
//        sb.append(SafeHtmlUtils.fromTrustedString("Born: "));
//        sb.appendEscaped("dateFormat.format(value.birthday)");
//        sb.appendHtmlConstant("</div>");
    }

    /**
     * By convention, cells that respond to user events should handle the enter
     * key. This provides a consistent user experience when users use keyboard
     * navigation in the widget.
     */
    @Override
    protected void onEnterKeyDown(Context context, Element parent,
            Supplier value, NativeEvent event, ValueUpdater<Supplier> valueUpdater) {
        Window.alert("You clicked " + value.getId());
    }
}
