package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.UserDetail;

@RemoteServiceRelativePath("service/user")
public interface UserRPCService extends RemoteService {

    UserDetail loginUser(UserDetail user);

}
