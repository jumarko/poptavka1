package cz.poptavka.sample.server.service.user;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.UserRPCService;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.shared.domain.UserDetail;

public class UserRPCServiceImpl extends AutoinjectingRemoteService implements UserRPCService {

    private static final long serialVersionUID = 1132667081084321575L;

    private GeneralService generalService;

    @Autowired
    public void setGeneralService(GeneralService generalService) {
        this.generalService = generalService;
    }

    @Override
    public UserDetail loginUser(UserDetail user) {
//        final List<User> existingUsers = this.generalService.searchByCriteria(
//                UserSearchCriteria.Builder.userSearchCriteria()
//                .withEmail(user.getEmail())
//                .withPassword(user.getPassword())
//                .build());
        return null;
    }

}
