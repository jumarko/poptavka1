package com.eprovement.poptavka.server.service.user;

import com.eprovement.poptavka.client.service.demand.UserRPCService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.security.PoptavkaUserAuthentication;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.LoginService;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.adminModule.AccessRoleDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.context.SecurityContextHolder;

@Configurable
public class UserRPCServiceImpl extends AutoinjectingRemoteService implements UserRPCService {

    private static final long serialVersionUID = 1132667081084321575L;
    private GeneralService generalService;
    private LoginService loginService;
    private ClientService clientService;
    private Converter<AccessRole, AccessRoleDetail> accessRoleConverter;
    private Converter<BusinessUser, BusinessUserDetail> businessUserConverter;

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    @Autowired
    public void setClientService(ClientService clientService) {
        this.clientService = clientService;
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

    @Override
    public UserDetail loginUser(String email, String password) throws RPCException {
        final User user = this.loginService.loginUser(email, password);
        return new UserDetail(user.getId(), user.getEmail(),
                accessRoleConverter.convertToTargetList(user.getAccessRoles()));
    }

    @Override
    public BusinessUserDetail getUserById(Long userId) throws RPCException {
        //Find vs. SearchUnique ??
        return businessUserConverter.convertToTarget(generalService.find(BusinessUser.class, userId));
    }

    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }

    @Override
    public BusinessUserDetail getLoggedBusinessUser() throws RPCException {
        return getUserById(
                ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().getAuthentication()).getUserId());
    }

    @Override
    public UserDetail getLoggedUser() throws RPCException {
        User user = generalService.find(User.class, ((PoptavkaUserAuthentication) SecurityContextHolder.getContext().
                    getAuthentication()).getUserId());
        return new UserDetail(user.getId(), user.getEmail(),
                accessRoleConverter.convertToTargetList(user.getAccessRoles()));
    }
}
