package com.eprovement.poptavka.client.user;

import com.eprovement.poptavka.shared.domain.UserDetail.Role;

/**
 * Interface for uniting User Layout Tab Views for handling role-dependent usability.
 * @author Beho
 */
public interface StyleInterface {

    void setRoleInterface(Role role);

}
