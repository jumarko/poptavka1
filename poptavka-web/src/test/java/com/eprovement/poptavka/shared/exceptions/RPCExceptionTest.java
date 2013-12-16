/*
 * Copyright (C) 2011, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.exceptions;

import com.eprovement.poptavka.RpcUtils;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

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

        final Map<Class, List<Method>> violations = RpcUtils.checkAllPublicRpcMethods(new RpcUtils.MethodRule() {
            @Override
            public boolean checkMethod(Method method) {
                boolean throwsRpcException = false;
                for (Class<?> declaredException : method.getExceptionTypes()) {
                    if (RPCException.class.equals(declaredException)) {
                        throwsRpcException = true;
                    }
                }
                return !isRpcExceptionAwareMethod(method) || throwsRpcException;
            }
        });
        RpcUtils.reportViolations(violations,
                "Not all RPC methods declared 'throws RPCException'. This is required for proper Exception handling on"
                + " GWT client (see SecuredAsyncCallback), Please check method signature and consider"
                + " adding clause 'throws RPCException'. It is also possible that you forgot to add @Autowired "
                + " to your Setter method.");
    }


    //--------------------------------------------------- HELPER METHODS -----------------------------------------------
    private static boolean isRpcExceptionAwareMethod(Method rpcMethod) {
        return rpcMethod.getAnnotation(RpcExceptionUnaware.class) == null
                && rpcMethod.getAnnotation(Autowired.class) == null
                && rpcMethod.getAnnotation(Value.class) == null;

    }



}
