/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.ui.RadioButton;

/**
 *
 * @author mato
 */
public class RadioCell extends CheckboxCell {

    /** 
     * An html string representation of a checked input box. 
     */
    private static final SafeHtml INPUT_SELECTED = SafeHtmlUtils.fromSafeConstant(
            "<input type=\"radio\" name=\"selected\" tabindex=\"-1\" checked/>");
    private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant(
            "<input type=\"radio\" name=\"selected\" tabindex=\"-1\"/>");

    @Override
    public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
        // Get the view data.  
        Object key = context.getKey();
        Boolean viewData = getViewData(key);


        if (value != null && value) {
            RadioButton l = new RadioButton("sa");
            sb.append(INPUT_SELECTED);
        } else {
            sb.append(INPUT_UNCHECKED);
        }
    }
}
