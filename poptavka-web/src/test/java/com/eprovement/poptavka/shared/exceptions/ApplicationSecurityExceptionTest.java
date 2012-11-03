/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.exceptions;

import com.eprovement.poptavka.RpcUtils;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.Assert;
import org.junit.Test;
import org.springframework.security.access.annotation.Secured;

public class ApplicationSecurityExceptionTest {

    @Test
    public void securedRpcMethodShouldThrowApplicationSecurityException() {
        final MethodRule securedMethodShouldThrowApplicationSecurityException = new MethodRule() {
            @Override
            boolean checkMethod(Method method) {
                return isSecuredMethod(method)
                        &&  ! throwsApplicationSecurityException(method);
            }
        };
        final Map<Class, List<Method>> violations =
                checkAllRpcMethods(securedMethodShouldThrowApplicationSecurityException);
        reportViolations(violations, "Not all Secured RPC methods declare 'throws ApplicationSecurityException'.\n "
                + "This is required for proper Exception handling on GWT client "
                + "(see RPCExceptionAspect and SecuredAsyncCallback)");
    }


    @Test
    public void unsecuredRpcMethodShouldNotThrowApplicationSecurityException() {

        final MethodRule unsecuredMethodShouldNotThrowApplicationSecurityException = new MethodRule() {
            @Override
            boolean checkMethod(Method method) {
                return !isSecuredMethod(method)
                        && throwsApplicationSecurityException(method);
            }
        };
        final Map<Class, List<Method>> violations =
                checkAllRpcMethods(unsecuredMethodShouldNotThrowApplicationSecurityException);

        reportViolations(violations, "Unsecured RPC methods should NOT declare 'throws ApplicationSecurityException'.\n"
                + "This is confusing and should be avoided!");
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------

    private abstract static class MethodRule {
        /**
         * Checks given method and return appropriate result.
         * @param method
         * @return false, if method violates any rule, true otherwise
         */
        abstract boolean checkMethod(Method method);
    }

    private Map<Class, List<Method>> checkAllRpcMethods(MethodRule methodRule) {
        final Set<Class> rpcClasses = RpcUtils.getRpcClasses();
        final Map<Class, List<Method>> violations = new HashMap<Class, List<Method>>();
        for (Class rpcClass : rpcClasses) {
            // initial violations are empty for each class
            violations.put(rpcClass, new ArrayList<Method>());

            for (Method method : rpcClass.getMethods()) {
                if (methodRule.checkMethod(method)) {
                    addViolation(violations, rpcClass, method);
                }
            }
        }
        return violations;
    }



    private void reportViolations(Map<Class, List<Method>> violations, String baseMessage) {
        for (Map.Entry<Class, List<Method>> violation : violations.entrySet()) {
            if (violation.getValue().size() > 0) {
                Assert.fail(baseMessage
                        + "\nPlease, check following methods!\n" + RpcUtils.formatViolationsMessage(violations));
                break;
            }
        }
    }


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


    private void addViolation(Map<Class, List<Method>> violations, Class rpcClass, Method method) {
        violations.get(rpcClass).add(method);
    }


}
