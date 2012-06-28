/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.domain.enums;

/**
 * Enum for handy work with demand type.
 *
 * @see com.eprovement.poptavka.domain.demand.DemandType#getType()
 */
public enum DemandTypeType {

    NORMAL("normal"),

    /**
     * Special demand at top positions.
     */
    ATTRACTIVE("attractive");

    private final String value;

    DemandTypeType(String value) {
        this.value = value;
    }

    /**
     * Get enum constant for given type code
     *
     * @param
     * @return
     */
    public static DemandTypeType fromValue(String typeCode) {
        for (DemandTypeType type : DemandTypeType.values()) {
            if (type.value.equals(typeCode)) {
                return type;
            }
        }
        throw new IllegalArgumentException("No DemandType enum constant for type code [" + typeCode + "].");
    }

    public String getValue() {
        return value;
    }

}
