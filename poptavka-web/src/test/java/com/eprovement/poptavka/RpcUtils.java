/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka;

import com.eprovement.poptavka.util.reflection.ClassFilter;
import com.eprovement.poptavka.util.reflection.ReflectionUtils;
import com.google.gwt.user.client.rpc.RemoteService;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
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






    private RpcUtils() {
        // utility class - DO NOT INSTANTIATE
    }

}
