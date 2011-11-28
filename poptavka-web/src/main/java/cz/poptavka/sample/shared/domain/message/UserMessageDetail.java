/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.message;

import cz.poptavka.sample.domain.message.UserMessage;
import java.io.Serializable;

/**
 *
 * @author Martin Slavkovsky
 */
public class UserMessageDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -928374659233195109L;
    private boolean isRead;
    private boolean isStarred;
    private MessageDetail messageDetail;

    public UserMessageDetail() {
    }

    public UserMessageDetail(UserMessageDetail detail) {
        this.updateWholeUserMessage(detail);
    }

    public static UserMessageDetail createUserMessageDetail(UserMessage userMessage) {
        return fillUserMessageDetail(new UserMessageDetail(), userMessage);
    }

    public static UserMessageDetail fillUserMessageDetail(UserMessageDetail detail, UserMessage userMessage) {
        detail.setIsRead(userMessage.isIsRead());
        detail.setIsStarred(userMessage.isIsStarred());
        detail.setMessageDetail(MessageDetail.createMessageDetail(userMessage.getMessage()));

        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeUserMessage(UserMessageDetail detail) {
        isRead = detail.isRead();
        isStarred = detail.isIsStarred();
        messageDetail = detail.getMessageDetail();
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public MessageDetail getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(MessageDetail messageDetail) {
        this.messageDetail = messageDetail;
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
}
