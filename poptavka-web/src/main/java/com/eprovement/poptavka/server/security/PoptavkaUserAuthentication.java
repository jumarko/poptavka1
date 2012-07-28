package com.eprovement.poptavka.server.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
/**
 * This class represents authenticated user.
 * @author kolkar
 *
 */
public class PoptavkaUserAuthentication implements Authentication {

    private static final long serialVersionUID = -3091441742758356129L;

    private boolean authenticated;

    private List<GrantedAuthority> grantedAuthority;
    private Authentication authentication;

    public PoptavkaUserAuthentication(User loggedUser, Authentication authentication) {
        this.grantedAuthority = new ArrayList<GrantedAuthority>();
        for (AccessRole role : loggedUser.getAccessRoles()) {
            this.grantedAuthority.add(new GrantedAuthorityImpl(role.getName()));
        }
        this.authentication = authentication;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        if (grantedAuthority != null) {
            authorities.addAll(grantedAuthority);
        }
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

