package com.eprovement.poptavka.client.common.session;

import com.eprovement.poptavka.client.common.LoadingPopup;
import com.eprovement.poptavka.client.resources.StyleResource;
import com.eprovement.poptavka.client.user.widget.LoadingDiv;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

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
    private static String forwardHistory = "";
    private static Boolean appCalledByURL = null;
    //
    public static final String BACK = "back";
    public static final String FORWARD = "forward";
    /**************************************************************************/
    //global constants
    public static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    public static final StyleResource RSCS = GWT.create(StyleResource.class);
    public static final long DAY_LENGTH = 1000 * 60 * 60 * 24;
    public static final String SUP_NEW_DETAIL_DEMAND_ID = "supDemDet";
    //local constants
    private static final int OFFSET_X = 60;
    private static final int OFFSET_Y = 35;
    private static PopupPanel popup = null;
    private static LoadingDiv loading = null;
    private static UserDetail userDetail = null;
    private static BusinessUserDetail businessUserDetail = null;
    //client projects - selected demand
    private static long demandId = -1L;

    /**************************************************************************/
    /* History methods                                                        */
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

    public static String getForwardHistory() {
        return forwardHistory;
    }

    public static void setForwardHistory(String forwardHistory) {
        Storage.forwardHistory = forwardHistory;
    }

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

    //getters for global final classes
    public LocalizableMessages getMessages() {
        return MSGS;
    }

    public StyleResource getResource() {
        return RSCS;
    }

    /**  Show and hide loading window related methods. **/
    public static void showLoading(String loadingMessage) {
        if (!(popup == null)) {
            LoadingPopup popupContent = (LoadingPopup) popup.getWidget();
            popupContent.setMessage(loadingMessage);
        } else {
            createLoadingPopup(loadingMessage);
        }
    }

    public static void hideLoading() {
        if (popup != null) {
            popup.hide();
            popup = null;
        }
    }

    private static void createLoadingPopup(String loadingMessage) {
        popup = new PopupPanel(false, false);
        popup.setStylePrimaryName(StyleResource.INSTANCE.common().loadingPopup());
        popup.setWidget(new LoadingPopup(loadingMessage));
        popup.setPopupPosition((Window.getClientWidth() / 2) - OFFSET_X, (Window.getClientHeight() / 2) - OFFSET_Y);
        popup.show();
    }

    public static void toggleLoading(Widget widget) {
        if (loading == null) {
            GWT.log("  - loading created");
            loading = new LoadingDiv(widget.getParent());
        } else {
            GWT.log("  - loading removed");
            loading.getElement().removeFromParent();
            loading = null;
        }
    }

    public static void setUserDetail(UserDetail aUserDetail) {
        userDetail = aUserDetail;
    }

    public static UserDetail getUser() {
        return userDetail;
    }

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

    /**
     * Method clears all data of this Storage session object.
     */
    public static void invalidateStorage() {
        setUserDetail(null);
        setBusinessUserDetail(null);
        setAppCalledByURL(null);
        // TODO martin - shall we clear following values when invalidating Storage? i.e. during logout
        // Will History be working correctly if we clear these values?
        // Martin will try to make new solution for history between login/logout
//        setDemandId(-1L);
//        setCurrentlyLoadedView(-1);
    }
}
