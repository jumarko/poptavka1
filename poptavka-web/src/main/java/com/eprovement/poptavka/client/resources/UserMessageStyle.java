package com.eprovement.poptavka.client.resources;

import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ClassName;

public interface UserMessageStyle extends CssResource {

    @ClassName("message")
    String message();

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

    @ClassName("reply-text-area")
    String replyTextArea();

    @ClassName("reply-button")
    String replyButton();

    @ClassName("messages-mine")
    String messagesMine();

    @ClassName("messages-received")
    String messagesReceived();

    @ClassName("message-unread")
    String messagesUnread();
}
