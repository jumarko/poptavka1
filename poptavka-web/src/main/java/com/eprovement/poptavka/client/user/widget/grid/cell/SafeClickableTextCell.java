/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.widget.grid.cell;

import com.google.gwt.cell.client.ClickableTextCell;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.text.shared.SafeHtmlRenderer;

/**
 * Providing HTML safe display for clicable text cell.
 * User for EVERY text display.
 *
 * @author Martin Slavkovsky
 */
public class SafeClickableTextCell extends ClickableTextCell {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**
     * Converts string to safe html text.
     */
    public static final SafeHtmlRenderer RENDERER = new SafeHtmlRenderer<String>() {
        /**
         * @{inheritDoc}
         */
        @Override
        public SafeHtml render(String object) {
            return SafeHtmlUtils.fromTrustedString(object);
        }

        /**
         * @{inheritDoc}
         */
        @Override
        public void render(String object, SafeHtmlBuilder builder) {
            builder.appendHtmlConstant(object);
        }
    };

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates ClickableText cell.
     */
    public SafeClickableTextCell() {
        super(RENDERER);
    }
}
