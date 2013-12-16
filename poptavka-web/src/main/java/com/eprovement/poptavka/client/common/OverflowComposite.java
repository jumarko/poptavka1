/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common;

import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

/**
 * Providing just overflow ability.
 *
 * @author Beho
 *
 */
public abstract class OverflowComposite extends Composite {

    /**
     * Sets overflow to given widget's parent.
     * @param widget
     * @param value - the Overflow enum
     */
    protected void setParentOverflow(Widget widget, Overflow value) {
        widget.getElement().getParentElement().getStyle().setOverflow(value);
    }

}
