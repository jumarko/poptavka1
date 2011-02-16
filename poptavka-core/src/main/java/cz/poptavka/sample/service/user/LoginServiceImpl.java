package cz.poptavka.sample.service.user;

import cz.poptavka.sample.domain.user.User;
import org.springframework.stereotype.Service;

/**
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
@Service
public class LoginServiceImpl implements LoginService {

    /**
     * TODO: remove this.
     */
    private static final long LOGGED_USER_ID = 1L;

    /**
     * TODO: replace constant user id with ID of real logged user.
     * @return
     */
    @Override
    public User getLoggedUser() {
        final User loggedUser = new User();
        loggedUser.setId(LOGGED_USER_ID);
        return loggedUser;
    }
}
