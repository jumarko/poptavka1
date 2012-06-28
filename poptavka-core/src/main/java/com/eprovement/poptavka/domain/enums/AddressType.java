package com.eprovement.poptavka.domain.enums;

/**
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
public enum AddressType {

    /**
     * Address came from external demand system that was used to populate table with demands.
     * In this case the field Address.street contains street, houseNumber, city, zipCode in one String.
     */
    FOREIGN,
    /** Address came from our external company catalog that was used to populate table with suppliers. */
    CATALOG,
    /** Address came from our own system after official registration. */
    OFFICIAL

}
