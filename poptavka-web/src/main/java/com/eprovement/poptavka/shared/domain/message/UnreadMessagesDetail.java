/*
 * This object will store information that will be displayed in the header where user can always see them.
 * Information will include number of unread messages for this user, etc.
 */
package com.eprovement.poptavka.shared.domain.message;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *
 * @author ivlcek
 */
public class UnreadMessagesDetail implements IsSerializable {

    private int unreadMessagesCount;

    public UnreadMessagesDetail() {
        // empty
    }

    /**
     * @return the unreadMessagesCount
     */
    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    /**
     * @param unreadMessagesCount the unreadMessagesCount to set
     */
    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

}
