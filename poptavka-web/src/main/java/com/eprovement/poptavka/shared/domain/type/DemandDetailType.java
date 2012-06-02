package com.eprovement.poptavka.shared.domain.type;

/**
 * DemandDetailType serves to recognize which particular DemandDetail implementation
 * is used.
 *
 * @author Beho
 */
public enum DemandDetailType {
    /** simple for basic view, for suppliers. **/
    BASE("base"),
    /** detail with all demand attributes for client and operator/admin. */
    FULL("full");
    private final String value;

    DemandDetailType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
