/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.cell.client.CheckboxCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;

/**
 * Cell for rendering radio button.
 * @author Martin Slavkovsky
 */
public class RadioCell extends CheckboxCell {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**
     * An html string representation of a checked input box.
     */
    private static final SafeHtml INPUT_SELECTED = SafeHtmlUtils.fromSafeConstant(
            "<input type=\"radio\" name=\"selected\" tabindex=\"-1\" checked/>");
    private static final SafeHtml INPUT_UNCHECKED = SafeHtmlUtils.fromSafeConstant(
            "<input type=\"radio\" name=\"selected\" tabindex=\"-1\"/>");

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    /**
     * @{inheritDoc}
     */
    @Override
    public void render(Context context, Boolean value, SafeHtmlBuilder sb) {
        if (value != null && value) {
            sb.append(INPUT_SELECTED);
        } else {
            sb.append(INPUT_UNCHECKED);
        }
    }
}
