/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.validation;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.ConstantsWithLookup;
import com.google.gwt.i18n.client.ValidationMessages;
import com.google.gwt.validation.client.AbstractValidationMessageResolver;
import com.google.gwt.validation.client.UserValidationMessagesResolver;

/**
 * Defines validation messages resource.
 */
public class CustomValidationMessagesResolver extends AbstractValidationMessageResolver
        implements UserValidationMessagesResolver {

    /**
     * Creates CustomValidationMessagesResolver instance.
     */
    protected CustomValidationMessagesResolver() {
        super((ConstantsWithLookup) GWT.create(ValidationMessages.class));
    }
}