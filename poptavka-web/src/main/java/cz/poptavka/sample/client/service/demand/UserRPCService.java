package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.LoggedUserDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

@RemoteServiceRelativePath("service/user")
public interface UserRPCService extends RemoteService {

    LoggedUserDetail loginUser(UserDetail user);

    UserDetail getSignedUser(String sessionId);

    UserDetail getUserById(Long userId);

    /**
     * Checks wheter given {@code email} is available.
     * @param email ęmail address to be checked
     */
    boolean checkFreeEmail(String email);
}
