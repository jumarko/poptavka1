package com.eprovement.poptavka.server.service.user;

import com.eprovement.poptavka.client.service.demand.UserRPCService;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.user.LoginService;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * All methods in this RPC service require access of authenticated user, which means that by the time any of these
 * methods is carried out there is a Authentication object in SecurityContextHolder that contains userId reference of
 * logged user.
 *
 * @author ivlcek
 */
@Configurable
public class UserRPCServiceImpl extends AutoinjectingRemoteService implements UserRPCService {

    private GeneralService generalService;
    private LoginService loginService;
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;
    private Converter<User, UserDetail> userConverter;

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setAccessRoleConverter(
            @Qualifier("accessRoleConverter") Converter<AccessRole, AccessRoleDetail> accessRoleConverter) {
        this.accessRoleConverter = accessRoleConverter;
    }

    @Autowired
    public void setBusinessUserConverter(
            @Qualifier("businessUserConverter") Converter<BusinessUser, BusinessUserDetail> businessUserConverter) {
        this.businessUserConverter = businessUserConverter;
    }

    @Autowired
    public void setUserConverter(
            @Qualifier("userConverter") Converter<User, UserDetail> userConverter) {
        this.userConverter = userConverter;
    }

    @Override
    public UserDetail loginUser(String email, String password) throws RPCException {
        final User user = this.loginService.loginUser(email, password);
        return new UserDetail(user.getId(), user.getEmail(),
                accessRoleConverter.convertToTargetList(user.getAccessRoles()));
    }

    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public BusinessUserDetail getBusinessUserById(Long userId) throws RPCException, ApplicationSecurityException {
        return businessUserConverter.convertToTarget(generalService.find(BusinessUser.class, userId));
    }


    @Override
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public UserDetail getUserById(Long userId) throws RPCException, ApplicationSecurityException {
        return userConverter.convertToTarget(generalService.find(User.class, userId));
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
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public UserDetail getLoggedUser() throws RPCException, ApplicationSecurityException {
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
    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    public BusinessUserDetail getLoggedBusinessUser() throws RPCException, ApplicationSecurityException {
        return businessUserConverter.convertToTarget(generalService.find(BusinessUser.class, getLoggedUserId()));
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
