package com.eprovement.poptavka.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Juraj Martinka
 *         Date: 21.8.11
 */
public class PoptavkaAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoptavkaAuthenticationEntryPoint.class);

    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException exception) throws IOException, ServletException {

        LOGGER.debug("Authentication required");

        // TODO redirect to login page
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendRedirect("/poptavka/j_spring_security_check");


//        httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Authentication required");
    }

}
