package com.eprovement.poptavka.domain.enums;
/**
 * States of offer life-cycle
 *
 */
public enum OfferStateType {

    ACCEPTED("ACCEPTED"),
    /** Special demand at top positions. */
    PENDING("PENDING"),
    DECLINED("DECLINED"),
    /** Offer was delivered and Client should accept and close the demand. **/
    COMPLETED("COMPLETED"),
    /** Completed offer was accepted by Client and demands was closed. **/
    CLOSED("CLOSED");

    private final String value;

    OfferStateType(String value) {
        this.value = value;
    }

    /**
     * Get enum constant for a given type code
     *
     * @param typeCode String representation of the enum value
     * @return the enum value
     */
    public static OfferStateType fromValue(String typeCode) {
        for (OfferStateType type : OfferStateType.values()) {
            if (type.value.equals(typeCode)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No OfferState enum constant for type code [" + typeCode + "].");
    }

    public String getValue() {
        return value;
    }

}
