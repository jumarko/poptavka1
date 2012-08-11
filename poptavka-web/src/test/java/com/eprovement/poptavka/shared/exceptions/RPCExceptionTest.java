/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.shared.exceptions;

import com.eprovement.poptavka.util.reflection.ClassFilter;
import com.eprovement.poptavka.util.reflection.ReflectionUtils;
import com.google.gwt.user.client.rpc.RemoteService;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.ClassMetadata;

/**
 * Test ensures that all RPC services declares "throws RPCException".
 * This is necessary to ensure proper exception hadling on client.
 *
 * @see RPCException
 * @see com.eprovement.poptavka.client.common.security.SecuredAsyncCallback
 */
public class RPCExceptionTest {

    private static final String BASE_PACKAGE_NAME = "com.eprovement.poptavka";
    private static final String CLIENT_SERVICE_PACKAGE_NAME = "com.eprovement.poptavka.client.service";
    private static final String SERVER_PACKAGE_NAME = "com.eprovement.poptavka.server";
    private static final Set<Class> RPC_CLASSES = new HashSet<Class>();

    private static final String METHODS_VALIDATIONS_SEPARATOR = ", ";

    /**
     * Load all RPC classes for testing.
     */
    @BeforeClass
    public static void setUp() throws Exception {
        final ClassFilter rpcClassFilter = new ClassFilter() {
            @Override
            public boolean match(ClassMetadata classMetadata) {
                return isRpcServiceClass(classMetadata.getClassName());
            }
        };

        RPC_CLASSES.addAll(ReflectionUtils.findClasses(SERVER_PACKAGE_NAME, rpcClassFilter));
        RPC_CLASSES.addAll(ReflectionUtils.findClasses(CLIENT_SERVICE_PACKAGE_NAME, rpcClassFilter));
    }

    private static Set<? extends BeanDefinition> findClassesInPackage(
            ClassPathScanningCandidateComponentProvider provider, String basePackageName) {
        final Set<? extends BeanDefinition> rpcClasses = provider.findCandidateComponents(basePackageName);

        if (rpcClasses.size() == 0) {
            System.err.println("Unable to find any class in base package=" + basePackageName);
        }
        return rpcClasses;
    }


    @Test
    public void throwsRpcException() {
        System.out.println("========================================================================================");
        System.out.println("Checking " + RPC_CLASSES.size() + " rpc service classes:\n" + RPC_CLASSES);
        System.out.println("========================================================================================");
        final Map<Class, List<Method>> violations = new HashMap<Class, List<Method>>();
        for (Class rpcClass : RPC_CLASSES) {
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
                    + " Please, check following methods!\n" + formatViolationsMessage(violations));
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


    private static boolean isRpcServiceClass(String className) {
        try {
            return RemoteService.class.isAssignableFrom(Class.forName(className));
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }


    private static String formatViolationsMessage(Map<Class, List<Method>> violations) {
        final StringBuilder violationMessage = new StringBuilder();
        for (Map.Entry<Class, List<Method>> violatedClass : violations.entrySet()) {
            if (violatedClass.getValue().size() == 0) {
                continue;
            }
            violationMessage.append("\n" + violatedClass.getKey().getName() + "\n");
            for (Method violatedMethod : violatedClass.getValue()) {
                violationMessage.append("        " + violatedMethod.getName() + "\n");
            }
        }

        return violationMessage.toString();
    }

}
