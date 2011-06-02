package cz.poptavka.sample.client.resources;

import com.google.gwt.resources.client.CssResource;

public interface UserMessageStyle extends CssResource {

    @ClassName("message")
    String message();

    @ClassName("message-first")
    String messageFirst();

    @ClassName("message-last")
    String messageLast();

    @ClassName("message-opened")
    String messageOpened();

    @ClassName("message-header")
    String messageHeader();

    @ClassName("message-body")
    String messageBody();

    @ClassName("action-button")
    String actionButton();

    @ClassName("wrapper")
    String wrapper();
}
