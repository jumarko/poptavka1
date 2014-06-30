/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.common.session;

import com.eprovement.poptavka.resources.StyleResource;
import com.eprovement.poptavka.resources.datagrid.DataGridResources;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.i18n.client.ValidationMessages;

/**
 * Global controller for events such as loading and stuff like that.
 * Basically it§s a copy of some methods of MainPresenter to make it more
 * readable.
 *
 * @author beho
 *
 */
public final class Storage {

    private static final Storage INSTANCE = new Storage();

    public static Storage get() {
        return INSTANCE;
    }
    // Value is set on each new module load. To prevent any further complications, set this value
    // using SearchModulePresenter constants.
    private static int currentlyLoadedView = -1;
    /**************************************************************************/
    /* History attributes                                                     */
    /**************************************************************************/
    private static boolean loginDueToHistory = false;
    private static boolean logoutDueToHistory = false;
    private static boolean calledDueToHistory = false;
    private static String forwardHistory = "";
    private static Boolean appCalledByURL = null;
    private DateTimeFormat dateTimeFormat;
    /**************************************************************************/
    //global constants
    public static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    public static final ValidationMessages VMSGS = GWT.create(ValidationMessages.class);
    public static final DataGridResources GRSCS = GWT.create(DataGridResources.class);
    public static final StyleResource RSCS = GWT.create(StyleResource.class);
    //Define own custom currency number formater because we don't want cents to be displayed.
    //Otherwise just use NumberFormat.getCurrencyFormater() to format currency numbers;
    public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getFormat(MSGS.formatCurrency());
    public static final String SUP_NEW_DETAIL_DEMAND_ID = "supDemDet";
    //local constants
    private static UserDetail userDetail = null;
    private static BusinessUserDetail businessUserDetail = null;
    //client projects - selected demand
    private static long demandId = -1L;
    private static long threadRootId = -1L;
    private static long clientId;
    private static long supplierId;

    /**************************************************************************/
    /* History methods                                                        */
    /* TODO Martin - refactor, not used any more                              */
    /**************************************************************************/
    public static boolean isLogoutDueToHistory() {
        return logoutDueToHistory;
    }

    public static void setLogoutDueToHistory(boolean logoutDueToHistory) {
        Storage.logoutDueToHistory = logoutDueToHistory;
    }

    public static boolean isLoginDueToHistory() {
        return loginDueToHistory;
    }

    public static void setLoginDueToHistory(boolean loginDueToHistory) {
        Storage.loginDueToHistory = loginDueToHistory;
    }

    public static boolean isCalledDueToHistory() {
        return calledDueToHistory;
    }

    public static void setCalledDueToHistory(boolean calledDueToHistory) {
        Storage.calledDueToHistory = calledDueToHistory;
    }

    public static String getForwardHistory() {
        return forwardHistory;
    }

    public static void setForwardHistory(String forwardHistory) {
        Storage.forwardHistory = forwardHistory;
    }

    /**************************************************************************/
    /* Date time formater initialization                                      */
    /**************************************************************************/
    /**
     * Inits date time format.
     * Differ between <b>long</b>(large screens) and <b>short</b>(tiny-middle screens).
     *
     * @param longFormat true if long format should be used, false for short one
     */
    public void initDateTimeFormat(boolean longFormat) {
        if (longFormat) {
            dateTimeFormat = DateTimeFormat.getFormat(MSGS.formatDateLong());
        } else {
            dateTimeFormat = DateTimeFormat.getFormat(MSGS.formatDateMiddle());
        }
    }

    /**
     * @return the date time format
     */
    public DateTimeFormat getDateTimeFormat() {
        return dateTimeFormat;
    }

    /**************************************************************************/
    /* User related                                                           */
    /**************************************************************************/
    /**
     * @return the businessUserDetail
     */
    public static BusinessUserDetail getBusinessUserDetail() {
        return businessUserDetail;
    }

    /**
     * @param aBusinessUserDetail the businessUserDetail to set
     */
    public static void setBusinessUserDetail(BusinessUserDetail aBusinessUserDetail) {
        businessUserDetail = aBusinessUserDetail;
    }

    /**
     * @return the user detail
     */
    public static UserDetail getUser() {
        return userDetail;
    }

    /**
     * Sets user detail object for global access.
     * @param aUserDetail
     */
    public static void setUserDetail(UserDetail aUserDetail) {
        userDetail = aUserDetail;
    }

    /**************************************************************************/
    /* Conversation related                                                   */
    /* TODO LATER Martin - refactor, not sure why it must be here             */
    /**************************************************************************/
    /**
     * @return the threadRootId
     */
    public static long getThreadRootId() {
        return threadRootId;
    }

    /**
     * @param aThreadRootId the threadRootId to set
     */
    public static void setThreadRootId(long aThreadRootId) {
        threadRootId = aThreadRootId;
    }

    /**
     * @return the clientId
     */
    public static long getClientId() {
        return clientId;
    }

    /**
     * @param aClientId the clientId to set
     */
    public static void setClientId(long aClientId) {
        clientId = aClientId;
    }

    /**
     * @return the supplierId
     */
    public static long getSupplierId() {
        return supplierId;
    }

    /**
     * @param aSupplierId the supplierId to set
     */
    public static void setSupplierId(long aSupplierId) {
        supplierId = aSupplierId;
    }

    public static long getDemandId() {
        return demandId;
    }

    public static void setDemandId(long demandId) {
        Storage.demandId = demandId;
    }

    public static Boolean isAppCalledByURL() {
        return appCalledByURL;
    }

    public static void setAppCalledByURL(Boolean appCalledByURL) {
        Storage.appCalledByURL = appCalledByURL;
    }

    /**************************************************************************/
    /* Widgets related                                                        */
    /**************************************************************************/
    /**
     * @return the current widget id
     */
    public static int getCurrentlyLoadedView() {
        return currentlyLoadedView;
    }

    /**
     * Set value currentlyLoadedView to actual loaded widget table. Used for SearchModule.
     * If widget contains no table, set value to NULL.
     * @param currentlyLoadedView
     */
    public static void setCurrentlyLoadedView(int currentlyLoadedView) {
        Storage.currentlyLoadedView = currentlyLoadedView;
    }

    /**************************************************************************/
    /* Other                                                                  */
    /**************************************************************************/
    /**
     * Method clears all data of this Storage session object.
     */
    public static void invalidateStorage() {
        setUserDetail(null);
        setBusinessUserDetail(null);
        setAppCalledByURL(null);
        setDemandId(-1L);
        setClientId(-1L);
        setSupplierId(-1L);
        setCurrentlyLoadedView(-1);
    }

    /**
     * Loads clientId and supplierId into Storage object. Loadin should be carried out when user is logged in
     * by normal loginPopup or by method loginFromSession that is used by SpringSecurity.
     */
    public static void loadClientAndSupplierIDs() {
        if (userDetail != null || businessUserDetail != null) {

            if (businessUserDetail.getBusinessRoles().contains(BusinessUserDetail.BusinessRole.CLIENT)) {
                setClientId(businessUserDetail.getClientId());
            }
            if (businessUserDetail.getBusinessRoles().contains(BusinessUserDetail.BusinessRole.SUPPLIER)) {
                setSupplierId(businessUserDetail.getSupplierId());
            }
        } else {
            throw new NullPointerException("userDetail or businessUserDetail in Storage were not initialized.");
        }
    }
}
