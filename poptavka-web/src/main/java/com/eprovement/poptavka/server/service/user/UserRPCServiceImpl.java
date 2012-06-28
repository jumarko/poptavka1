package com.eprovement.poptavka.server.service.user;

import com.eprovement.poptavka.client.service.demand.UserRPCService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.server.converter.AccessRoleConverter;
import com.eprovement.poptavka.server.converter.BusinessUserConverter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.LoginService;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.converter.UserConverter;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(UserRPCService.URL)
public class UserRPCServiceImpl extends AutoinjectingRemoteService implements UserRPCService {

    private static final long serialVersionUID = 1132667081084321575L;
    private GeneralService generalService;
    private LoginService loginService;
    private ClientService clientService;
    private AccessRoleConverter roleConverter = new AccessRoleConverter();

    private BusinessUserConverter businessUserConverter = new BusinessUserConverter();

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

    @Override
    public UserDetail loginUser(String email, String password) throws RPCException {
        final User user = this.loginService.loginUser(email, password);
        return new UserDetail(user.getId(), user.getEmail(),
                roleConverter.convertToTargetList(user.getAccessRoles()));
    }

    @Override
    public BusinessUserDetail getUserById(Long userId) throws RPCException {
        //Find vs. SearchUnique ??
        return new UserConverter().convertToTarget(generalService.find(BusinessUser.class, userId));
    }

    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }
}
