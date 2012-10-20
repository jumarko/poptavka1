package com.eprovement.poptavka.server.service.secured.user;

import com.eprovement.poptavka.client.service.demand.SecuredUserRPCService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * All methods in this RPC service require access of authenticated user, which means that by the time any of these
 * methods is carried out there is a Authentication object in SecurityContextHolder that contains userId reference of
 * logged user.
 *
 * @author ivlcek
 */
@Configurable
public class SecuredUserRPCServiceImpl extends AutoinjectingRemoteService implements SecuredUserRPCService {

    private static final long serialVersionUID = 9027367081084322983L;
    private GeneralService generalService;
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
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

    // TODO ivlcek - refactor this class and remove unused staff

    /**
     * Returns UserDetail of logged User. Since this RPC class requires access of authenticated user only, the logged
     * user will be always successfully retrieved from SecurityContextHolder.
     *
     * @return UserDetail of logged User
     * @throws RPCException
     * @throws ApplicationSecurityException
     */
    @Override
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
