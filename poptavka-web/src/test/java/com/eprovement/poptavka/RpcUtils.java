/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka;

import com.eprovement.poptavka.util.reflection.ClassFilter;
import com.eprovement.poptavka.util.reflection.ReflectionUtils;
import com.google.gwt.user.client.rpc.RemoteService;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import junit.framework.Assert;
import org.springframework.core.type.ClassMetadata;

/**
 * Handy utility class for testing RPC classes.
 * E.g. it can find all RPC classes in project.
 */
public final class RpcUtils {
    private static final String CLIENT_SERVICE_PACKAGE_NAME = "com.eprovement.poptavka.client.service";
    private static final String SERVER_PACKAGE_NAME = "com.eprovement.poptavka.server";

    private static final ClassFilter RPC_CLASS_FILTER = new ClassFilter() {
        private boolean isRpcServiceClass(String className) {
            try {
                return RemoteService.class.isAssignableFrom(Class.forName(className));
            } catch (Throwable e) {
                e.printStackTrace();
                return false;
            }
        }
        @Override
        public boolean match(ClassMetadata classMetadata) {
            return isRpcServiceClass(classMetadata.getClassName());
        }
    };
    private static final Set<Class> RPC_CLASSES = new HashSet<Class>() { {
            addAll(ReflectionUtils.findClasses(SERVER_PACKAGE_NAME, RPC_CLASS_FILTER));
            addAll(ReflectionUtils.findClasses(CLIENT_SERVICE_PACKAGE_NAME, RPC_CLASS_FILTER));
        }
    };


    /**
     * Common method for finding all RPC classes which can be introspected byt some unit tests.
     * @return set of all RPC classes in project.
     */
    public static Set<Class> getRpcClasses() {
        return Collections.unmodifiableSet(RPC_CLASSES);
    }

    public static void reportViolations(Map<Class, List<Method>> violations, String baseMessage) {
        for (Map.Entry<Class, List<Method>> violation : violations.entrySet()) {
            if (violation.getValue().size() > 0) {
                Assert.fail(baseMessage
                        + "\nPlease, check following methods!\n" + RpcUtils.formatViolationsMessage(violations));
                break;
            }
        }
    }




    public static String formatViolationsMessage(Map<Class, List<Method>> violations) {
        final StringBuilder violationMessage = new StringBuilder();
        for (Map.Entry<Class, List<Method>> violatedClass : violations.entrySet()) {
            if (violatedClass.getValue().size() == 0) {
                continue;
            }
            violationMessage.append("\n");
            violationMessage.append(violatedClass.getKey().getName());
            violationMessage.append("\n");
            for (Method violatedMethod : violatedClass.getValue()) {
                violationMessage.append("        ");
                violationMessage.append(violatedMethod.getName());
                violationMessage.append("\n");
            }
        }

        return violationMessage.toString();
    }

    public interface MethodRule {
        /**
         * Checks given method and return appropriate result.
         * @param method
         * @return false, if method violates any rule, true otherwise
         */
        boolean checkMethod(Method method);
    }



    public static Map<Class, List<Method>> checkAllPublicRpcMethods(MethodRule methodRule) {
        final Set<Class> rpcClasses = RpcUtils.getRpcClasses();
        final Map<Class, List<Method>> violations = new HashMap<Class, List<Method>>();
        for (Class rpcClass : rpcClasses) {
            // initial violations are empty for each class
            violations.put(rpcClass, new ArrayList<Method>());

            for (Method method : rpcClass.getDeclaredMethods()) {
                if (! isPublicRpcMethod(method)) {
                    continue;
                }
                if (! methodRule.checkMethod(method)) {
                    addViolation(violations, rpcClass, method);
                }
            }
        }
        return violations;
    }


    private static boolean isPublicRpcMethod(Method method) {
        if (method.getName().equals("readResolve")) {
            // not sure why readResolve is presented in all RPC service classes (maybe aspectj stuff)
            return false;
        }

        // each other public method is considered to be regular RPC method used on client
        return !method.isSynthetic() && Modifier.isPublic(method.getModifiers());
    }



    private static void addViolation(Map<Class, List<Method>> violations, Class rpcClass, Method method) {
        violations.get(rpcClass).add(method);
    }



    private RpcUtils() {
        // utility class - DO NOT INSTANTIATE
    }

}
