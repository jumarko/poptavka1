/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.ui;

import com.github.gwtbootstrap.client.ui.BigDecimalBox;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Modified Bootstrap's BigDecimalBox.
 * Better for validation and change monitoring.
 * Fires change events even with wrong inputs.
 * Now we can validate directly integers of detail objects. No String converstions needed.
 *
 * @author Martin Slavkovsky
 */
public class WSPriceBox extends BigDecimalBox {

    private static final String NUMBER_FORMAT_REGEX = "\\d{1,3}(,\\d{3})*";
    private static final String NUMBER_FORMAT_REGEX_FRAGMENTS = "\\d{1,3}(,\\d{3})*(\\.\\d{2})";
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private static final NumberFormat NUMBER_FORMAT = NumberFormat.getFormat(MSGS.formatNumber());

    /**
     * Get big decimal value.
     * <b>Note</b>
     * Used in getValue process in ValidationMonitor
     * @return value
     */
    @Override
    public BigDecimal getValue() {
        if (getText().isEmpty()) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(NUMBER_FORMAT.parse(getText())).setScale(0, RoundingMode.DOWN);
        }
    }

    @Override
    public void setValue(BigDecimal value) {
        if (value != null) {
            setText(NUMBER_FORMAT.format(value));
        }
    }

    /**
     * Formats & validates input.
     * <b>Note</b>
     * Used in prevalidation process in ValidationMonitor
     *
     * @return true if valid after format, false otherwise
     */
    public boolean formatAndValidate() throws NumberFormatException {
        if (getText().matches(NUMBER_FORMAT_REGEX)) {
            return true;
        }

        //If already formated, but with fraction, trow exception
        if (getText().matches(NUMBER_FORMAT_REGEX_FRAGMENTS)) {
            throw new NumberFormatException();
        } else { //If not formated, try to formate it
            //try to normalize it by removing all empty spaces and format using number formatter;
            super.setText(NUMBER_FORMAT.format(Integer.valueOf(getText().replace(" ", ""))));
        }
        return true;
    }
}
