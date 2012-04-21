/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.user;

import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.exception.IncorrectActivationLinkException;

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
     * @throws cz.poptavka.sample.exception.IncorrectActivationLinkException if link does not correspond to some valid
     *      (generated) activation link
     */
    BusinessUser verifyActivationLink(String link) throws IncorrectActivationLinkException;
}
