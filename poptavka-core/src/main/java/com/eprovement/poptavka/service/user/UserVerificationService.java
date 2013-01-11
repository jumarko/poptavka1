/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.ExpiredActivationCodeException;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;

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
     *
     * @param user
     * @param activationCode activation code to be verified
     * @throws com.eprovement.poptavka.exception.IncorrectActivationCodeException
     *          if activationCode does not correspond to some valid (generated) activation activationCode
     * @throws com.eprovement.poptavka.exception.ExpiredActivationCodeException if activationCode already expired
     */
    void verifyActivationCode(BusinessUser user, String activationCode) throws ExpiredActivationCodeException,
            IncorrectActivationCodeException;


    /**
     * Verifies given {@code user} using {@code activationCode}, that means if correct activation code is provided,
     * all user's {@link BusinessUser#businessUserRoles}
     * verification status ( {@link com.eprovement.poptavka.domain.user.BusinessUserRole#verification} ) is set to
     * {@link com.eprovement.poptavka.domain.enums.Verification#VERIFIED}.
     *
     *
     * @param user
     * @param activationCode activation code which will be checked and corresponded user will be verified
     *
     * @see #verifyActivationCode(com.eprovement.poptavka.domain.user.BusinessUser, String)
     *      for various preconditions and exception states
     */
    void activateUser(BusinessUser user, String activationCode);

}
