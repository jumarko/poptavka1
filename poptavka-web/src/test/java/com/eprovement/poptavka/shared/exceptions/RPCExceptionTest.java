/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.exceptions;

import com.eprovement.poptavka.RpcUtils;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.fail;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Test ensures that all RPC services declares "throws RPCException".
 * This is necessary to ensure proper exception hadling on client.
 *
 * @see RPCException
 * @see com.eprovement.poptavka.client.common.security.SecuredAsyncCallback
 */
public class RPCExceptionTest {


    @Test
    public void throwsRpcException() {
        final Set<Class> rpcClasses = RpcUtils.getRpcClasses();
        System.out.println("========================================================================================");
        System.out.println("Checking " + rpcClasses.size() + " rpc service classes:\n" + rpcClasses);
        System.out.println("========================================================================================");
        final Map<Class, List<Method>> violations = new HashMap<Class, List<Method>>();
        for (Class rpcClass : rpcClasses) {
            final List<Method> violatedMethods = new ArrayList<Method>();
            for (Method rpcMethod : rpcClass.getDeclaredMethods()) {
                if (isPublicRpcMethod(rpcMethod)) {
                    boolean throwsRpcException = false;
                    for (Class<?> declaredException : rpcMethod.getExceptionTypes()) {
                        if (RPCException.class.equals(declaredException)) {
                            throwsRpcException = true;
                        }
                    }
                    if (! throwsRpcException) {
                        violatedMethods.add(rpcMethod);
                    }
                }
            }
            if (violatedMethods.size() > 0) {
                violations.put(rpcClass, violatedMethods);
            }
        }
        if (violations.size() > 0) {
            fail("Not all RPC methods declared 'throws RPCException'. This is required for proper Exception handling on"
                    + " GWT client (see SecuredAsyncCallback), Please check method signature and consider"
                    + " adding clause 'throws RPCException'. It is also possible that you forgot to add @Autowired "
                    + " to your Setter method.\n"
                    + " Please, check following methods!\n" + RpcUtils.formatViolationsMessage(violations));
        }
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private static boolean isPublicRpcMethod(Method rpcMethod) {
        if (rpcMethod.getAnnotation(RpcExceptionUnaware.class) != null) {
            // annotated with special annotation which means to ignore this method
            return false;
        }
        if (rpcMethod.getAnnotation(Autowired.class) != null) {
            // this method is very probably used only for dependency injection (most common case is the SETTER)
            return false;
        }
        if (rpcMethod.getName().equals("readResolve")) {
            // not sure why readResolve is presented in all RPC service classes (maybe aspectj stuff)
            return false;
        }
        // each other public method is considered to be regular RPC method used on client
        return Modifier.isPublic(rpcMethod.getModifiers());
    }



}
