/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 *
 * @author Martin
 */
public class SupplierCell extends AbstractCell<FullSupplierDetail> {

    @Override
    public void render(Cell.Context context, FullSupplierDetail value, SafeHtmlBuilder sb) {
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
        sb.appendEscaped(value.getUserData().getCompanyName());
        sb.appendHtmlConstant("</div>");

        // Display the address in normal text.
        sb.appendHtmlConstant("<div style=\"padding-left:10px;\">");
        String categories = value.getCategories().toString();
        if (!categories.isEmpty()) {
            sb.appendEscaped(categories.substring(1, categories.length() - 1));
        }
        sb.appendHtmlConstant("</div>");
    }
}
