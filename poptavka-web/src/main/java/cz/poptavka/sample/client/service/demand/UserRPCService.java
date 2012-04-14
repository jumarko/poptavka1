package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.LoggedUserDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

@RemoteServiceRelativePath("service/user")
public interface UserRPCService extends RemoteService {

    LoggedUserDetail loginUser(UserDetail user) throws CommonException;

    UserDetail getSignedUser(String sessionId) throws CommonException;

    UserDetail getUserById(Long userId) throws CommonException;

    /**
     * Checks wheter given {@code email} is available.
     * @param email ęmail address to be checked
     */
    boolean checkFreeEmail(String email) throws CommonException;
}
