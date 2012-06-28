/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.exception.ExpiredActivationLinkException;
import com.eprovement.poptavka.exception.IncorrectActivationLinkException;
import com.eprovement.poptavka.exception.UserNotExistException;

public interface BusinessUserVerificationService {
    /**
     * Generates new activation link for given user.
     * Activation link is encrypted with symmetric cipher.
     * @param user
     * @return encrypted activation link
     */
    String generateActivationLink(BusinessUser user);

    /**
     * Verifies given link if it belongs to the existing user.
     * @param link link to be verified
     * @return User which has been verified
     * @throws com.eprovement.poptavka.exception.IncorrectActivationLinkException
     *          if link does not correspond to some valid (generated) activation link
     * @throws com.eprovement.poptavka.exception.ExpiredActivationLinkException if link already expired
     * @throws com.eprovement.poptavka.exception.UserNotExistException if user with email extracted from activation link
     *      does not exist in user database
     */
    BusinessUser verifyActivationLink(String link) throws UserNotExistException, ExpiredActivationLinkException,
            IncorrectActivationLinkException;


    /**
     * Verifies user using given {@code activationLink}, that means if correct activation link is provided,
     * then appropriate {@link BusinessUser} instance is loaded and for all its {@link BusinessUser#businessUserRoles}
     * verification status ( {@link com.eprovement.poptavka.domain.user.BusinessUserRole#verification} ) is set to
     * {@link com.eprovement.poptavka.domain.enums.Verification#VERIFIED}.
     *
     * @param activationLink activation link which will be checked and corresponded user will be verified
     * @return user which has already been verified
     *
     * @see #verifyActivationLink(String) for various preconditions and exception states
     */
    BusinessUser verifyUser(String activationLink);
}
