package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;


/**
 * Represents full detail of AccessRole. Serves for creating new
 * AccessRole or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class UserServiceDetail implements IsSerializable {

    private ServiceDetail service;
    private UserDetail user;
    private String status;

    /** for serialization. **/
    public UserServiceDetail() {
    }

    public UserServiceDetail(UserServiceDetail role) {
        this.updateWholeUserService(role);
    }


    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeUserService(UserServiceDetail detail) {
        service = detail.getService();
        user = detail.getUser();
        status = detail.getStatus();
    }

    public ServiceDetail getService() {
        return service;
    }

    public void setService(ServiceDetail service) {
        this.service = service;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public UserDetail getUser() {
        return user;
    }

    public void setUser(UserDetail user) {
        this.user = user;
    }

    @Override
    public String toString() {

        return "\nGlobal UserService Detail Info:"
                + "\n   Service="
                + service.toString() + "\n     User="
                + user.toString() + "\n    Status="
                + status;
    }
}
