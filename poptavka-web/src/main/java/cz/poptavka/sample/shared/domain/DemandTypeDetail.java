/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain;

/**
 * DemandTypeDetail is used only to list all DemandType enum values.
 * TODO ivlcek - maybe we can use the domain object DemandType and remove this
 * shared DemandTypeDetail object.
 *
 * @author ivan.vlcek
 */
public enum DemandTypeDetail {
    NORMAL("normal"),
    /** Special demand at top positions. */
    ATTRACTIVE("attractive");
    private final String value;

    DemandTypeDetail(String value) {
        this.value = value;
    }
}
