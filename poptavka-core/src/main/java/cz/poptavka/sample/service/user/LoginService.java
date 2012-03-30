package cz.poptavka.sample.service.user;

import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.LoginException;

/**
 * @author Juraj Martinka
 *         Date: 8.1.11
 */
public interface LoginService {
    User getLoggedUser();

    /**
     * Tries to log in user identified by {@code email} using given {@code password}.
     * If either email or password is incorrect, then {@link LoginException} is thrown.
     * If email and password matches then complete {@link User} object is returned.
     *
     * @param email aka "user name" - uniquely identifies user
     * @param password user's password in plain text
     * @return User object representing complete information about user with given {@code email}
     * @throws LoginException if email or password is incorrect
     */
    User loginUser(String email, String password) throws LoginException;
}
