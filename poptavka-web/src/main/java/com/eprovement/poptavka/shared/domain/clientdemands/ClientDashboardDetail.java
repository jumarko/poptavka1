/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.clientdemands;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents all data required to build a dashboard for client on ClientDemands Welcome Page
 *
 * @author ivlcek
 */
public class ClientDashboardDetail implements IsSerializable {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long userId;
    private int unreadMessagesMyDemandsCount;
    private int unreadMessagesOfferedDemandsCount;
    private int unreadMessagesAssignedDemandsCount;
    private int unreadMessagesClosedDemandsCount;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public ClientDashboardDetail() {
        //for serialization
    }

    /**************************************************************************/
    /* Getters & Setters                                                      */
    /**************************************************************************/

    /**
     * @return the userId
     */
    public long getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(long userId) {
        this.userId = userId;
    }

    /**
     * @return the unreadMessagesMyDemandsCount
     */
    public int getUnreadMessagesMyDemandsCount() {
        return unreadMessagesMyDemandsCount;
    }

    /**
     * @param unreadMessagesMyDemandsCount the unreadMessagesMyDemandsCount to set
     */
    public void setUnreadMessagesMyDemandsCount(int unreadMessagesMyDemandsCount) {
        this.unreadMessagesMyDemandsCount = unreadMessagesMyDemandsCount;
    }

    /**
     * @return the unreadMessagesOfferedDemandsCount
     */
    public int getUnreadMessagesOfferedDemandsCount() {
        return unreadMessagesOfferedDemandsCount;
    }

    /**
     * @param unreadMessagesOfferedDemandsCount the unreadMessagesOfferedDemandsCount to set
     */
    public void setUnreadMessagesOfferedDemandsCount(int unreadMessagesOfferedDemandsCount) {
        this.unreadMessagesOfferedDemandsCount = unreadMessagesOfferedDemandsCount;
    }

    /**
     * @return the unreadMessagesAssignedDemandsCount
     */
    public int getUnreadMessagesAssignedDemandsCount() {
        return unreadMessagesAssignedDemandsCount;
    }

    /**
     * @param unreadMessagesAssignedDemandsCount the unreadMessagesAssignedDemandsCount to set
     */
    public void setUnreadMessagesAssignedDemandsCount(int unreadMessagesAssignedDemandsCount) {
        this.unreadMessagesAssignedDemandsCount = unreadMessagesAssignedDemandsCount;
    }

    /**
     * @return the unreadMessagesClosedDemandsCount
     */
    public int getUnreadMessagesClosedDemandsCount() {
        return unreadMessagesClosedDemandsCount;
    }

    /**
     * @param unreadMessagesClosedDemandsCount the unreadMessagesClosedDemandsCount to set
     */
    public void setUnreadMessagesClosedDemandsCount(int unreadMessagesClosedDemandsCount) {
        this.unreadMessagesClosedDemandsCount = unreadMessagesClosedDemandsCount;
    }
}
