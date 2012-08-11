/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.util.reflection;

import org.springframework.core.type.ClassMetadata;

public interface ClassFilter {

    /**
     * Makes a decision whether given class matches required condition.
     * @param classMetadata
     * @return true if class matches false otherwise
     */
    boolean match(ClassMetadata classMetadata);
}
