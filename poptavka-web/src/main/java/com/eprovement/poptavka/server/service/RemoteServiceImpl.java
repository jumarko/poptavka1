package com.eprovement.poptavka.server.service;

import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gwt.user.client.rpc.IncompatibleRemoteServiceException;
import com.google.gwt.user.client.rpc.SerializationException;
import com.google.gwt.user.server.rpc.RPC;
import com.google.gwt.user.server.rpc.RPCRequest;

public abstract class RemoteServiceImpl extends AutoinjectingRemoteService implements RemoteService {

    private RemoteService service = null;

    public void doPost(RemoteService service, HttpServletRequest request, HttpServletResponse response) {
        this.service = service;
        doPost(request, response);
    }

    @Override
    public ServletContext getServletContext() {
        ServletContext context;
        try {
            context = super.getServletContext();
        } catch (Exception e) {
            context = getThreadLocalRequest().getSession().getServletContext();
        }
        return context;
    }

    @Override
    public String processCall(final String payload) throws SerializationException {
        final RPCRequest request = RPC.decodeRequest(payload, this.getClass(), this);
        try {
            Object result = request.getMethod().invoke(service, request.getParameters());
            return RPC.encodeResponseForSuccess(request.getMethod(), result, request.getSerializationPolicy());
        } catch (IllegalAccessException e) {
            return RPC.encodeResponseForFailure(request.getMethod(), e, request.getSerializationPolicy());
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            return RPC.encodeResponseForFailure(request.getMethod(), cause, request.getSerializationPolicy());
        } catch (IncompatibleRemoteServiceException ex) {
            return RPC.encodeResponseForFailure(null, ex, request.getSerializationPolicy());
        } finally {
            service = null;
        }
    }

}
