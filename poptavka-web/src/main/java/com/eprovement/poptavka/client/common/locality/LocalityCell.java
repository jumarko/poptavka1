/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.common.locality;

import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.google.gwt.cell.client.AbstractCell;
import com.google.gwt.cell.client.Cell;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;

/**
 *
 * @author Martin
 */
public class LocalityCell extends AbstractCell<LocalityDetail> {

    /**
     * The html of the image used for contacts.
     */
    public LocalityCell() {
    }

    @Override
    public void render(Cell.Context context, LocalityDetail value, SafeHtmlBuilder sb) {
        if (value != null) {
            sb.appendEscaped(value.getName());
        }
    }
}