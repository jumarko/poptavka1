/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.ui;

import com.github.gwtbootstrap.client.ui.BigDecimalBox;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import java.math.BigDecimal;
import java.text.ParseException;

/**
 * Modified Bootstrap's BigDecimalBox.
 * Better for validation and change monitoring.
 * Fires change events even with wrong inputs.
 * Now we can validate directly integers of detail objects. No String converstions needed.
 *
 * @author Martin Slavkovsky
 */
public class WSBigDecimalBox extends BigDecimalBox {

    /*
     * @{inheritDoc}
     * Sets Big decimal value.
     */
    @Override
    public void setValue(BigDecimal value, boolean fireEvents) {
        super.setText(value.toString().replace(".00", ""));
        BigDecimal oldValue = null;
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

    /**
     * Set big decimal value
     * @param value to be set, if null, empty string is set
     */
    @Override
    public void setValue(BigDecimal value) {
        if (value == null) {
            setText("");
        } else {
            super.setValue(value);
        }
    }
}