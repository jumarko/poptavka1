/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.type;

/**
 *
 * TODO - je nutne vytvarat presnu kopiu domain objektu DemandStatus pre GWT?
 * Nemozem pouzit priamo domenovy objekt?
 * @author ivan.vlcek
 */
public enum DemandStatusType {
    NEW("NEW"),
    /**
     * There are two meanings for this state. Brand new Client created Demand during registration
     * and he must confirm email activation link. The other meaning is when Demand came from external system
     * and we are waiting for approval to show Demand from non-registered Client.
     * Until we receive link confirmation/approval this Demand is in state TEMPORARY.
     */
    CRAWLED("CRAWLED"),
    /**
     * Registered/non-registered Client confirmed/approved TEMPORARY Demand.
     * Operator must check this Demand and switch it to another state.
     */
    TO_BE_CHECKED("TO_BE_CHECKED"),
    /**
     * Operator checked the Demand which needs to be changed. Either some information is missing or it is a spam.
     */
    INVALID("INVALID"),
   /**
     * Demand is properly described by Client and Operator switched it to ACTIVE state.
     */
    ACTIVE("ACTIVE"),
    /**
     * No supplier were chosen for this demand and the validity of the Demand has expired.
     * This Demand can be re-activated by Client.
     * After Client re-activates the Demand it will go to the state of TO_BE_CHECKED.
     */
    INACTIVE("INACTIVE"),
    /**
     * A Supplier is assigned to this Demand and is working on it.
     */
    ASSIGNED("ASSIGNED"),
    /**
     * A Supplier finished the realization of Demand and switched it to state FINISHED.
     */
    FINISHED("FINISHED"),
    /**
     * A Client checked FINISHED Demand and closed Demand if it was Ok.
     * Otherwise Client switches back to ASSIGNED and Supplier has to rework it.
     */
    CLOSED("CLOSED"),
    /**
     * A Client or Operator canceled Demand on which the work could being done
     * or the work has never stared for some reason.
     */
    CANCELED("CANCELED");
    private final String value;

    DemandStatusType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
