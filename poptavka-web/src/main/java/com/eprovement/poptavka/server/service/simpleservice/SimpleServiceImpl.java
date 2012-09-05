/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.server.service.simpleservice;

import com.eprovement.poptavka.client.service.demand.SimpleService;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import org.springframework.security.access.annotation.Secured;

/**
 * The server side implementation of the simple RPC service.
 */
@SuppressWarnings("serial")
public class SimpleServiceImpl extends RemoteServiceServlet implements SimpleService {

    @Override
    @Secured(CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE)
    public String getData() throws RPCException {

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
    public String getSecuredData() throws RPCException {
        return "Secured Data for Supplier role only";
    }
}
