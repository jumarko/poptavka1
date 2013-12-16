/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.detail;

import com.google.gwt.user.client.ui.Widget;

/**
 *
 * @author Martin Slavkovsky
 */
public final class DetailModuleBuilder {

    /**************************************************************************/
    /* Constants                                                              */
    /**************************************************************************/
    public static final int DEMAND_DETAIL_TAB = 0;
    public static final int USER_DETAIL_TAB = 1;
    public static final int RATING_TAB = 2;
    public static final int CONVERSATION_TAB = 3;
    public static final int ADVERTISEMENT_TAB = 4;
    public static final int CUSTOM_TAB = 5;

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private boolean demandTab;
    private boolean userTab;
    private boolean conversationTab;
    private boolean advertisementTab;
    private boolean ratingTab;
    private long demandId;
    private long userId;
    private long threadRootId;
    private long senderId;
    private Widget advertisementWidget;
    private int selectTabIdx;

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public boolean isDemandTab() {
        return demandTab;
    }

    public boolean isUserTab() {
        return userTab;
    }

    public boolean isConversationTab() {
        return conversationTab;
    }

    public boolean isAdvertisementTab() {
        return advertisementTab;
    }

    public boolean isRatingTab() {
        return ratingTab;
    }

    public long getDemandId() {
        return demandId;
    }

    public long getUserId() {
        return userId;
    }

    public long getThreadRootId() {
        return threadRootId;
    }

    public long getSenderId() {
        return senderId;
    }

    public Widget getAdvertisementWidget() {
        return advertisementWidget;
    }

    public int getSelectedTabIdx() {
        return selectTabIdx;
    }

    /**************************************************************************/
    /* Builder                                                                */
    /**************************************************************************/
    public static class Builder {

        private boolean demandTab;
        private boolean userTab;
        private boolean conversationTab;
        private boolean advertisementTab;
        private boolean ratingTab;
        private long demandId;
        private long userId;
        private long threadRootId;
        private long senderId;
        private Widget advertisementWidget;
        private int selectTabIdx;

        public Builder() {
        }

        public Builder addDemandTab(long demandId) {
            this.demandTab = true;
            this.demandId = demandId;
            return this;
        }

        public Builder addUserTab(long supplierId) {
            this.userTab = true;
            this.userId = supplierId;
            return this;
        }

        public Builder addConversationTab(long threadRootId, long senderId) {
            this.conversationTab = true;
            this.threadRootId = threadRootId;
            this.senderId = senderId;
            return this;
        }

        public Builder addRatingTab(long demandId) {
            this.ratingTab = true;
            this.demandId = demandId;
            return this;
        }

        public Builder addAdvertisementTab(long demandId, Widget advertisementWidget) {
            this.advertisementTab = true;
            this.advertisementWidget = advertisementWidget;
            return this;
        }

        public Builder selectTab(int tabIdx) {
            this.selectTabIdx = tabIdx;
            return this;
        }

        public DetailModuleBuilder build() {
            return new DetailModuleBuilder(this);
        }
    }

    /**************************************************************************/
    /* Constructor                                                            */
    /**************************************************************************/
    private DetailModuleBuilder(Builder builder) {

        this.demandTab = builder.demandTab;
        this.userTab = builder.userTab;
        this.conversationTab = builder.conversationTab;
        this.advertisementTab = builder.advertisementTab;
        this.ratingTab = builder.ratingTab;
        this.demandId = builder.demandId;
        this.userId = builder.userId;
        this.threadRootId = builder.threadRootId;
        this.senderId = builder.senderId;
        this.advertisementWidget = builder.advertisementWidget;
        this.selectTabIdx = builder.selectTabIdx;
    }
}
