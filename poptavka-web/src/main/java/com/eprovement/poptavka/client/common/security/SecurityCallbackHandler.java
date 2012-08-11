package com.eprovement.poptavka.client.common.security;

public interface SecurityCallbackHandler {

    void onAuthorizationExpected(final String externalLoginUrl);

    void onAccessDenied();
}