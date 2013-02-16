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
    public static final int DISPLAY_COUNT_DISABLED = -1;
    //Display string Constants
    public static final String LEFT_BRACE = "<span style=\"float: right;\">";
    public static final String RIGHT_BRACE = "</span>";
    public static final String SPACE = " ";
    //
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
            sb.appendEscaped(text.toString());
            switch (displayCountOfWhat) {
            case DISPLAY_COUNT_OF_DEMANDS:
                text.append(SPACE);
                sb.appendHtmlConstant(LEFT_BRACE);
                sb.append(value.getDemandsCount());
                sb.appendHtmlConstant(RIGHT_BRACE);
                break;
            case DISPLAY_COUNT_OF_SUPPLIERS:
                text.append(SPACE);
                sb.appendHtmlConstant(LEFT_BRACE);
                sb.append(value.getSuppliersCount());
                sb.appendHtmlConstant(RIGHT_BRACE);
                break;
            default:
                break;
            }

        }
    }
}
