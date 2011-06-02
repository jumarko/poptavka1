/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain;

import java.io.Serializable;

/**
 *  TODO prepisat tak aby sa pouzivali kody a nie priamo IDcka
 *
 * @author ivan.vlcek
 */
public enum OfferStateDetail implements Serializable {

    ACCEPTED(Integer.valueOf(1)),
    PENDING(Integer.valueOf(2)),
    DECLINED(Integer.valueOf(3));
    private final Integer value;
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -102932467233112389L;

    OfferStateDetail(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }
}
