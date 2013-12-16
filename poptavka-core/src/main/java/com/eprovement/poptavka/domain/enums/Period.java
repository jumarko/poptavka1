package com.eprovement.poptavka.domain.enums;

/**
 * Periods for notifications
 *
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
public enum Period {
    /** Immediately. */
    INSTANTLY("INSTANTLY"),
    /** Once a day, usually early in the morning. */
    DAILY("DAILY"),
    /** Once a week, usually on Monday. */
    WEEKLY("WEEKLY");

    private final String value;

    Period(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
