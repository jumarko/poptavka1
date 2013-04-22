/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import java.math.BigDecimal;
import java.util.Date;

/**
 * This interface represent universal detail used in UniversalTableWidget.
 * It contains all needed method definitions for all possible cases.
 * Each detail object what is going to be used with UniversalTableWidget must implement it.
 *
 * @author Martin Slavkovsky
 */
public interface IUniversalDetail extends TableDisplayRating, TableDisplayDisplayName {

    // Client part & Supplier part
    //--------------------------------------------------------------------------
    long getClientId();

    long getSupplierId();

    // Message part
    //--------------------------------------------------------------------------
    long getMessageId();

    long getThreadRootId();

    long getSenderId();

    Date getMessageSent();

    // UserMessage part
    //--------------------------------------------------------------------------
    long getUserMessageId();

    void setUserMessageId(long userMessageId);

    boolean isStarred();

    boolean isRead();

    void setIsStarred(boolean isStarred);

    void setIsRead(boolean isStarred);

    int getMessageCount();

    void setMessageCount(int messageCount);

    Date getDeliveryDate();

    // Demand part
    //--------------------------------------------------------------------------
    long getDemandId();

    Date getValidTo();

    Date getEndDate();

    Date getReceivedDate();

    String getTitle();

    BigDecimal getPrice();

    // Offer part
    //--------------------------------------------------------------------------
    long getOfferId();
}
