package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.core.client.GWT;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiRenderer;

public class RootCategoryCell extends AbstractCell<ICatLocDetail> {

    /**************************************************************************/
    /* UiRenderer                                                             */
    /**************************************************************************/
    private static RootCategoryCell.MyUiRenderer renderer = GWT.create(RootCategoryCell.MyUiRenderer.class);

    interface MyUiRenderer extends UiRenderer {

        void render(SafeHtmlBuilder sb, String catName, String catCount);
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    @Override
    public void render(Cell.Context context, ICatLocDetail value, SafeHtmlBuilder sb) {
        if (value != null) {
            renderer.render(sb,
                    value.toString(),
                    String.valueOf(value.getDemandsCount()));
        }
    }
}