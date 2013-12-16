package com.eprovement.poptavka.domain.enums;

/**
 * Demand (project) life-cycle status (state)
 *
 * @author Juraj Martinka
 *         Date: 31.1.11
 */
public enum DemandStatus {


    /**
     * New Demand that has just been created by a Client. It to be validated by an Operator, who will change its
     * DemandStatus to ACTIVE or, INVALID.
     */
    NEW("NEW"),

    /**
     * There are two meanings for this state:
     * - Demand create by a client who has not verified their e-mail address yet.
     * - Demand imported from an external system is waiting for approval
     */
    CRAWLED("CRAWLED"),

    /**
     * Registered/non-registered Client confirmed/approved TEMPORARY Demand.
     * Operator must check this Demand and switch it to another state.
     *
     */
    TO_BE_CHECKED("TO_BE_CHECKED"),

    /**
     * Operator checked the Demand which needs to be changed. Either some information is missing or it is a spam.
     *
     * Stav 2
     */
    INVALID("INVALID"),

    /**
     * Demand is properly described by Client and Operator switched it to ACTIVE state.
     *
     * Stav 3
     */
    ACTIVE("ACTIVE"),

    /**
     * No supplier were chosen for this demand and the validity of the Demand has expired.
     * This Demand can be re-activated by Client.
     * After Client re-activates the Demand it will go to the state of TO_BE_CHECKED.
     *
     * Stav
     */
    INACTIVE("INACTIVE"),

    /**
     * This status means that at least one offer was submited for this particular demand
     */
    OFFERED("OFFERED"),

    /**
     * A Supplier is assigned to this Demand and is working on it.
     */
    ASSIGNED("ASSIGNED"),

    /**
     * A Supplier finished the realization of Demand and is waiting for Client's acceptation.
     */
    PENDINGCOMPLETION("PENDINGCOMPLETION"),

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

    DemandStatus(String value) {
        this.value = value;
    }


    public String getValue() {
        return value;
    }
}
