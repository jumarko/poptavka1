package com.eprovement.poptavka.server.security;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.service.user.LoginService;

/**
 * Custom implementation of authenticationProvider.
 * This class represents authentication mechanism.
 * @author kolkar
 *
 */
public class PoptavkaAuthenticationProvider implements AuthenticationProvider {

    private static final Log LOGGER = LogFactory.getLog(PoptavkaAuthenticationProvider.class);

    private LoginService loginService;

    @Autowired
    public void setLoginService(LoginService loginService) {
        this.loginService = loginService;
    }

    public PoptavkaAuthenticationProvider(LoginService loginService) {
        super();
        this.loginService = loginService;
    }

    /**
     * Method used to authenticate user
     * @param authentication
     * @return authentication object that represents signed in user
     * @throws AuthenticationException
     */
    @Override
    @Transactional(readOnly = true)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.info("authenticate");
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        // refactor this with StringUtils.isEmpty
        if (username == null || username.isEmpty()) {
            throw new UsernameNotFoundException("Username not provided");
        }

        if (password == null || password.isEmpty()) {
            throw new BadCredentialsException("Password not provided");
        }

        LOGGER.info("authenticate calls loginService");
        final User loggedUser = this.loginService.loginUser(username, password);

        Authentication customAuthentication = new PoptavkaUserAuthentication(loggedUser, authentication);
        customAuthentication.setAuthenticated(true);

        return customAuthentication;

    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

