package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * The cell used to render categories.
 *
 * @author Martin
 */
public class CategoryCell extends AbstractCell<CategoryDetail> {

    //DisplayCountOfWhat
    public static final int DISPLAY_COUNT_OF_DEMANDS = 0;
    public static final int DISPLAY_COUNT_OF_SUPPLIERS = 1;
    private int displayCountOfWhat = -1;

    /**
     * The html of the image used for contacts.
     */
    public CategoryCell(int displayCountOfWhat) {
        this.displayCountOfWhat = displayCountOfWhat;
    }

    @Override
    public void render(Cell.Context context, CategoryDetail value, SafeHtmlBuilder sb) {
        if (value != null) {
            StringBuilder text = new StringBuilder();

            text.append(value.getName().replaceAll("-a-", " a ").replaceAll("-", ", "));
            text.append(" (");
            switch (displayCountOfWhat) {
                case DISPLAY_COUNT_OF_DEMANDS:
                    text.append(value.getDemands());
                    break;
                case DISPLAY_COUNT_OF_SUPPLIERS:
                    text.append(value.getSuppliers());
                    break;
                default:
                    break;
            }
            text.append(")");

            sb.appendEscaped(text.toString());
        }
    }
}
