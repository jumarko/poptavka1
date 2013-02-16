/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.util.aop;

import org.springframework.aop.framework.Advised;

/**
 * Utility class containing usefull methods for handling adviced classes.
 * See <a href="http://en.wikipedia.org/wiki/Aspect-oriented_programming">Aspect oriented programming</a>.
 */
public final class AopUtils {

    private AopUtils() {
        throw new AssertionError("Utility class, DO NOT INSTANTIATE!");
    }

    /**
     * Unproxy given aop proxy and returns real object behind the proxy.
     * Usefull when we need to interact directly with real object (e.g. setting some mocked dependencies).
     * @param proxy
     * @param <T>
     * @return
     */
    public static <T> Object  unproxy(T proxy) {
        if (org.springframework.aop.support.AopUtils.isAopProxy(proxy) && proxy instanceof Advised) {
            Object target = null;
            try {
                target = ((Advised) proxy).getTargetSource().getTarget();
                return target;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        return null;
    }

}
