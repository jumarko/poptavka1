package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.google.gwt.user.client.rpc.AsyncCallback;
import org.springframework.security.access.annotation.Secured;

public interface UserRPCServiceAsync {

    void loginUser(String email, String password, AsyncCallback<UserDetail> callback);

    void getBusinessUserById(Long userId, AsyncCallback<BusinessUserDetail> callback);

    void getUserById(Long userId, AsyncCallback<UserDetail> callback);

    void getLoggedBusinessUser(AsyncCallback<BusinessUserDetail> callback);

    void getLoggedUser(AsyncCallback<UserDetail> callback);

    @Secured(CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE)
    void getBusinessUserByEmail(String email, AsyncCallback<BusinessUserDetail> async);
}
