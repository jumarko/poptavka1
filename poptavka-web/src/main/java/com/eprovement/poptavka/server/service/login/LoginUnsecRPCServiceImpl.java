package com.eprovement.poptavka.server.service.login;

import com.eprovement.poptavka.client.service.demand.LoginUnsecRPCService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.ExpiredActivationCodeException;
import com.eprovement.poptavka.exception.IncorrectActivationCodeException;
import com.eprovement.poptavka.exception.UserNotExistException;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.notification.welcome.WelcomeMessageSender;
import com.eprovement.poptavka.service.user.UserVerificationService;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.googlecode.genericdao.search.Search;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * All methods in this RPC service require access of authenticated user, which means that by the time any of these
 * methods is carried out there is a Authentication object in SecurityContextHolder that contains userId reference of
 * logged user.
 *
 * @author ivlcek
 */
@Configurable
public class LoginUnsecRPCServiceImpl extends AutoinjectingRemoteService implements LoginUnsecRPCService {

    //Services
    private GeneralService generalService;
    private UserVerificationService userVerificationService;
    private WelcomeMessageSender welcomeMessageSender;
    //Converters
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    /**************************************************************************/
    /* Autowired methods                                                      */
    /**************************************************************************/
    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setUserVerificationService(UserVerificationService userVerificationService) {
        this.userVerificationService = userVerificationService;
    }

    @Autowired
    public void setWelcomeMessageSender(WelcomeMessageSender welcomeMessageSender) {
        this.welcomeMessageSender = welcomeMessageSender;
    }

    /**************************************************************************/
    /* Autowired convertors                                                   */
    /**************************************************************************/
    @Autowired
    public void setBusinessUserConverter(
            @Qualifier("businessUserConverter") Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        this.businessUserConverter = businessUserConverter;
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    @Override
    public BusinessUserDetail getBusinessUserByEmail(String email) throws RPCException {
        final Search search = new Search(BusinessUser.class);
        search.addFilterEqual("email", email);
        return businessUserConverter.convertToTarget((BusinessUser) generalService.searchUnique(search));
    }

    /**
     * Reset password for user who forgot his password. New random password is saved into database.
     * @param userId whose password will be reset
     * @return new random password
     */
    @Override
    public String resetPassword(long userId) throws RPCException {
        String randomPassword = RandomStringUtils.randomAlphabetic(8);
        final User user = this.generalService.find(User.class, userId);
        user.setPassword(randomPassword);
        generalService.save(user);
        return randomPassword;
    }

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
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

    @Override
    public boolean sendActivationCodeAgain(BusinessUserDetail user) throws RPCException {
        // we must search business user by email because detail object doesn't have to proper ID already assigned.
        // TODO LATER : move this to the common place
        userVerificationService.sendNewActivationCode(findUserByEmail(user.getEmail()));
        // since activation mail has been sent in synchronous fashion everything should be ok
        return true;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private BusinessUser findUserByEmail(String email) {
        final Search search = new Search(BusinessUser.class);
        search.addFilterEqual("email", email);
        return (BusinessUser) generalService.searchUnique(search);
    }
}
