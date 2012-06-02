package com.eprovement.poptavka.server.service;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;

@Component("gwt-request-handler")
public class RemoteServiceHandler implements HttpRequestHandler,
        ApplicationContextAware {
    private ApplicationContext applicationContext;

    public void handleRequest(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) throws ServletException,
            IOException {
        String requestPath = httpServletRequest.getServletPath();

        Map<String, RemoteService> services = applicationContext
                .getBeansOfType(RemoteService.class);
        RemoteService service = null;
        for (String key : services.keySet()) {
            if (requestPath.endsWith(key)) {
                if (service != null) {
                    throw new IllegalStateException(
                            "More than one GWT RPC service bean matches request: "
                                    + requestPath);
                } else {
                    service = services.get(key);
                }
            }
        }
        if (service == null) {
            String availableServlets = "";
            for (String key : services.keySet()) {
                availableServlets += key + " -> "
                        + services.get(key).getClass().getName() + "\n";
            }
            throw new IllegalStateException(
                    "Cannot find GWT RPC service bean for request: "
                            + requestPath
                            + "\nMake sure that service implementation extends RemoteServiceImpl "
                            + "instead of GWT RemoteServiceServlet\nList of available beans:\n"
                            + availableServlets);
        }
        service.doPost(service, httpServletRequest, httpServletResponse);
    }

    public void setApplicationContext(ApplicationContext applicationContext)
        throws BeansException {
        this.applicationContext = applicationContext;
    }
}
