/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.application.security.SymmetricKeyEncryptor;
import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.exception.ExpiredActivationCodeException;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;
import com.eprovement.poptavka.exception.UserNotExistException;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.mail.MailService;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.annotate.JsonCreator;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.ObjectMapper;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

public class BusinessUserVerificationServiceImpl implements BusinessUserVerificationService {

    private static final int DEFAULT_VALIDITY_LENGTH_MILLIS = 7 * 24 * 3600 * 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessUserVerificationServiceImpl.class);

    private final SymmetricKeyEncryptor symmetricEncryptor;
    private final GeneralService generalService;
    private MailService mailService;

    private final ObjectMapper jsonMapper = new ObjectMapper();


    public BusinessUserVerificationServiceImpl(SymmetricKeyEncryptor symmetricEncryptor,
            GeneralService generalService) {
        Validate.notNull(symmetricEncryptor);
        Validate.notNull(generalService);

        this.symmetricEncryptor = symmetricEncryptor;
        this.generalService = generalService;
    }

    public void setMailService(MailService mailService) {
        this.mailService = mailService;
    }

    @Override
    public String sendNewActivationCode(BusinessUser businessUser) {
        return sendNewActivationCode(businessUser, false);
    }


    @Override
    public String sendNewActivationCodeAsync(BusinessUser businessUser) {
        return sendNewActivationCode(businessUser, true);
    }

    private String sendNewActivationCode(BusinessUser businessUser, boolean async) {
        Validate.notNull(businessUser, "businessUser cannot be null!");
        // User#activationEmail is set within scope of "generateActivationCode" method
        final String activationCode = generateActivationCode(businessUser);
        if (mailService != null) {
            LOGGER.info("action=send_new_activation_email email={} businuessUser={}",
                    businessUser.getEmail(), businessUser);
            final SimpleMailMessage activationMailMessage =
                    createActivationMailMessage(businessUser.getEmail(), activationCode);
            if (async) {
                mailService.sendAsync(activationMailMessage);
            } else {
                mailService.send(activationMailMessage);
            }
        }
        return activationCode;
    }


    @Override
    public String generateActivationCode(BusinessUser businessUser) {
        Validate.notNull(businessUser, "User to be activated must be specified!");

        LOGGER.info("action=generate_activation_code status=start email={} businuessUser={}",
                businessUser.getEmail(), businessUser);

        final Date now = new Date();
        final Date activationValidTo = new Date(now.getTime() + DEFAULT_VALIDITY_LENGTH_MILLIS);
        final ActivationCode activationCode = new ActivationCode(businessUser.getEmail(), activationValidTo.getTime());

        // URL encoding must be performed because encrypted link can contain '/' character
        final String encryptedLink = symmetricEncryptor.encrypt(serializeActivationCode(activationCode));

        // emailActivation property can be overwritten multiple times.
        setActivationEmailForUser(businessUser, activationCode, encryptedLink);

        LOGGER.info("action=generate_activation_code status=finish email={} validTo={} businuessUser={} ",
                new Object[] {businessUser.getEmail(), activationValidTo, businessUser});

        return businessUser.getActivationEmail().getActivationCode();
    }


    @Override
    @Transactional
    public BusinessUser activateUser(String activationCode) {
        LOGGER.debug("action=verify_user status=start");

        final BusinessUser businessUser = verifyActivationCode(activationCode);
        boolean alreadyVerified = true;
        for (BusinessUserRole role: businessUser.getBusinessUserRoles()) {
            if (role.getVerification() != Verification.VERIFIED) {
                alreadyVerified = false;
                role.setVerification(Verification.VERIFIED);
                LOGGER.debug("action=verify_user status=set_verification_for_role role={}", role);
            }
        }

        if (alreadyVerified) {
            // al roles have already been verified - no effect
            LOGGER.debug("action=verify_user status=finish_already_verified businessUser={}", businessUser);
            return businessUser;
        }

        generalService.save(businessUser);
        LOGGER.debug("action=verify_user status=finish_verified businessUser={}", businessUser);
        return businessUser;
    }

    @Override
    @Transactional(readOnly = true)
    public BusinessUser verifyActivationCode(String activationCode) throws UserNotExistException,
            ExpiredActivationCodeException, IncorrectActivationCodeException {
        Validate.notEmpty(activationCode, "Activation code to be verified must not be null!");

        LOGGER.debug("action=verify_activation_code status=start");

        final ActivationCode decryptedActivationCode;
        try {
            decryptedActivationCode = deserializeActivationCode(symmetricEncryptor.decrypt(activationCode));
        } catch (EncryptionOperationNotPossibleException eonpe) {
            throw new IncorrectActivationCodeException("Passed activation code=" +  activationCode
                    + " is in illegal format. Probably, it has been encrypted incorrectly.");
        }

        if (activationExpired(decryptedActivationCode)) {
            throw new ExpiredActivationCodeException("Activation code expired. "
                    + "New activation code for user must be generated.");
        }

        final BusinessUser userToBeActivated = getUserByEmail(decryptedActivationCode.getUserEmail());
        if (userToBeActivated == null) {
            throw new UserNotExistException("Relevant user does not exist - activation code is broken!");
        }
        if (userToBeActivated.getActivationEmail() == null) {
            throw new IncorrectActivationCodeException("No activation email has been set for user. Activation code "
                    + decryptedActivationCode + " might have been generated by malicious user!");
        }
        // activation link is stored in an encrypted URL encoded form in User table,
        // therefore these forms must be compared!
        if (! activationCode.equals(userToBeActivated.getActivationEmail().getActivationCode())) {
            throw new IncorrectActivationCodeException("Received activation code is different from the one"
                    + " assigned to the user. Either activation link has not been generated by poptavka"
                    + " or another (newer one) activation link for given user has been generated.");
        }
        LOGGER.debug("action=verify_activation_code status=finish user=" + userToBeActivated);
        return userToBeActivated;
    }

    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private BusinessUser getUserByEmail(String email) {
        Validate.notEmpty(email);

        final Search searchByEmail = new Search(BusinessUser.class);
        searchByEmail.addFilterEqual("email", email.trim());

        // at most one user with given email can exist
        return (BusinessUser) this.generalService.searchUnique(searchByEmail);
    }

    private boolean activationExpired(ActivationCode decryptedCode) {
        final Date now = new Date();
        return now.after(new Date(decryptedCode.getValidity()));
    }

    private String serializeActivationCode(ActivationCode activationCode) {
        try {
            return jsonMapper.writeValueAsString(activationCode);
        } catch (IOException e) {
            throw new IllegalStateException("Activation code cannot be serialized!");
        }
    }

    private ActivationCode deserializeActivationCode(String activationCode) {
        try {
            return jsonMapper.readValue(activationCode, ActivationCode.class);
        } catch (IOException e) {
            throw new IllegalStateException("Activation code cannot be deserialized!", e);
        }
    }


    private void setActivationEmailForUser(BusinessUser user, ActivationCode activationCode,
                                           String serializedEncryptedCode) {
        final ActivationEmail activationEmail = new ActivationEmail();
        activationEmail.setActivationCode(serializedEncryptedCode);
        activationEmail.setValidTo(new Date(activationCode.getValidity()));
        user.setActivationEmail(activationEmail);
    }

    private SimpleMailMessage createActivationMailMessage(String userMail, String activationCode) {
        final SimpleMailMessage activationMessage = new SimpleMailMessage();

        Locale englishLocale = new Locale("en", "EN");
        ResourceBundle rb = ResourceBundle.getBundle("localization", englishLocale);
        String activationEmailText = rb.getString("uc10.mail.sentence1");

        activationMessage.setFrom("poptavka1@gmail.com");
        activationMessage.setTo(userMail);

        activationMessage.setSubject("Poptavka account activation");

        activationMessage.setText(activationEmailText + " \n" + activationCode);
        return activationMessage;

    }


    /**
     * Nested class for more handy work with activation links.
     */
    private static class ActivationCode {

        private String userEmail;
        private long validity;


        @JsonCreator()
        public ActivationCode(@JsonProperty("userEmail") String userEmail, @JsonProperty("validity") long validity) {
            this.userEmail = userEmail;
            this.validity = validity;
        }


        public long getValidity() {
            return validity;
        }

        public String getUserEmail() {
            return userEmail;
        }

    }

}
