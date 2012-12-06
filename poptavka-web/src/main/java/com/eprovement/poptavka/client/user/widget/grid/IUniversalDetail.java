/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.widget.grid;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import java.util.Date;

/**
 * This interface represent universal detail used in UniversalTableWidget.
 * It contains all needed method definitions for all possible cases.
 * Each detail object what is going to be used with UniversalTableWidget must implement it.
 *
 * @author Martin Slavkovsky
 */
public interface IUniversalDetail {

    // Client part
    //--------------------------------------------------------------------------
    long getClientId();

    String getClientName();

    // Supplier part
    //--------------------------------------------------------------------------
    long getSupplierId();

    String getSupplierName();

    int getRating();

    // Message part
    //--------------------------------------------------------------------------
    long getMessageId();

    long getThreadRootId();

    long getSenderId();

    Date getMessageSent();

    // UserMessage part
    //--------------------------------------------------------------------------
    long getUserMessageId();

    boolean isRead();

    void setRead(boolean isRead);

    boolean isStarred();

    void setStarred(boolean isStarred);

    int getMessageCount();

    int getUnreadMessageCount();

    Date getDeliveryDate();

    // Demand part
    //--------------------------------------------------------------------------
    long getDemandId();

    Date getValidTo();

    Date getEndDate();

    Date getReceivedDate();

    String getTitle();

    String getPrice();

    DemandStatus getDemandStatus();

    // Offer part
    //--------------------------------------------------------------------------
    OfferStateType getOfferState();

    // Display
    //--------------------------------------------------------------------------
    /**
     * Display string as HTML. We suppose calling of this method always come from trusted (programmed) source.
     * User CANNOT call this method due to security issues.
     * @param trustedHtml
     * @return string in html tags
     */
    String displayHtml(String trustedHtml, boolean isRead);
}
