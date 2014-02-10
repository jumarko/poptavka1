/*
 * Copyright (C) 2007-2011, GoodData(R) Corporation. All rights reserved.
 */
package com.eprovement.poptavka.rest.exception;

import com.eprovement.poptavka.exception.DomainObjectNotFoundException;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ExceptionHandlerExceptionResolver;
import org.springframework.web.servlet.mvc.method.annotation.ServletInvocableHandlerMethod;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * Custom exception handler for translating all uncaught exceptions thrown from rest api controllers.
 */
public class RestExceptionResolver extends ExceptionHandlerExceptionResolver
        implements HandlerExceptionResolver, Ordered {

    public int getOrder() {
        return Integer.MIN_VALUE; // we're first in line, yay!
    }

    @Override
    protected ServletInvocableHandlerMethod getExceptionHandlerMethod(
            HandlerMethod handlerMethod, Exception exception) {
        try {
            return new ServletInvocableHandlerMethod(this, getClass().getMethod("handleException", Exception.class));
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Handles given exception by returning proper response entity for it.
     * Generally, this implementation tries to find proper response status code for given exception
     * while using exception's message as response body.
     * @param e exception to be handled
     * @return response body as a representation of exception for remote client
     */
    @org.springframework.web.bind.annotation.ExceptionHandler
    public ResponseEntity<String> handleException(Exception e) {
        HttpStatus responseStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String responseBody = e.getMessage();
        final ResponseStatus responseStatusAnnotation =
                AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class);
        if (responseStatusAnnotation != null) {
            responseStatus = responseStatusAnnotation.value();
        } else if (e instanceof IllegalArgumentException) {
            responseStatus = HttpStatus.BAD_REQUEST;
            responseBody = e.getMessage();
        } else if (e instanceof ConstraintViolationException) {
            responseStatus = HttpStatus.BAD_REQUEST;
            responseBody = getResponseBodyForViolationException((ConstraintViolationException) e);
        } else if (e instanceof DomainObjectNotFoundException) {
            final DomainObjectNotFoundException dnfe = (DomainObjectNotFoundException) e;
            responseStatus = HttpStatus.NOT_FOUND;
            responseBody = String.format("No object with id=%s of class=%s has been found",
                    dnfe.getId(), dnfe.getDomainObjectClass().getSimpleName());
        }
        logger.error("action=rest_exception_handled", e);
        return new ResponseEntity<>(responseBody, responseStatus);
    }

    private String getResponseBodyForViolationException(ConstraintViolationException violationException) {
        final Set<ConstraintViolation<?>> constraintViolations = violationException.getConstraintViolations();
        if (CollectionUtils.isNotEmpty(constraintViolations)) {
            final ConstraintViolation<?> violation = constraintViolations.iterator().next();
            return String.format("propertyPath='%s', message='%s'",
                    violation.getPropertyPath(), violation.getMessage());
        }
        return "";
    }
}