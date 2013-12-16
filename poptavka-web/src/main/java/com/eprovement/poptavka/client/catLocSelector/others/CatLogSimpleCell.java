package com.eprovement.poptavka.client.catLocSelector.others;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * The cell used to render categories and localities.
 *
 * @author Martin
 */
public class CatLogSimpleCell extends AbstractCell<ICatLocDetail> {

    @Override
    public void render(Cell.Context context, ICatLocDetail value,
            SafeHtmlBuilder sb) {
        if (value != null) {
            sb.appendEscaped(value.toString());
        }
    }
}