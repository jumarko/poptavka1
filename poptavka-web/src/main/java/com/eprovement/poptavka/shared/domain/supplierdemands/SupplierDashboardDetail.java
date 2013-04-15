/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.supplierdemands;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents all data required to build a dashboard for supplier on SupplierDemands Welcome Page
 *
 * @author ivlcek
 */
public class SupplierDashboardDetail implements IsSerializable {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long userId;
    private int unreadMessagesPotentialDemandsCount;
    private int unreadMessagesOfferedDemandsCount;
    private int unreadMessagesAssignedDemandsCount;
    private int unreadMessagesClosedDemandsCount;

    /**************************************************************************/
    /* Constructors                                                           */
    /**************************************************************************/
    public SupplierDashboardDetail() {
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
     * @return the unreadMessagesPotentialDemandsCount
     */
    public int getUnreadMessagesPotentialDemandsCount() {
        return unreadMessagesPotentialDemandsCount;
    }

    /**
     * @param unreadMessagesPotentialDemandsCount the unreadMessagesPotentialDemandsCount to set
     */
    public void setUnreadMessagesPotentialDemandsCount(int unreadMessagesPotentialDemandsCount) {
        this.unreadMessagesPotentialDemandsCount = unreadMessagesPotentialDemandsCount;
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
