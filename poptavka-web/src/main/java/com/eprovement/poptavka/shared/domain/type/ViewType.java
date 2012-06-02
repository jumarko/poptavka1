package com.eprovement.poptavka.shared.domain.type;

/**
 * defines view, where the method call should be executed or handled.
 * @author Beho
 *
 */
public enum ViewType {

    /**
     * Editable demand detail with all fields. Used by client to edit
     * his own demands.
     */
    EDITABLE("editable"),

    /**
     * Potential demand for supplier, not editable.
     */
    POTENTIAL("potential"),

    /**
     * Demand offer representation.
     */
    OFFER("offer"),

    /**
     * for operator, editable, extending EDITABLE.
     */
    OPERATOR("operator"),

    /**
     * for admin, editable, extending OPERATOR.
     */
    ADMIN("admin");

    private final String value;
    ViewType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
