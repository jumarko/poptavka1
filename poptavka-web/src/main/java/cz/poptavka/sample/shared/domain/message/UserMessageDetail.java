/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.message;

import com.google.gwt.view.client.ProvidesKey;
import cz.poptavka.sample.client.user.messages.tab.MessageTableDisplay;
import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.shared.domain.converter.MessageConverter;
import java.io.Serializable;

/**
 *
 * @author Martin Slavkovsky
 */
public class UserMessageDetail implements Serializable, MessageTableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -928374659233195109L;
    private Long id;
    private boolean isRead;
    private boolean isStarred;
    private MessageDetail messageDetail;
    private String senderEmail;

    private int messageCount;
    private int unreadMessageCount;

    public UserMessageDetail() {
    }

    public UserMessageDetail(UserMessageDetail detail) {
        this.updateWholeUserMessage(detail);
    }

    public static UserMessageDetail createUserMessageDetail(UserMessage userMessage) {
        UserMessageDetail detail = new UserMessageDetail();
        detail.setId(userMessage.getId());
        detail.setRead(userMessage.isRead());
        detail.setStarred(userMessage.isStarred());
        detail.setMessageDetail(new MessageConverter().convertToTarget(userMessage.getMessage()));
        detail.setSenderEmail(userMessage.getMessage().getSender().getEmail()); //User().getEmail());

        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeUserMessage(UserMessageDetail detail) {
        id = detail.getId();
        isRead = detail.isRead();
        isStarred = detail.isStarred();
        messageDetail = detail.getMessageDetail();
        senderEmail = detail.getSenderEmail();
    }

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

    @Override
    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    @Override
    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    @Override
    public int getUnreadMessageCount() {
        return unreadMessageCount;
    }

    @Override
    public void setUnreadMessageCount(int unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    @Override
    public String getFormattedMessageCount() {
        return "(" + getMessageCount() + "/"
                + getUnreadMessageCount() + ")";
    }

    public String toString() {
        return "read: " + isRead
                + "starred: " + isStarred
                + "messageDetail" + messageDetail.toString();
    }
    //    public MessageType getMessageType() {
    //        return MessageType.CONVERSATION;
    //    }
    private static final String HTML_UNREAD_START = "<strong>";
    private static final String HTML_UNREAD_END = "</strong>";

    /**
     * Display string as HTML. We suppose calling of this method always come from trusted (programmed) source.
     * User CANNOT call this nethod due to security issues.
     * @param trustedHtml
     * @return string in html tags
     */
    public static String displayHtml(String trustedHtml, boolean isRead) {
        if (isRead) {
            return trustedHtml;
        } else {
            return HTML_UNREAD_START + trustedHtml + HTML_UNREAD_END;
        }
    }
    public static final ProvidesKey<UserMessageDetail> KEY_PROVIDER = new ProvidesKey<UserMessageDetail>() {

        @Override
        public Object getKey(UserMessageDetail item) {
            return item == null ? null : item.getId();
        }
    };
}
