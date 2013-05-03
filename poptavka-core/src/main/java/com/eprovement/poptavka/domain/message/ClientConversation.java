package com.eprovement.poptavka.domain.message;

import com.eprovement.poptavka.domain.user.BusinessUser;

/**
 *
 * @author Vojtech Hubr
 */
public class ClientConversation {
    private UserMessage latestUserMessage;
    private BusinessUser supplier;
    private int messageCount;

    public ClientConversation(UserMessage latestUserMessage, BusinessUser supplier,
            int messageCount) {
        this.latestUserMessage = latestUserMessage;
        this.supplier = supplier;
        this.messageCount = messageCount;
    }

    public UserMessage getLatestUserMessage() {
        return latestUserMessage;
    }

    public void setLatestUserMessage(UserMessage latestUserMessage) {
        this.latestUserMessage = latestUserMessage;
    }

    public BusinessUser getSupplier() {
        return supplier;
    }

    public void setSupplier(BusinessUser supplier) {
        this.supplier = supplier;
    }

    public int getMessageCount() {
        return messageCount;
    }

    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 17 * hash + (this.latestUserMessage != null ? this.latestUserMessage.hashCode() : 0);
        hash = 17 * hash + (this.supplier != null ? this.supplier.hashCode() : 0);
        hash = 17 * hash + this.messageCount;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ClientConversation other = (ClientConversation) obj;
        if (this.latestUserMessage != other.latestUserMessage
                && (this.latestUserMessage == null
                || !this.latestUserMessage.equals(other.latestUserMessage))) {
            return false;
        }
        if (this.supplier != other.supplier && (this.supplier == null
                || !this.supplier.equals(other.supplier))) {
            return false;
        }
        if (this.messageCount != other.messageCount) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "ClientsConversation{" + "latestUserMessage=" + latestUserMessage
                + ", with supplier=" + supplier + ", messageCount="
                + messageCount + '}';
    }
}
