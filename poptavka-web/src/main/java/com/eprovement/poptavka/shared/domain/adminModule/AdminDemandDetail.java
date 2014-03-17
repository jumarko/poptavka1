/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.domain.adminModule;

import com.eprovement.poptavka.client.user.widget.grid.TableDisplayUserMessage;
import com.eprovement.poptavka.client.user.widget.grid.columns.CreatedDateColumn.TableDisplayCreatedDate;
import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.LocalityColumn.TableDisplayLocality;
import com.eprovement.poptavka.client.user.widget.grid.columns.UrgencyColumn.TableDisplayValidTo;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import java.util.ArrayList;
import java.util.Date;

/**
 *
 * @author Martin Slavkovsky
 * @since 17.3.2014
 */
public class AdminDemandDetail implements IsSerializable, TableDisplayCreatedDate,
    TableDisplayDemandTitle, TableDisplayLocality, TableDisplayValidTo, TableDisplayDetailModule,
    TableDisplayUserMessage {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    private long userMessageId;
    private Date created;
    private String demandTitle;
    private int messagesCount;
    private int unreadMessagesCount;
    private ArrayList<ICatLocDetail> localities;
    private Date validTo;
    private long demandId;
    private long supplierId;
    private long threadRootId;
    private long senderId;
    private boolean starred;
    private boolean read;

    public static final ProvidesKey<AdminDemandDetail> KEY_PROVIDER
        = new ProvidesKey<AdminDemandDetail>() {
            @Override
            public Object getKey(AdminDemandDetail item) {
                return item == null ? null : item.getUserMessageId();
            }
        };

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    /**************************************************************************/
    public long getUserMessageId() {
        return userMessageId;
    }

    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    public int getUnreadMessagesCount() {
        return unreadMessagesCount;
    }

    @Override
    public int getMessagesCount() {
        return messagesCount;
    }

    @Override
    public void setMessagesCount(int messageCount) {
        this.messagesCount = messageCount;
    }

    public void setUnreadMessagesCount(int unreadMessagesCount) {
        this.unreadMessagesCount = unreadMessagesCount;
    }

    public ArrayList<ICatLocDetail> getLocalities() {
        return localities;
    }

    public void setLocalities(ArrayList<ICatLocDetail> localities) {
        this.localities = localities;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getThreadRootId() {
        return threadRootId;
    }

    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

}
