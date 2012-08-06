/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.service.user;

import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.application.security.SymmetricKeyEncryptor;
import com.eprovement.poptavka.domain.activation.ActivationEmail;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.exception.ExpiredActivationLinkException;
import com.eprovement.poptavka.exception.IncorrectActivationLinkException;
import com.eprovement.poptavka.exception.UserNotExistException;
import com.eprovement.poptavka.service.GeneralService;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

public class BusinessUserVerificationServiceImpl implements BusinessUserVerificationService {

    private static final int DEFAULT_VALIDITY_LENGTH_MILLIS = 7 * 24 * 3600 * 1000;

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessUserVerificationServiceImpl.class);

    private final SymmetricKeyEncryptor symmetricEncryptor;
    private final GeneralService generalService;
    private String deploymentUrl;

    private final ObjectMapper jsonMapper = new ObjectMapper();
    private static final String USER_ACTIVATION_RESOURCE_URI = "api/user/activation?link=";


    public BusinessUserVerificationServiceImpl(SymmetricKeyEncryptor symmetricEncryptor,
            GeneralService generalService) {
        Validate.notNull(symmetricEncryptor);
        Validate.notNull(generalService);

        this.symmetricEncryptor = symmetricEncryptor;
        this.generalService = generalService;
    }

    public void setDeploymentUrl(String deploymentUrl) {
        this.deploymentUrl = deploymentUrl;
    }

    @Override
    public String generateActivationLink(BusinessUser businessUser) {
        Validate.notNull(businessUser, "User to be activated must be specified!");

        LOGGER.info("action=generate_activation_link status=start email={} businuessUser={}",
                businessUser.getEmail(), businessUser);

        final Date now = new Date();
        final Date linkValidTo = new Date(now.getTime() + DEFAULT_VALIDITY_LENGTH_MILLIS);
        final ActivationLink activationLink = new ActivationLink(businessUser.getEmail(), linkValidTo.getTime());

        // URL encoding must be performed because encrypted link can contain '/' character
        final String encryptedLink = encode(symmetricEncryptor.encrypt(serializeLink(activationLink)));

        // emailActivation property can be overwritten multiple times.
        setActivationEmailForUser(businessUser, activationLink, encryptedLink);

        LOGGER.info("action=generate_activation_link status=finish email={} validTo={} businuessUser={} ",
                new Object[] {businessUser.getEmail(), linkValidTo, businessUser});

        return StringUtils.trimToEmpty(deploymentUrl) + USER_ACTIVATION_RESOURCE_URI
                + businessUser.getActivationEmail().getActivationLink();
    }


    @Override
    @Transactional
    public BusinessUser verifyUser(String activationLink) {
        LOGGER.debug("action=verify_user status=start");

        final BusinessUser businessUser = verifyActivationLink(activationLink);
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
    public BusinessUser verifyActivationLink(String activationLink) throws UserNotExistException,
            ExpiredActivationLinkException, IncorrectActivationLinkException {
        Validate.notEmpty(activationLink, "Activation link to be verified must not be null!");

        LOGGER.debug("action=verify_activation_link status=start");

        // DO NOT URL DECODE activationLink - this has already be done by Spring MVC infrastructure
        activationLink = stripUrlPrefix(activationLink);
        final ActivationLink decryptedLink = deserializeLink(symmetricEncryptor.decrypt(activationLink));

        if (linkExpired(decryptedLink)) {
            throw new ExpiredActivationLinkException("Activation link expired. "
                    + "New activation link for user must be generated.");
        }

        final BusinessUser userToBeActivated = getUserByEmail(decryptedLink.getUserEmail());
        if (userToBeActivated == null) {
            throw new UserNotExistException("Relevant user does not exist - activation link is broken!");
        }
        if (userToBeActivated.getActivationEmail() == null) {
            throw new IncorrectActivationLinkException("No activation email has been set for user. Activation link "
                    + decryptedLink + " might have been generated by malicious user!");
        }
        // activation link is stored in an encrypted URL encoded form in User table,
        // therefore these forms must be compared!
        if (! activationLink.equals(decode(userToBeActivated.getActivationEmail().getActivationLink()))) {
            throw new IncorrectActivationLinkException("Received activation link is different from the one"
                    + " assigned to the user. Either activation link has not been generated by poptavka"
                    + " or another (newer one) activation link for given user has been generated.");
        }
        LOGGER.debug("action=verify_activation_link status=finish user=" + userToBeActivated);
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

    private boolean linkExpired(ActivationLink decryptedLink) {
        final Date now = new Date();
        return now.after(new Date(decryptedLink.getValidity()));
    }

    private String serializeLink(ActivationLink activationLink) {
        try {
            return jsonMapper.writeValueAsString(activationLink);
        } catch (IOException e) {
            throw new IllegalStateException("Activation link cannot be serialized!");
        }
    }

    private ActivationLink deserializeLink(String activationLink) {
        try {
            return jsonMapper.readValue(activationLink, ActivationLink.class);
        } catch (IOException e) {
            throw new IllegalStateException("Activation link cannot be deserialized!", e);
        }
    }

    private String stripUrlPrefix(String activationLinkPlainText) {
        Validate.notEmpty(activationLinkPlainText);
        if (activationLinkPlainText.startsWith(deploymentUrl)) {
            activationLinkPlainText = activationLinkPlainText.substring(deploymentUrl.length());
        }
        if (activationLinkPlainText.startsWith(USER_ACTIVATION_RESOURCE_URI)) {
            activationLinkPlainText = activationLinkPlainText.substring(USER_ACTIVATION_RESOURCE_URI.length());
        }

        return activationLinkPlainText;
    }

    private void setActivationEmailForUser(BusinessUser user, ActivationLink activationLink, String linkString) {
        final ActivationEmail activationEmail = new ActivationEmail();
        activationEmail.setActivationLink(linkString);
        activationEmail.setValidTo(new Date(activationLink.getValidity()));
        user.setActivationEmail(activationEmail);
    }

    private String encode(String paramValue) {
        try {
            return URLEncoder.encode(paramValue, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Never should occur - unsupported encoding", e);
        }
    }

    private String decode(String urlEncoded) {
        try {
            return URLDecoder.decode(urlEncoded, "utf-8");
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("Never should occur - unsupported encoding", e);
        }
    }

    /**
     * Nested class for more handy work with activation links.
     */
    private static class ActivationLink {

        private String userEmail;
        private long validity;

        private ActivationLink() {
            // ONLY FOR JACKSON deserialization mechanism
        }

        public ActivationLink(String userEmail, long validity) {
            this.userEmail = userEmail;
            this.validity = validity;
        }


        public long getValidity() {
            return validity;
        }

        public void setValidity(long validity) {
            this.validity = validity;
        }

        public String getUserEmail() {
            return userEmail;
        }

        public void setUserEmail(String userEmail) {
            this.userEmail = userEmail;
        }

    }

}
