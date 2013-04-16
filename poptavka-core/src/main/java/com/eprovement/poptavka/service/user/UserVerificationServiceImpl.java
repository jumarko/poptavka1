/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.ExpiredActivationCodeException;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.mail.MailService;
import com.eprovement.poptavka.validation.EmailValidator;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class UserVerificationServiceImpl implements UserVerificationService {

    private static final int DEFAULT_VALIDITY_LENGTH_MILLIS = 7 * 24 * 3600 * 1000;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserVerificationServiceImpl.class);
    private static final int ACTIVATION_CODE_LENGTH = 6;
    private static final Locale ENGLISH_LOCALE = new Locale("en", "EN");

    private final String activationEmailFromAddress;
    private final GeneralService generalService;
    private final MailService mailService;

    public UserVerificationServiceImpl(GeneralService generalService, MailService mailService,
                                       String activationEmailFromAddress) {
        Validate.notNull(generalService);
        Validate.notNull(mailService);
        Validate.isTrue(EmailValidator.getInstance().isValid(activationEmailFromAddress),
                "activationEmailFromAddress is not a valid email address!");

        this.generalService = generalService;
        this.mailService = mailService;
        this.activationEmailFromAddress = activationEmailFromAddress;
    }


    @Override
    public String sendNewActivationCode(User businessUser) {
        return sendNewActivationCode(businessUser, false);
    }

    @Override
    public String sendNewActivationCodeAsync(User businessUser) {
        return sendNewActivationCode(businessUser, true);
    }

    private String sendNewActivationCode(User user, boolean async) {
        Validate.notNull(user, "user cannot be null!");
        // User#activationEmail is set within scope of "generateActivationCode" method
        final String activationCode = generateActivationCode(user);
        LOGGER.info("action=send_new_activation_email email={} businuessUser={}",
                user.getEmail(), user);
        final SimpleMailMessage activationMailMessage =
                createActivationMailMessage(user.getEmail(), activationCode);
        if (async) {
            mailService.sendAsync(activationMailMessage);
        } else {
            mailService.send(activationMailMessage);
        }
        return activationCode;
    }

    @Override
    public String generateActivationCode(User user) {
        Validate.notNull(user, "User to be activated must be specified!");

        LOGGER.info("action=generate_activation_code status=start email={} businuessUser={}",
                user.getEmail(), user);

        final ActivationEmail activationEmail = new ActivationEmail();
        activationEmail.setActivationCode(RandomStringUtils.randomNumeric(ACTIVATION_CODE_LENGTH));
        final Date now = new Date();
        activationEmail.setValidTo(new Date(now.getTime() + DEFAULT_VALIDITY_LENGTH_MILLIS));
        saveActivationEmailForUser(user, activationEmail);

        LOGGER.info("action=generate_activation_code status=finish email={} validTo={} businuessUser={} ",
                user.getEmail(), activationEmail.getValidTo(), user);

        return user.getActivationEmail().getActivationCode();
    }

    @Override
    @Transactional
    public void activateUser(BusinessUser user, String activationCode) {
        Validate.notNull(user, "user to be actiavted cannot be null!");
        LOGGER.debug("action=verify_user status=start user={}", user);

        verifyActivationCode(user, activationCode);
        boolean alreadyVerified = true;
        for (BusinessUserRole role : user.getBusinessUserRoles()) {
            if (role.getVerification() != Verification.VERIFIED) {
                alreadyVerified = false;
                role.setVerification(Verification.VERIFIED);
                LOGGER.debug("action=verify_user status=set_verification_for_role role={}", role);
            }
        }

        if (alreadyVerified) {
            // al roles have already been verified - no effect
            LOGGER.debug("action=verify_user status=finish_already_verified user={}", user);
            return;
        }

        generalService.merge(user);
        LOGGER.debug("action=verify_user status=finish_verified user={}", user);
    }

    @Override
    @Transactional(readOnly = true)
    public void verifyActivationCode(BusinessUser user, String activationCode) throws ExpiredActivationCodeException,
            IncorrectActivationCodeException {
        Validate.notNull(user, "user to be activated cannot be null");
        Validate.notEmpty(activationCode, "Activation code to be verified must not be null!");

        LOGGER.debug("action=verify_activation_code status=start");

        if (user.getActivationEmail() == null) {
            throw new IncorrectActivationCodeException("No activation email has been set for user. Activation code "
                    + activationCode + " might have been generated by malicious user!");
        }

        if (activationExpired(user.getActivationEmail())) {
            throw new ExpiredActivationCodeException("Activation code expired. "
                    + "New activation code for user must be generated.");
        }

        if (!activationCode.equals(user.getActivationEmail().getActivationCode())) {
            throw new IncorrectActivationCodeException("Received activation code is different from the one"
                    + " assigned to the user. Either activation link has not been generated by poptavka"
                    + " or another (newer one) activation link for given user has been generated.");
        }
        LOGGER.debug("action=verify_activation_code status=finish user=" + user);
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private boolean activationExpired(ActivationEmail activationEmail) {
        final Date now = new Date();
        return now.after(activationEmail.getValidTo());
    }

    private void saveActivationEmailForUser(User user, ActivationEmail activationEmail) {
        user.setActivationEmail(activationEmail);
        generalService.save(user);
    }

    private SimpleMailMessage createActivationMailMessage(String userMail, String activationCode) {
        final SimpleMailMessage activationMessage = new SimpleMailMessage();

        ResourceBundle rb = ResourceBundle.getBundle("localization", ENGLISH_LOCALE);
        String activationEmailText = rb.getString("uc10.mail.sentence1");

        activationMessage.setFrom(activationEmailFromAddress);
        activationMessage.setTo(userMail);

        activationMessage.setSubject("Poptavka account activation");

        activationMessage.setText(activationEmailText + " \n" + activationCode);
        return activationMessage;

    }
}
