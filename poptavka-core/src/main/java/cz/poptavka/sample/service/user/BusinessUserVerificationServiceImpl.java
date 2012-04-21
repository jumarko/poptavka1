/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package cz.poptavka.sample.service.user;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.application.security.SymmetricKeyEncryptor;
import cz.poptavka.sample.domain.activation.ActivationEmail;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.domain.user.Verification;
import cz.poptavka.sample.exception.ExpiredActivationLinkException;
import cz.poptavka.sample.exception.IncorrectActivationLinkException;
import cz.poptavka.sample.exception.LoginUserNotExistException;
import cz.poptavka.sample.exception.UserNotExistException;
import cz.poptavka.sample.service.GeneralService;
import java.io.IOException;
import java.util.Date;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

public class BusinessUserVerificationServiceImpl implements BusinessUserVerificationService {

    private static final int DEFAULT_VALIDITY_LENGTH_MILLIS = 7 * 24 * 3600 * 1000;

    private final SymmetricKeyEncryptor symmetricEncryptor;
    private final GeneralService generalService;
    private String deploymentUrl;

    private final ObjectMapper jsonMapper = new ObjectMapper();


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
    @Transactional(propagation = Propagation.REQUIRED)
    public String generateActivationLink(BusinessUser businessUser) {
        Validate.notNull(businessUser, "User to be activated must be specified!");
        Validate.notNull(businessUser.getId(), "User to be activated must already haven an ID assigned!");

        final BusinessUser businessUserFromDb = this.generalService.find(BusinessUser.class, businessUser.getId());
        if (businessUserFromDb == null) {
            throw new UserNotExistException("No user with id=" + businessUser.getId() + " exists!");
        }

        final Date now = new Date();
        final Date linkValidTo = new Date(now.getTime() + DEFAULT_VALIDITY_LENGTH_MILLIS);
        final ActivationLink activationLink = new ActivationLink(businessUserFromDb.getEmail(), linkValidTo.getTime());

        final String encryptedLink = symmetricEncryptor.encrypt(serializeLink(activationLink));

        // emailActivation property can be overwritten multiple times.
        saveActivationEmailForUser(businessUserFromDb, activationLink, encryptedLink);

        return StringUtils.trimToEmpty(deploymentUrl) + encryptedLink;
    }


    /**
     * Checks if passed {@code activationLink} represents valid link for correct user
     * and activates that user if all requirements are satisfied - this practically means setting
     * {@link BusinessUserRole#verification} to  {@link Verification#VERIFIED} for every {@link BusinessUserRole}
     * assigned to the given business user.
     *
     * @param activationLink full activation link including host url,
     *                       e.g. "https://www.poptavam.com/poptavka/ABx+12KCLAAHSJKQIUHKWQsdfakjlPQOW122
     * @return business user which has been verified ({@link BusinessUserRole#verification}
     * @throws IncorrectActivationLinkException if non-valid activation link is passed, e.g. empty or without user email
     * @throws cz.poptavka.sample.exception.LoginUserNotExistException if activation link represents activation
     * of non-existing user
     * @throws ExpiredActivationLinkException if activation link expired already and cannot be used anymore
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public BusinessUser verifyActivationLink(String activationLink) throws LoginUserNotExistException,
            ExpiredActivationLinkException, IncorrectActivationLinkException {
        Validate.notEmpty(activationLink, "Activation link to be verified must not be null!");
        activationLink = stripDeploymentUrl(activationLink);
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
        // activation link is stored in an encrypted form in User table, therefore these encrypted forms
        // must be compared!
        if (! activationLink.equals(userToBeActivated.getActivationEmail().getActivationLink())) {
            throw new IncorrectActivationLinkException("Received activation link is different from the one"
                    + " assigned to the user. Link might have been generated by malicious user!");
        }
        for (BusinessUserRole role: userToBeActivated.getBusinessUserRoles()) {
            role.setVerification(Verification.VERIFIED);
        }
        generalService.save(userToBeActivated);

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

    private String stripDeploymentUrl(String activationLinkPlainText) {
        Validate.notEmpty(activationLinkPlainText);
        if (activationLinkPlainText.startsWith(deploymentUrl)) {
            return activationLinkPlainText.substring(deploymentUrl.length());
        }

        return activationLinkPlainText;
    }

    private void saveActivationEmailForUser(BusinessUser user, ActivationLink activationLink, String encryptedLink) {
        final ActivationEmail activationEmail = new ActivationEmail();
        activationEmail.setActivationLink(encryptedLink);
        activationEmail.setValidTo(new Date(activationLink.getValidity()));
        user.setActivationEmail(activationEmail);
        this.generalService.save(user);
    }



}
