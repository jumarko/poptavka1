/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.exceptions;

import com.eprovement.poptavka.RpcUtils;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.security.access.annotation.Secured;

public class ApplicationSecurityExceptionTest {

    @Test
    public void securedRpcMethodShouldThrowApplicationSecurityException() {
        final RpcUtils.MethodRule securedMethodShouldThrowApplicationSecurityException = new RpcUtils.MethodRule() {
            @Override
            public boolean checkMethod(Method method) {
                return !isSecuredMethod(method)
                        || method.getDeclaringClass().isInterface()
                        || throwsApplicationSecurityException(method);

            }
        };
        final Map<Class, List<Method>> violations =
                RpcUtils.checkAllPublicRpcMethods(securedMethodShouldThrowApplicationSecurityException);
        RpcUtils.reportViolations(violations,
                "Not all Secured RPC methods declare 'throws ApplicationSecurityException'.\n "
                + "This is required for proper Exception handling on GWT client "
                + "(see RPCExceptionAspect and SecuredAsyncCallback)");
    }


    @Test
    public void unsecuredRpcMethodShouldNotThrowApplicationSecurityException() {

        final RpcUtils.MethodRule unsecuredMethodShouldNotThrowApplicationSecurityException =
                new RpcUtils.MethodRule() {
                    @Override
                    public boolean checkMethod(Method method) {
                        return isSecuredMethod(method)
                                || method.getDeclaringClass().isInterface()
                                || ! throwsApplicationSecurityException(method);
                    }
                };
        final Map<Class, List<Method>> violations =
                RpcUtils.checkAllPublicRpcMethods(unsecuredMethodShouldNotThrowApplicationSecurityException);

        RpcUtils.reportViolations(violations,
                "Unsecured RPC methods should NOT declare 'throws ApplicationSecurityException'.\n"
                + "This is confusing and should be avoided!");
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------



    private boolean isSecuredMethod(Method method) {
        return method.getAnnotation(Secured.class) != null;
    }

    private boolean throwsApplicationSecurityException(Method method) {
        final Class<?>[] exceptionTypes = method.getExceptionTypes();
        if (exceptionTypes != null) {
            for (Class<?> exceptionType : exceptionTypes) {
                if (exceptionType == ApplicationSecurityException.class) {
                    return true;
                }
            }
        }
        return false;
    }



}
