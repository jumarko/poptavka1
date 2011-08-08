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
import cz.poptavka.sample.shared.domain.CategoryDetail;
import java.util.ArrayList;

public class RootView extends OverflowComposite
        implements RootPresenter.RootViewInterface {

    private static RootViewUiBinder uiBinder = GWT.create(RootViewUiBinder.class);

    interface RootViewUiBinder extends UiBinder<Widget, RootView> {
    }
    private static final Logger LOGGER = Logger.getLogger("    SupplierCreationView");
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    @UiField
    HorizontalPanel panel;
    private final SingleSelectionModel<CategoryDetail> selectionModel = new SingleSelectionModel<CategoryDetail>();

    public RootView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    @Override
    public Widget getWidgetView() {
        return this;
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
    public void displayRootCategories(int columns, ArrayList<CategoryDetail> rootCategories) {
        if (rootCategories.isEmpty()) {
            panel.clear();
            return;
        }
        panel.clear();
        int size = rootCategories.size();
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
            cellList = new CellList<CategoryDetail>(new RootCategoryCell());
            cellList.setRowCount(subSize, true);
            cellList.setSelectionModel(selectionModel);
            cellList.setRowData(rootCategories.subList(startIdx, startIdx + subSize));
            panel.add(cellList);
            startIdx += subSize;
            size -= subSize;
            columns--;
        }
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