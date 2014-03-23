package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.ExpiredActivationCodeException;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;

public interface UserVerificationService {

    /**
     * Generates new activation code for given {@code user} and send it to his mail in a synchronous fashion.
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
     * @param user user for which the activation code should be generated
     * @return encrypted activation code
     */
    String generateActivationCode(User user);

    /**
     * Verifies given activation code if it belongs to the existing user.
     *
     * @param user user to be verified with given activation code
     * @param activationCode activation code to be verified
     * @throws com.eprovement.poptavka.exception.IncorrectActivationCodeException
     *          if activationCode does not correspond to some valid (generated) activation activationCode
     * @throws com.eprovement.poptavka.exception.ExpiredActivationCodeException if activationCode already expired
     */
    void verifyActivationCode(User user, String activationCode) throws ExpiredActivationCodeException,
            IncorrectActivationCodeException;


    /**
     * Activates given {@code user} using {@code activationCode}, that means if correct activation code is provided
     * verification status {@link User#verification} is set to
     * {@link com.eprovement.poptavka.domain.enums.Verification#VERIFIED}.
     *
     * @param user user to be activated
     * @param activationCode activation code which will be checked and corresponded user will be verified
     *
     * @see #verifyActivationCode(com.eprovement.poptavka.domain.user.User, String)
     *      for various preconditions and exception states
     */
    void activateUser(User user, String activationCode);

    /**
     * Resets password for user.
     * New random password is saved into database.
     *
     * <p>
     * Note: that this method returns plaintext form of new generated password.
     * You can retrieve the hashed value via {@code user.getPassword()}.
     * </p>
     *
     * @param user user whose password will be reset
     * @return new random password in plaintext
     */
    String resetPassword(User user);

}
