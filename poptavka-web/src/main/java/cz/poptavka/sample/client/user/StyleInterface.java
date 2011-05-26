package cz.poptavka.sample.client.user;

import cz.poptavka.sample.shared.domain.UserDetail.Role;

/**
 * Interface for uniting User Layout Tab Views for handling role-dependent usability.
 * @author Beho
 */
public interface StyleInterface {

    void setRoleInterface(Role role);

}
