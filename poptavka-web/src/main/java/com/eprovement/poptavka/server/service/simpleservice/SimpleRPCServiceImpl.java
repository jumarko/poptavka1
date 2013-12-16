/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.server.service.simpleservice;

import com.eprovement.poptavka.client.service.demand.SimpleRPCService;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.shared.exceptions.ApplicationSecurityException;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.security.access.annotation.Secured;

/**
 * TODO Martin remove this class
 * The server side implementation of the simple RPC service.
 */
@Configurable
public class SimpleRPCServiceImpl extends AutoinjectingRemoteService implements SimpleRPCService {

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public String getData() throws RPCException, ApplicationSecurityException {

        final String serverInfo = getServletContext().getServerInfo();
        final String userAgent = getThreadLocalRequest().getHeader("User-Agent");

        // Escape data from the client to avoid cross-site script
        // vulnerabilities.
        return serverInfo + "<br>" + escapeHtml(userAgent);
    }

    /**
     * Escape an html string. Escaping data received from the client helps to
     * prevent cross-site script vulnerabilities.
     *
     * @param html the html string to escape
     * @return the escaped string
     */
    private String escapeHtml(String html) {
        if (html == null) {
            return null;
        }
        return html.replaceAll("&", "&amp;").replaceAll("<", "&lt;").replaceAll(">", "&gt;");
    }

    @Override
    @Secured(CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE)
    public String getSecuredData() throws RPCException, ApplicationSecurityException {
        return "Secured Data for Supplier role only";
    }
}
