package cz.poptavka.sample.server.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Juraj Martinka
 *         Date: 21.8.11
 */
public class PoptavkaAuthenticationSuccessHandler implements
        AuthenticationSuccessHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(PoptavkaAuthenticationSuccessHandler.class);

    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response, Authentication authentication) throws IOException,
            ServletException {

        LOGGER.debug("Authentication success");

        HttpServletResponse httpResponse = (HttpServletResponse) response;
        httpResponse.sendError(HttpServletResponse.SC_OK, "Authentication success");
    }

}
