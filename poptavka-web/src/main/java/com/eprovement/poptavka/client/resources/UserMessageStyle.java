package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;

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

    @ClassName("reply-message-arrow-border")
    String replyMessageArrowBorder();

    @ClassName("reply-message-arrow")
    String replyMessageArrow();

    @ClassName("reply-text-area")
    String replyTextArea();
}
