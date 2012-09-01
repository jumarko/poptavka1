package com.eprovement.poptavka.server.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.eprovement.poptavka.domain.user.User;
import com.eprovement.poptavka.domain.user.rights.AccessRole;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * This class represents authenticated user and all his granted authorities
 * @author kolkar
 *
 */
public class PoptavkaUserAuthentication implements Authentication {

    private static final long serialVersionUID = -3091441742758356129L;

    private boolean authenticated;

    private final List<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
    private final Authentication authentication;
    private final User loggedUser;

    public PoptavkaUserAuthentication(User loggedUser, Authentication authentication) {
        for (AccessRole role : loggedUser.getAccessRoles()) {
            this.grantedAuthority.add(new SimpleGrantedAuthority(role.getCode()));
        }
        this.authentication = authentication;
        this.loggedUser = loggedUser;
    }

    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(grantedAuthority);
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

    public Long getUserId() {
        return loggedUser.getId();
    }
}
