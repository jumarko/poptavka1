/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.security;

public interface SecurityCallbackHandler {

    void onAuthorizationExpected(String messageForUser);

    void onAccessDenied(String errorId);
}