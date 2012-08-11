package com.eprovement.poptavka.client.common.login;

public interface LoginUICapabilities {

    String getLoginValue();

    String getPasswordValue();

    void setErrorMessage(final String message);

    void hide();
}