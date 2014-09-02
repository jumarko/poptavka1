/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.shared.domain.supplier.LesserSupplierDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 * Cell for rendering supplier information.
 * @author Martin Slavkovsky
 */
public class SupplierCell extends AbstractCell<LesserSupplierDetail> {

    /**
     * @{inheritDoc}
     */
    @Override
    public void render(Cell.Context context, LesserSupplierDetail value, SafeHtmlBuilder sb) {
        /*
         * Always do a null check on the value. Cell widgets can pass null to
         * cells if the underlying data contains a null, or if the data arrives
         * out of order.
         */
        if (value == null) {
            return;
        }

        // Display the name in big letters.
        sb.appendHtmlConstant("<div style=\"size:200%;font-weight:bold;\">");
        sb.appendEscaped(value.getUserData().getDisplayName());
        sb.appendHtmlConstant("</div>");

        // Display the address in normal text.
        sb.appendHtmlConstant("<div style=\"padding-left:10px;overflow:hidden;text-overflow: ellipsis;white-space: "
                + "nowrap;\">");
        String categories = value.getCategories().toString();
        if (!categories.isEmpty()) {
            sb.appendEscaped(categories.substring(1, categories.length() - 1));
        }
        sb.appendHtmlConstant("</div>");
    }
}
