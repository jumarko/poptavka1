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
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DisplaySuppliersView extends OverflowComposite
        implements DisplaySuppliersPresenter.DisplaySuppliersViewInterface {

    private static DisplaySuppliersViewUiBinder uiBinder = GWT.create(DisplaySuppliersViewUiBinder.class);

    interface DisplaySuppliersViewUiBinder extends UiBinder<Widget, DisplaySuppliersView> {
    }
    private static final Logger LOGGER = Logger.getLogger("    SupplierCreationView");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    @UiField(provided = true)
    CellList list;
    @UiField(provided = true)
    SimplePager pager;
    @UiField
    HorizontalPanel panel;
    @UiField
    FlowPanel path;
    @UiField
    SplitLayoutPanel split;
    @UiField
    ListBox localityList;

    private final SingleSelectionModel<CategoryDetail> selectionModel = new SingleSelectionModel<CategoryDetail>();

    public DisplaySuppliersView() {
        initCellList();
        initWidget(uiBinder.createAndBindUi(this));

        LOGGER.info("CreateView pre DisplaySuppliers");
    }

    @Override
    public ListBox getLocalityList() {
        return localityList;
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

    @Override
    public FlowPanel getPath() {
        return path;
    }

    //removes last one
    @Override
    public void removePath() {
        path.remove(path.getWidgetCount() - 1);
    }

    @Override
    public void addPath(Widget widget) {
        path.add(widget);
    }

    @Override
    public CellList getList() {
        return list;
    }

    @Override
    public SimplePager getPager() {
        return pager;
    }

    @Override
    public HorizontalPanel getPanel() {
        return panel;
    }

    @Override
    public SingleSelectionModel getSelectionModel() {
        return selectionModel;
    }

    @Override
    public SplitLayoutPanel getSplitter() {
        return split;
    }
    private static final List<Supplier> SUPPLIERS = Arrays.asList(
            new Supplier(), new Supplier(), new Supplier(), new Supplier());
    private boolean root = true;

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

    @Override
    public void displaySubCategories(int columns, ArrayList<CategoryDetail> categories) {
        if (categories.isEmpty()) {
            return;
        }
        panel.clear();
        int size = categories.size();
        int subSize = 0;
        int startIdx = 0;
        if (size < columns) {
            columns = size;
        }
        while (columns != 0) {
            if (size % columns == 0) {
                subSize = size / columns;
            } else {
                subSize = size / columns + 1;
            }
            CellList cellList = null;
            if (root) {
                cellList = new CellList<CategoryDetail>(new RootCategoryCell());
            } else {
                cellList = new CellList<CategoryDetail>(new SubCategoryCell());
            }
            cellList.setRowCount(subSize, true);
            cellList.setSelectionModel(selectionModel);
            cellList.setRowData(categories.subList(startIdx, startIdx + subSize));
            panel.add(cellList);
            startIdx += subSize;
            size -= subSize;
            columns--;
        }
        root = false;
    }
}

/**
 * Supplier Cell
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

/**
 * Root Category Cell .
 */
class RootCategoryCell extends AbstractCell<CategoryDetail> {

    @Override
    public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        StringBuilder text = new StringBuilder();

        text.append(value.getName().replaceAll("-a-", " a ").replaceAll("-", ", "));
        text.append(" (");
        text.append(value.getSuppliers());
        text.append(")");

        sb.appendEscaped(text.toString());
//        sb.appendHtmlConstant("</div>");
    }
}

/**
 * Sub Category Cell .
 */
class SubCategoryCell extends AbstractCell<CategoryDetail> {

    @Override
    public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        StringBuilder text = new StringBuilder();

        text.append(value.getName().replaceAll("-a-", " a ").replaceAll("-", ", "));
        text.append(" (");
        text.append(value.getSuppliers());
        text.append(")");

        sb.appendEscaped(text.toString());
//        sb.appendHtmlConstant("</div>");
    }
}