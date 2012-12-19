/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.ExpiredActivationCodeException;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;
import com.eprovement.poptavka.exception.UserNotExistException;

public interface UserVerificationService {

    /**
     * Generates new activation code for given {@code businessUser} and send it to his mail in a synchronous fashion.
     * New activation code is persisted.
     *
     * @return generated activation code
     * @see #generateActivationCode(com.eprovement.poptavka.domain.user.User)
     */
    String sendNewActivationCode(User user);

    /**
     * The same as {@link #sendNewActivationCode(com.eprovement.poptavka.domain.user.User)} but sends
     * activation email in an <sťrong>asynchronous</sťrong> way.
     */
    String sendNewActivationCodeAsync(User user);

    /**
     * Generates new activation code for given user.
     * Activation dode is encrypted with symmetric cipher.
     * @param user
     * @return encrypted activation code
     */
    String generateActivationCode(User user);

    /**
     * Verifies given activation code if it belongs to the existing user.
     * @param activationCode activation code to be verified
     * @return User which has been verified
     * @throws com.eprovement.poptavka.exception.IncorrectActivationCodeException
     *          if activationCode does not correspond to some valid (generated) activation activationCode
     * @throws com.eprovement.poptavka.exception.ExpiredActivationCodeException if activationCode already expired
     * @throws com.eprovement.poptavka.exception.UserNotExistException if user with email extracted from activation
     *      activationCode
     *      does not exist in user database
     */
    BusinessUser verifyActivationCode(String activationCode) throws UserNotExistException,
            ExpiredActivationCodeException, IncorrectActivationCodeException;


    /**
     * Verifies user using given {@code activationCode}, that means if correct activation code is provided,
     * then appropriate {@link BusinessUser} instance is loaded and for all its {@link BusinessUser#businessUserRoles}
     * verification status ( {@link com.eprovement.poptavka.domain.user.BusinessUserRole#verification} ) is set to
     * {@link com.eprovement.poptavka.domain.enums.Verification#VERIFIED}.
     *
     * @param activationCode activation code which will be checked and corresponded user will be verified
     * @return user which has already been verified
     *
     * @see #verifyActivationCode(String) for various preconditions and exception states
     */
    BusinessUser activateUser(String activationCode);

}
