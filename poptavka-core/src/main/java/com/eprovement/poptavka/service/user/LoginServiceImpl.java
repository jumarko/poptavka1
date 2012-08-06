package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.exception.LoginUserNotExistException;
import org.apache.commons.lang.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;

import com.eprovement.poptavka.application.security.Encryptor;
import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.exception.IncorrectPasswordException;
import com.eprovement.poptavka.exception.LoginException;
import com.eprovement.poptavka.service.GeneralService;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;


/**
 *
 * Basic imlementation of {@link LoginService}.
 *
 * @author Juraj Martinka
 *         Date: 30.3.2012
 */
public class LoginServiceImpl implements LoginService {

    /**
     * TODO: remove this.
     */
    private static final long LOGGED_USER_ID = 1L;

    private static final Logger LOGGER = LoggerFactory.getLogger(LoginServiceImpl.class);


    private final GeneralService generalService;
    private Encryptor encryptor;

    public LoginServiceImpl(GeneralService generalService, Encryptor encryptor) {
        Validate.notNull(generalService);
        Validate.notNull(encryptor);
        this.generalService = generalService;
        this.encryptor = encryptor;
    }

    /**
     * TODO: replace constant user id with ID of real logged user.
     * @return
     */
    @Override
    @Transactional(readOnly = true)
    public User getLoggedUser() {
        final User loggedUser = new User();
        loggedUser.setId(LOGGED_USER_ID);
        return loggedUser;
    }


    /**
     *  {@inheritDoc}
     *  @throws IllegalArgumentException if {@code email} or {@code password} is empty
     */
    @Override
    @Transactional(readOnly = true)
    public User loginUser(String email, String password) throws LoginException {
        Validate.notEmpty(email);
        Validate.notEmpty(password);

        LOGGER.debug("action=login_user status=start login={}", email);
        final Search searchByEmail = new Search(User.class);
        searchByEmail.addFilterEqual("email", email.trim());

        // at most one user with given email can exist
        final User user = (User) this.generalService.searchUnique(searchByEmail);
        if (user == null) {
            throw new LoginUserNotExistException("email=" + email);
        }

        try {
            if (! encryptor.matches(password, user.getPassword())) {
                throw new IncorrectPasswordException("For user with email=" + email);
            }
        } catch (EncryptionOperationNotPossibleException eonpe) {
            throw new IncorrectPasswordException("Invalid password stored in db for user with email=" + email + "."
                    + "Probably password is stored as a plaintext!", eonpe);
        }

        // everything is OK
        LOGGER.debug("action=login_user status=finish login={}", email);
        return user;
    }
}
