package cz.poptavka.sample.service.user;

import cz.poptavka.sample.domain.user.User;

/**
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
public interface LoginService {
    User getLoggedUser();
}
