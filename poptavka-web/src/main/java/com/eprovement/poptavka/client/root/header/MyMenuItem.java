/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.root.header;

import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;

/**
 * Must define own MenuItem to be able to set different menu's popup position.
 *
 * @author Martin Slavkovsky
 */
public class MyMenuItem extends MenuItem {

    private static final int LEFT_OFFSET = 10;

    /**
     * Creates an instance with no text (label) and a null sub-MenuBar.
     */
    MyMenuItem() {
        super("", (MenuBar) null);
    }

    /**
     * Overridden to return user-specified X location.
     */
    @Override
    public int getAbsoluteLeft() {
        return super.getAbsoluteLeft() + super.getOffsetWidth() - super.getSubMenu().getOffsetWidth() - LEFT_OFFSET;
    }
}
