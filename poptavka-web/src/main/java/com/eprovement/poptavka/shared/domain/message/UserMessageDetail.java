/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.shared.search.ISortField;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

/**
 *
 * @author Martin Slavkovsky
 */
public class UserMessageDetail implements IsSerializable {

    public enum UserMessageField implements ISortField {

        STARRED("starred");

        private String value;

        private UserMessageField(String value) {
            this.value = value;
        }

        @Override
        public String getValue() {
            return value;
        }
    }
    private Long id;
    private boolean isRead;
    private boolean isStarred;
    private MessageDetail messageDetail;
    private String senderEmail;
    private int messageCount;

    public static final ProvidesKey<UserMessageDetail> KEY_PROVIDER = new ProvidesKey<UserMessageDetail>() {
        @Override
        public Object getKey(UserMessageDetail item) {
            return item == null ? null : item.getId();
        }
    };

    /**************************************************************************/
    /* Constructora                                                           */
    /**************************************************************************/
    public UserMessageDetail() {
        // for serialization
    }

    /**************************************************************************/
    /* Getter & Setters                                                       */
    /**************************************************************************/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isStarred() {
        return isStarred;
    }

    public void setStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public MessageDetail getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(MessageDetail messageDetail) {
        this.messageDetail = messageDetail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public String toString() {
        return "read: " + isRead
                + "starred: " + isStarred
                + "messageDetail" + messageDetail.toString();
    }
}
