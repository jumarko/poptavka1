/**
 *
 */
package com.eprovement.poptavka.client.common.security;

import com.eprovement.poptavka.client.common.login.LoginUICapabilities;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.RequestException;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author dmartin
 *
 */
public class LoginHandler implements ClickHandler {

    protected static final Logger LOGIN_HANDLER_LOGGER = Logger.getLogger("LoginHandler");
    private final short timeout = 1000;
    private static final String DEFAULT_SPRING_LOGIN_URL = "j_spring_security_check";
    private LoginUICapabilities loginUIComponent;
    private String springLoginUrl = null;

    public LoginHandler(final LoginUICapabilities loginUIComponent) {
        if (loginUIComponent == null) {
            throw new IllegalArgumentException("A LoginUICapabilities object must be provided");
        }
        this.loginUIComponent = loginUIComponent;
    }

    private String getFilteredLoginValue() {
        String unfilteredValue = this.loginUIComponent.getLoginValue();
        return unfilteredValue;
    }

    private String getFilteredPasswordValue() {
        String unfilteredValue = this.loginUIComponent.getPasswordValue();
        return unfilteredValue;
    }

    public String getSpringLoginUrl() {
        if (this.springLoginUrl == null) {
            this.springLoginUrl = GWT.getHostPageBaseURL() + DEFAULT_SPRING_LOGIN_URL;
        }
        return springLoginUrl;
    }

    @Override
    public void onClick(ClickEvent event) {

        // TODO ivlcek - figure out why the URL contains duplicite value of HostPageBase URL
        String url = GWT.getModuleBaseURL() + getSpringLoginUrl();
        url = "http://127.0.0.1:8888/j_spring_security_check";
        LOGIN_HANDLER_LOGGER.log(Level.FINEST, "getHostPageBaseURL=" + GWT.getHostPageBaseURL());
        LOGIN_HANDLER_LOGGER.log(Level.FINEST, "getModuleBaseURL=" + GWT.getModuleBaseURL());
        LOGIN_HANDLER_LOGGER.log(Level.FINEST, "final URL=" + url);

        final RequestBuilder rb = new RequestBuilder(RequestBuilder.POST, url);

        rb.setHeader("Content-Type", "application/x-www-form-urlencoded");
        rb.setHeader("X-GWT-Secured", "Logging...");
        // rb.setHeader("X-XSRF-Cookie", Cookies.getCookie("myCookieKey"));
        // TODO : work on this
        final StringBuilder sbParams = new StringBuilder(100);
        sbParams.append("j_username=");
        sbParams.append(getFilteredLoginValue());
        sbParams.append("&j_password=");
        sbParams.append(getFilteredPasswordValue());

        try {
            rb.sendRequest(sbParams.toString(), new RequestCallback() {

                @Override
                public void onResponseReceived(final Request request, final Response response) {

                    int status = response.getStatusCode();
                    if (status == Response.SC_OK) { // 200: everything's ok
                        loginUIComponent.setErrorMessage("You are logged !");
                        Timer t = new Timer() {

                            @Override
                            public void run() {
                                loginUIComponent.hide();
                            }
                        };
                        t.schedule(timeout);
                    } else if (status == Response.SC_UNAUTHORIZED) { // 401: oups...
                        // TODO ivlcek localize messages
                        loginUIComponent.setErrorMessage("Oups... Wrong credentials !");
                    } else { // something else ?
                        loginUIComponent.setErrorMessage("Oups... Unexpected error (" + status + ")");
                    }
                }

                @Override
                public void onError(final Request request, final Throwable exception) {
                    loginUIComponent.setErrorMessage("Oups Response not received... " + exception.getMessage());
                }
            });
        } catch (RequestException exception) {
            loginUIComponent.setErrorMessage("Oups catch branch... " + exception.getMessage());
        }
    }
}
