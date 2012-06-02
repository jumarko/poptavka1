package com.eprovement.poptavka.domain.user;

/**
 * @author Juraj Martinka
 *         Date: 8.5.11
 */
public enum Verification {

    /** User has already been verified by email link activation. */
    VERIFIED,

    /**
     * User has filled out our registration form and received email link activation.
     * If user was in state EXTERNAL before registration we will change it to UNVERIFIED
     * and we will be waiting for email link activation.
     */
    UNVERIFIED,

    /** User came from external system and will be verified after email link activation is performed. */
    EXTERNAL

}
