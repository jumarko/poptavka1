package cz.poptavka.sample.shared.domain.demand;

public enum DetailType {

    /**
     * Editable demand detail with all fields. Used by client to edit
     * his own demands
     */
    EDITABLE,

    /**
     * Potential demand for supplier, not editable
     */
    POTENTIAL,

    /**
     * Demand offer representation
     */
    OFFER,

    /**
     * for operator, editable, extending EDITABLE
     */
    OPERATOR

}
