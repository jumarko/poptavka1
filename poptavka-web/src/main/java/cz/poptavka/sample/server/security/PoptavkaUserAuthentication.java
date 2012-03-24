package cz.poptavka.sample.server.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;
/**
 * This class represents authenticated user.
 * @author kolkar
 *
 */
public class PoptavkaUserAuthentication implements Authentication {

    private static final long serialVersionUID = -3091441742758356129L;

    private boolean authenticated;

    private GrantedAuthority grantedAuthority;
    private Authentication authentication;

    public PoptavkaUserAuthentication(String role, Authentication authentication) {
        this.grantedAuthority = new GrantedAuthorityImpl(role);
        this.authentication = authentication;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        authorities.add(grantedAuthority);
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return authentication.getCredentials();
    }

    @Override
    public Object getDetails() {
        return authentication.getDetails();
    }

    @Override
    public Object getPrincipal() {
        return authentication.getPrincipal();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

}

