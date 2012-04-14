package cz.poptavka.sample.server.service.user;

import com.googlecode.genericdao.search.Search;
import cz.poptavka.sample.client.service.demand.UserRPCService;
import cz.poptavka.sample.domain.user.BusinessUser;
import cz.poptavka.sample.domain.user.BusinessUserRole;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.user.ClientService;
import cz.poptavka.sample.service.user.LoginService;
import cz.poptavka.sample.shared.domain.LoggedUserDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.UserDetail.Role;
import cz.poptavka.sample.shared.exceptions.RPCException;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRPCServiceImpl extends AutoinjectingRemoteService implements UserRPCService {

    private static final long serialVersionUID = 1132667081084321575L;
    private GeneralService generalService;
    private LoginService loginService;
    private ClientService clientService;


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
    public LoggedUserDetail loginUser(UserDetail userDetail) throws RPCException {
        final User user = this.loginService.loginUser(userDetail.getEmail(), userDetail.getPassword());
        return new LoggedUserDetail(user.getId(), user.getEmail(), user.getAccessRoles());
    }

    @Override
    public UserDetail getSignedUser(String sessionId) throws RPCException {
        // TODO make real implementation of getting user according to sessionID
        // now it's just fake string, that needs to be parsed
        String[] parsedSession = sessionId.split("=");
        Long userId = Long.parseLong(parsedSession[(parsedSession.length - 1)]);

        final BusinessUser user = (BusinessUser) generalService.searchUnique(
                new Search(User.class).addFilterEqual("id", userId));

        UserDetail userDetail = new UserDetail();

        List<BusinessUserRole> roles = user.getBusinessUserRoles();
        for (BusinessUserRole role : roles) {
            if (role instanceof Client) {
                userDetail.setClientId(role.getId());
                userDetail.addRole(Role.CLIENT);
            }
            if (role instanceof Supplier) {
                userDetail.setSupplierId(role.getId());
                userDetail.addRole(Role.SUPPLIER);
            }
            //add other roles
        }
        // TODO add other useful attributes like, count of new demands, offers,
        // messages and so on
        userDetail.setUserId(roles.get(0).getBusinessUser().getId());

        return userDetail;
    }

    @Override
    public UserDetail getUserById(Long userId) throws RPCException {
        return UserDetail.createUserDetail(generalService.find(BusinessUser.class, userId));
    }


    @Override
    public boolean checkFreeEmail(String email) throws RPCException {
        return clientService.checkFreeEmail(email);
    }
}
