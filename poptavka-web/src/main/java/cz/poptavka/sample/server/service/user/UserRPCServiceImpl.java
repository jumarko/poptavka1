package cz.poptavka.sample.server.service.user;

import cz.poptavka.sample.client.service.demand.UserRPCService;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.shared.domain.UserDetail;

public class UserRPCServiceImpl extends AutoinjectingRemoteService implements UserRPCService {

    private static final long serialVersionUID = 1132667081084321575L;

    @Override
    public UserDetail loginUser(UserDetail user) {
        // TODO Auto-generated method stub
        return null;
    }

}
