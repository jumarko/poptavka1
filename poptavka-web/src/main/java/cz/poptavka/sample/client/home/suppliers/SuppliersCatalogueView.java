package cz.poptavka.sample.client.home.suppliers;

import com.google.gwt.cell.client.AbstractCell;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

import cz.poptavka.sample.client.main.common.OverflowComposite;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SuppliersCatalogueView extends OverflowComposite
        implements SuppliersCataloguePresenter.SuppliersCatalogueViewInterface {

    private static SuppliersCatalogueViewUiBinder uiBinder = GWT.create(SuppliersCatalogueViewUiBinder.class);

    interface SuppliersCatalogueViewUiBinder extends UiBinder<Widget, SuppliersCatalogueView> {
    }

    private static final Logger LOGGER = Logger.getLogger("    SupplierCreationView");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

//    @UiField
//    FlexTable table;
    @UiField
    HorizontalPanel panel;

    private final SingleSelectionModel<CategoryDetail> selectionModel = new SingleSelectionModel<CategoryDetail>();

    public SuppliersCatalogueView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }

//    @Override
//    public FlexTable getTable() {
//        return table;
//    }
    @Override
    public HorizontalPanel getPanel() {
        return panel;
    }

    @Override
    public SingleSelectionModel getSelectionModel() {
        return selectionModel;
    }

    private static final List<Supplier> SUPPLIERS = Arrays.asList(
            new Supplier(), new Supplier(), new Supplier(), new Supplier());

    //TODO Martin - zistit ci je toto ok, alebo to radsej urobit pomocou cez horizontal
    //panel, cellListami a abstractCell, ktora bude zobrazovat Logo (ak bude), a label
    //Teraz je to cez FlexTable, kde cell je SimplePanel, ktory obsahuje Logo(ak je) a Label
    //Hlavna otazka je, co so stylom, potom co jednoduchsie a rychlejsie
//    @Override
//    public void setFlexTable(int columns, ArrayList<CategoryDetail> categories) {
//        table.clear();
//        int row = 0;
//        int col = 0;
//        for (CategoryDetail category : categories) {
//            if (category == null) {
//                continue;
//            }
//            if (col == columns) {
//                col = 0;
//                row++;
//            }
//            HorizontalPanel cellPanel = new HorizontalPanel();
//            cellPanel.add(new Image(StyleResource.INSTANCE.images().normal()));
//            Label label = new Label(category.getName());
////            label.setStylePrimaryName(DEBUG_ID_PREFIX);
//            cellPanel.add(label);
//            table.setWidget(row, col++, cellPanel);
//        }
//    }

    @Override
    public void createCatalogue(int col, ArrayList<CategoryDetail> categories) {
        int size = categories.size();
        int subSize = 0;
        int startIdx = 0;
        while (col != 0) {
            if (size % col == 0) {
                subSize = size / col;
            } else {
                subSize = size / col + 1;
            }
            CellList list = new CellList<CategoryDetail>(new CategoryCell());
            list.setRowCount(subSize, true);
            list.setSelectionModel(selectionModel);
            list.setRowData(categories.subList(startIdx, startIdx + subSize));
            panel.add(list);
            startIdx += subSize;
            size -= subSize;
            col--;
        }
    }
}


/**
 * A custom {@link Cell} used to render a {@link Contact}. We extend
 * {@link AbstractCell} because it provides reasonable implementations of
 * methods that work for most use cases.
 */
class CategoryCell extends AbstractCell<CategoryDetail> {

    @Override
    public void render(Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
//        if (value == null) {
//            return;
//        }

        // Display the name in big letters.
//        sb.appendHtmlConstant("<div><a href=\"http://firmy.sk/66161/ivan-genda-s-car/\"> "
//                + "<img style=\"float: left; margin-right: 10px;padding: 0px; width: 80px;\" "
//                + "src=\"http://mattkendrick.com/wp-content/uploads/2009/07/w3schools.jpg\"  "
//                + "alt=\"http://s-car.sk\"  > </a>");
//
//        sb.appendHtmlConstant("<a href=\"http://firmy.sk/66161/ivan-genda-s-car/\"> "
//                + "<strong>1.) Ivan Genda, S-Car</strong> </a>");
//        sb.appendHtmlConstant("</div>");
//        sb.appendHtmlConstant("<div style=\"width:800px;\"> Výroba autoplachiet "
//                + "na všetky druhy áut, markízy, stánky, prístrešky, bilboardy, "
//                + "výroba REKLÁM pomocou digitálnej plnej potlače. Elektrické a "
//                + "quartzové infra ohrievače.</div>");

//        sb.appendHtmlConstant("<A href=\"http://www.s-car.sk\" target=\"_blank\"  "
//                + " style=\"FONT-FAMILY: Arial, sans-serif; COLOR:green;\"> www.s-car.sk</A>");

//        sb.appendHtmlConstant("<span style=\"FONT-FAMILY: Arial, sans-serif; COLOR:gray;\"> "
//                + "&nbsp;&nbsp;-&nbsp; Minská 7/6, Martin, 036 01</span></div><br/>");
        // Display the address in normal text.
//        sb.appendHtmlConstant("<div style=\"padding-left:10px;\">");
        sb.appendEscaped(value.getName());
//        sb.appendHtmlConstant("</div>");

        // Format that birthday and display it in light gray.
//        sb.appendHtmlConstant("<div style=\"padding-left:10px;color:#aaa;\">");
//        sb.append(SafeHtmlUtils.fromTrustedString("Born: "));
//        sb.appendEscaped("dateFormat.format(value.birthday)");
//        sb.appendHtmlConstant("</div>");
    }
}