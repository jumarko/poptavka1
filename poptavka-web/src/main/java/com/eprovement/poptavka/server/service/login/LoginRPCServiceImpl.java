/*
 * Copyright (C) 2013, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.login;

import com.eprovement.poptavka.client.service.login.LoginRPCService;
import com.eprovement.poptavka.domain.enums.Verification;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.exception.ExpiredActivationCodeException;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;
import com.eprovement.poptavka.exception.UserNotExistException;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.notification.welcome.WelcomeMessageSender;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.service.user.UserVerificationService;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * This RPC handles all secured requests from Login module.
 * All methods in this RPC service require access of authenticated user, which means that by the time any of these
 * methods is carried out there is a Authentication object in SecurityContextHolder that contains userId reference of
 * logged user.
 *
 * @author ivlcek
 */
@Configurable
public class LoginRPCServiceImpl extends AutoinjectingRemoteService implements LoginRPCService {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //Services
    @Autowired
    private GeneralService generalService;
    @Autowired
    private UserVerificationService userVerificationService;
    @Autowired
    private WelcomeMessageSender welcomeMessageSender;
    @Autowired
    private ClientService clientService;
    @Autowired
    private SupplierService supplierService;

    //Converters
    @Autowired
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;
    @Autowired
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Request business user detail by id.
     * @param userId of user
     * @return business user detail
     * @throws RPCException
     */
    @Override
    public BusinessUserDetail getBusinessUserById(Long userId) throws RPCException {
        return businessUserConverter.convertToTarget(generalService.find(BusinessUser.class, userId));
    }

    /**
     * Request business user detail by its email.
     * @param email
     * @return business user detail
     * @throws RPCException
     */
    @Override
    public BusinessUserDetail getBusinessUserByEmail(String email) throws RPCException {
        final Search search = new Search(BusinessUser.class);
        search.addFilterEqual("email", email);
        return businessUserConverter.convertToTarget((BusinessUser) generalService.searchUnique(search));
    }

    /**
     * Returns UserDetail of logged User. Since this RPC class requires access of authenticated user only, the logged
     * user will be always successfully retrieved from SecurityContextHolder.
     *
     * @return UserDetail of logged User
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    public UserDetail getLoggedUser() throws RPCException {
        User user = generalService.find(User.class, getLoggedUserId());
        return new UserDetail(user.getId(), user.getEmail(),
            accessRoleConverter.convertToTargetList(user.getAccessRoles()));
    }

    /**
     * Returns BusinessUserDetail of logged user.
     *
     * @return Logged BusinessUserDetail
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
    public BusinessUserDetail getLoggedBusinessUser() throws RPCException {
        return businessUserConverter.convertToTarget(generalService.find(BusinessUser.class, getLoggedUserId()));
    }

    /**
     * Login external supplier.
     * According to business user role recreates default notifications and sets user as VERIFIED.
     * @param userId
     */
    public void loginExternalUser(long userId) throws RPCException {
        //If user is client
        final BusinessUser businessUser = generalService.find(BusinessUser.class, userId);
        //recreate notifications
        businessUser.getSettings().getNotificationItems().clear();
        BusinessUserRole client = null;
        BusinessUserRole supplier = null;
        for (BusinessUserRole role : businessUser.getBusinessUserRoles()) {
            if (role instanceof Client) {
                client = role;
            }
            if (role instanceof Supplier) {
                supplier = role;
            }
        }
        if (supplier != null) {
            supplierService.createDefaultNotifications(supplier);
        } else if (client != null) {
            clientService.createDefaultNotifications(client);
        }
        //set user as verified
        businessUser.setVerification(Verification.VERIFIED);
        generalService.save(businessUser);
    }

    /**
     * Reset password for user who forgot his password. New random password is saved into database.
     * @param userId whose password will be reset
     * @return new random password
     */
    @Override
    public String resetPassword(long userId) throws RPCException {
        return userVerificationService.resetPassword(this.generalService.find(User.class, userId));
    }

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    /**
     * Activates user.
     * @param user
     * @param activationCode of user
     * @return the userActivationResult
     * @throws RPCException
     */
    @Override
    public UserActivationResult activateUser(BusinessUserDetail user, String activationCode) throws RPCException {
        final BusinessUser businessUser = findUserByEmail(user.getEmail());
        try {
            userVerificationService.activateUser(businessUser, StringUtils.trimToEmpty(activationCode));
        } catch (UserNotExistException unee) {
            return UserActivationResult.ERROR_UNKNOWN_USER;
        } catch (IncorrectActivationCodeException iace) {
            return UserActivationResult.ERROR_INCORRECT_ACTIVATION_CODE;
        } catch (ExpiredActivationCodeException eace) {
            return UserActivationResult.ERROR_EXPIRED_ACTIVATION_CODE;
        }
        welcomeMessageSender.sendWelcomeMessage(businessUser);
        return UserActivationResult.OK;
    }

    /**
     * Sends activation code again.
     * @param user
     * @return true if successfully sent, false otherwise
     * @throws RPCException
     */
    @Override
    public boolean sendActivationCodeAgain(BusinessUserDetail user) throws RPCException {
        // we must search business user by email because detail object doesn't have to proper ID already assigned.
        // TODO LATER : move this to the common place
        userVerificationService.sendNewActivationCode(findUserByEmail(user.getEmail()));
        // since activation mail has been sent in synchronous fashion everything should be ok
        return true;
    }

    /**
     * Checks if the given businessUser has an activation email and sends
     * an activation email if not.
     * @param businessUser
     * @return void
     * @throws RPCException
     */
    @Override
    public void hasActivationEmail(BusinessUserDetail businessUser) throws RPCException {
        User user = generalService.find(User.class, businessUser.getUserId());
        if (user.getActivationEmail() == null) {
            sendActivationCodeAgain(businessUser);
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Finds user by email.
     * @param email of user
     * @return business user
     */
    private BusinessUser findUserByEmail(String email) {
        final Search search = new Search(BusinessUser.class);
        search.addFilterEqual("email", email);
        return (BusinessUser) generalService.searchUnique(search);
    }

    /**
     * Retrieves userId of logged user from Authentication object.
     *
     * @return userId of logged user
     */
    private Long getLoggedUserId() {
        return ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId();
    }
}
