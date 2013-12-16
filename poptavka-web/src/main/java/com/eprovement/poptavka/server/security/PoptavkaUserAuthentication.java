/*
 * Copyright (C) 2012, eProvement s.r.o. All rights reserved.
 */
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
 */
public class PoptavkaUserAuthentication implements Authentication {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private boolean authenticated;
    private final List<GrantedAuthority> grantedAuthority = new ArrayList<GrantedAuthority>();
    private final Authentication authentication;
    private final User loggedUser;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Creates PoptavkaUserAuthentication instance.
     * @param loggedUser
     * @param authentication
     */
    public PoptavkaUserAuthentication(User loggedUser, Authentication authentication) {
        for (AccessRole role : loggedUser.getAccessRoles()) {
            this.grantedAuthority.add(new SimpleGrantedAuthority(role.getCode()));
        }
        this.authentication = authentication;
        this.loggedUser = loggedUser;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    /**
     * Sets authenticated value.
     * @param authenticated true if authenticated, false otherwise
     * @throws IllegalArgumentException
     */
    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    /**
     * @return the collection of GrantedAuthorities
     */
    @Override
    public Collection<GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(grantedAuthority);
    }

    /**
     * @return the creadetials
     */
    @Override
    public Object getCredentials() {
        return authentication.getCredentials();
    }

    /**
     * @return the details
     */
    @Override
    public Object getDetails() {
        return authentication.getDetails();
    }

    /**
     * @return the principal
     */
    @Override
    public Object getPrincipal() {
        return authentication.getPrincipal();
    }

    /**
     * @return the name
     */
    @Override
    public String getName() {
        return this.getClass().getSimpleName();
    }

    /**
     * @return the user id
     */
    public Long getUserId() {
        return loggedUser.getId();
    }

    /**
     * @return true if authenticated, false otherwise
     */
    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }
}
