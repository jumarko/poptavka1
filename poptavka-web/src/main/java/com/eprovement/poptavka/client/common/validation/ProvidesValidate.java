/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.validation;

/**
 * Implements in widget's view in order to use validation on widget's compontents.
 *
 * @author Martin Slavkovsky
 */
public interface ProvidesValidate {

    /**
     * Validates view's components.
     * @return true if valid, false otherwise.
     */
    boolean isValid();

    /**
     * Resets view's components.
     * Clears them or sets to default values.
     */
    void reset();
}
