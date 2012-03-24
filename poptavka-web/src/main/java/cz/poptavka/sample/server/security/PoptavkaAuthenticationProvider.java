package cz.poptavka.sample.server.security;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Custom implementation of authenticationProvider.
 * This class represents authentication mechanism.
 * @author kolkar
 *
 */
public class PoptavkaAuthenticationProvider implements AuthenticationProvider {

    private static final Log LOGGER = LogFactory.getLog(PoptavkaAuthenticationProvider.class);

    private static Map<String, String> users = new HashMap<String, String>();

    static {
        users.put("fabrizio", "javacodegeeks");
        users.put("justin", "javacodegeeks");
        users.put("client@test.com", "kreslo");
        users.put("supplier@test.com", "kreslo");
    }

    /**
     * Method used to authenticate user
     * @param authentication
     * @return authentication object that represents signed in user
     * @throws AuthenticationException
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.info("authentication");
        String username = (String) authentication.getPrincipal();
        String password = (String) authentication.getCredentials();

        if (users.get(username) == null) {
            throw new UsernameNotFoundException("User not found");
        }

        String storedPass = users.get(username);

        if (!storedPass.equals(password)) {
            throw new BadCredentialsException("Invalid password");
        }

        Authentication customAuthentication = new PoptavkaUserAuthentication("ROLE_USER", authentication);
        customAuthentication.setAuthenticated(true);

        return customAuthentication;

    }

    @Override
    public boolean supports(Class<? extends Object> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

}

