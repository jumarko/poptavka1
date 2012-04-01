package cz.poptavka.sample.service.user;

import org.apache.commons.lang.Validate;
import org.springframework.transaction.annotation.Transactional;

import com.googlecode.genericdao.search.Search;

import cz.poptavka.sample.application.security.Encryptor;
import cz.poptavka.sample.domain.user.User;
import cz.poptavka.sample.exception.IncorrectPasswordException;
import cz.poptavka.sample.exception.LoginException;
import cz.poptavka.sample.exception.UserNotExistException;
import cz.poptavka.sample.service.GeneralService;


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

        final Search searchByEmail = new Search(User.class);
        searchByEmail.addFilterEqual("email", email.trim());

        // at most one user with given email can exist
        final User user = (User) this.generalService.searchUnique(searchByEmail);
        if (user == null) {
            throw new UserNotExistException("email=" + email);
        }

        if (! encryptor.matches(password, user.getPassword())) {
            throw new IncorrectPasswordException("For user with email=" + email);
        }

        // everything is OK
        return user;
    }
}
