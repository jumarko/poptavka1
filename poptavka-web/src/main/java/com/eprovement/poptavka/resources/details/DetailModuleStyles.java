/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.resources.details;

import com.google.gwt.resources.client.CssResource;

/**
 * Defines styles for Detail module views.
 *
 * @author Jaro
 */
public interface DetailModuleStyles extends CssResource {

    /* Detail Tab Panel Styles */
    @ClassName("detailTabPanel")
    String detailTabPanel();

    @ClassName("empty-view")
    String emptyView();

    /* Demand Detail Styles */
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

    @ClassName("rating-container")
    String ratingContainer();

    @ClassName("detail-locality-label")
    String detailLocalityLabel();

    @ClassName("demandDetailSectionHeader")
    String demandDetailSectionHeader();

    /* Conversation Detail Styles */
    @ClassName("conversation-container")
    String conversationContainer();

    @ClassName("conversation-detail")
    String conversationDetail();

    @ClassName("conversation-block")
    String conversationBlock();

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

    @ClassName("conversation-ellipsis")
    String conversationEllipsis();

    @ClassName("message-container")
    String messageContainer();

    @ClassName("message-opened")
    String messageOpened();

    @ClassName("message-buttons-container")
    String messageButtonsContainer();

    @ClassName("message-body")
    String messageBody();

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

    @ClassName("detail-description-content")
    String detailDescriptionContent();
}
