/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.resources;

import com.google.gwt.resources.client.CssResource;

/**
 *
 * @author slavkovsky.martin, Jaro
 */
public interface DetailViews extends CssResource {
    /* COMMON DETAIL STYLES */
    @ClassName("common-detail-container")
    String commonDetailContainer();

    /* DEMANDS DETAIL SECTION */
    @ClassName("detail-block")
    String detailBlock();

    @ClassName("detail-header")
    String detailHeader();

    @ClassName("demand-detail-content")
    String demandDetailContent();

    @ClassName("detail-block-label")
    String detailBlockLabel();

    @ClassName("detail-strong")
    String detailStrong();

    @ClassName("detail-date-label")
    String detailDateLabel();

    @ClassName("detail-category-label")
    String detailCategoryLabel();

    @ClassName("detail-locality-label")
    String detailLocalityLabel();

    @ClassName("demandDetailSectionHeader")
    String demandDetailSectionHeader();

    /* CONVERSATION DETAIL SECTION */
    @ClassName("conversation-container")
    String conversationContainer();

    @ClassName("conversation-detail")
    String conversationDetail();

    @ClassName("conversation-block")
    String conversationBlock();

    @ClassName("conversation-detail-green")
    String conversationDetailGreen();

    @ClassName("conversation-detail-red")
    String conversationDetailRed();

    @ClassName("conversation-detail-header")
    String conversationDetailHeader();

    @ClassName("conversation-detail-header-green")
    String conversationDetailHeaderGreen();

    @ClassName("conversation-detail-header-red")
    String conversationDetailHeaderRed();

    @ClassName("conversation-detail-time")
    String conversationDetailTime();

    @ClassName("conversation-detail-content")
    String conversationDetailContent();

    @ClassName("conversation-left-block")
    String conversationLeftBlock();

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

    @ClassName("close-button")
    String closeButton();

    @ClassName("messages-mine")
    String messagesMine();

    @ClassName("messages-received")
    String messagesReceived();

    @ClassName("message-unread")
    String messagesUnread();

    @ClassName("response-container")
    String responseContainer();
}
