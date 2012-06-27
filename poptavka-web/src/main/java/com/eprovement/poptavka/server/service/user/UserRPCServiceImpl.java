package com.eprovement.poptavka.server.service.user;

import com.googlecode.genericdao.search.Search;
import com.eprovement.poptavka.client.service.demand.UserRPCService;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.BusinessUserRole;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.GeneralService;
import com.eprovement.poptavka.service.user.ClientService;
import com.eprovement.poptavka.service.user.LoginService;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail.BusinessRole;
import com.eprovement.poptavka.shared.domain.converter.AccessRoleConverter;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(UserRPCService.URL)
public class UserRPCServiceImpl extends AutoinjectingRemoteService implements UserRPCService {

    private static final long serialVersionUID = 1132667081084321575L;
    private GeneralService generalService;
    private LoginService loginService;
    private ClientService clientService;
    private AccessRoleConverter roleConverter = new AccessRoleConverter();

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
    public UserDetail loginUser(BusinessUserDetail userDetail) throws RPCException {
        final User user = this.loginService.loginUser(userDetail.getEmail(), userDetail.getPassword());
        return new UserDetail(user.getId(), user.getEmail(),
                roleConverter.convertToTargetList(user.getAccessRoles()));
    }

    @Override
    public BusinessUserDetail getSignedUser(String sessionId) throws RPCException {
        // TODO make real implementation of getting user according to sessionID
        // now it's just fake string, that needs to be parsed
        String[] parsedSession = sessionId.split("=");
        Long userId = Long.parseLong(parsedSession[(parsedSession.length - 1)]);

        final BusinessUser user = (BusinessUser) generalService.searchUnique(
                new Search(User.class).addFilterEqual("id", userId));

        BusinessUserDetail userDetail = new BusinessUserDetail();

        List<BusinessUserRole> roles = user.getBusinessUserRoles();

        for (BusinessUserRole role : roles) {
            if (role instanceof Client) {
                userDetail.setClientId(role.getId());
                userDetail.addRole(BusinessRole.CLIENT);
            }
            if (role instanceof Supplier) {
                userDetail.setSupplierId(role.getId());
                userDetail.addRole(BusinessRole.SUPPLIER);
            }
            //add other roles
        }
        // TODO add other useful attributes like, count of new demands, offers,
        // messages and so on
        userDetail.setUserId(roles.get(0).getBusinessUser().getId());

        return userDetail;
    }

    @Override
    public BusinessUserDetail getUserById(Long userId) throws RPCException {
        return BusinessUserDetail.createUserDetail(generalService.find(BusinessUser.class, userId));
    }

    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }
}
