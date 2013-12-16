/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    public static final SafeHtmlRenderer RENDERER = new SafeHtmlRenderer<String>() {
        @Override
        public SafeHtml render(String object) {
            return SafeHtmlUtils.fromTrustedString(object);
        }

        @Override
        public void render(String object, SafeHtmlBuilder builder) {
            builder.appendHtmlConstant(object);
        }
    };

    public SafeClickableTextCell() {
        super(RENDERER);
    }
}
