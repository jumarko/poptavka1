/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.type;

/**
 * ClientDemandType is used only to list all DemandType enum values.
 * TODO LATER ivlcek - maybe we can use the domain object DemandType and remove this
 * shared ClientDemandType object.
 *
 * @author ivan.vlcek
 */
public enum ClientDemandType {
    NORMAL("normal"),
    /** Special demand at top positions. */
    ATTRACTIVE("attractive");
    private final String value;

    ClientDemandType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
