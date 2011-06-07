/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.type;

import java.io.Serializable;

/**
 *  TODO prepisat tak aby sa pouzivali kody a nie priamo IDcka.
 *
 * @author ivan.vlcek
 */
public enum OfferStateType implements Serializable {

    ACCEPTED("ACCEPTED"),
    PENDING("PENDING"),
    DECLINED("DECLINED");
    private final String value;
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -102932467233112389L;

    OfferStateType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
