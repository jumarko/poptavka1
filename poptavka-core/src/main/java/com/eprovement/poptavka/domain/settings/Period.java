package com.eprovement.poptavka.domain.settings;

/**
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
public enum Period {

    INSTANTLY("INSTANTLY"),
    DAILY("DAILY"),
    BY3DAYS("3DAYS"),
    BY5DAYS("5DAYS"),
    BY7DAYS("7DAYS");

    private final String value;

    Period(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
