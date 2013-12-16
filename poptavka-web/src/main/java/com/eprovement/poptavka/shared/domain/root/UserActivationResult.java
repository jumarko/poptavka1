/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.root;

/**
 * The result of user activation/verification process.
 * Corresponds directly to the signature of method
 * {@link com.eprovement.poptavka.service.user.UserVerificationService#activateUser(
 * com.eprovement.poptavka.domain.user.BusinessUser, String)}.
 *
 */
public enum UserActivationResult {
    /** User has been verified sucessfuly - passed activation is correct */
    OK,
    /** Passed activation code contains user's email that does not exist in current database. */
    ERROR_UNKNOWN_USER,
    /** Passed activation code expired and user must ask for a new one. */
    ERROR_EXPIRED_ACTIVATION_CODE,
    /** User enters incorrect activation code. */
    ERROR_INCORRECT_ACTIVATION_CODE
}
