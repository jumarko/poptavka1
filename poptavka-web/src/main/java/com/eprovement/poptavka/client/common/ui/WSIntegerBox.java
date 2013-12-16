/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.ui;

import com.github.gwtbootstrap.client.ui.IntegerBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import java.text.ParseException;

/**
 * IntegerBox created according to GWT TextBox to be used with integers. Better for validation.
 * Now we can validate directly integers in detail objects. No converstion/duplicity to String needed.
 *
 * @author Martin Slavkovsky
 */
public class WSIntegerBox extends IntegerBox {

    /*
     * @{inheritDoc}
     * Sets Integer value.
     */
    @Override
    public void setValue(Integer value, boolean fireEvents) {
        super.setText(value.toString());
        Integer oldValue = null;
        try {
            oldValue = getValueOrThrow();
        } catch (ParseException ex) {
            if (fireEvents) {
                ValueChangeEvent.fire(this, value);
            }
        }
        if (fireEvents) {
            ValueChangeEvent.fireIfNotEqual(this, oldValue, value);
        }
    }

}