package com.eprovement.poptavka.client.root.interfaces;

import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.SimplePanel;

public interface IRootModule {

    String CUSTOM_TOKEN = "view";
    String UNSUBSCRIBE_TOKEN = "unsubscribe";
    String PAYMENT_TOKEN = "payment";
    String LOGIN_TOKEN = "login";

    public interface Handler {

        void onUnsubscribeUser(String password);
    }

    public interface Presenter extends HandleResizeEvent {
    }

    public interface View extends IsWidget {

        SimplePanel getHeader();

        SimplePanel getToolbar();

        SimplePanel getBody();
    }
}
