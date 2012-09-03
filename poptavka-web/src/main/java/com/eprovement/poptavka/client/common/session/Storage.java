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
    private static final String BACK = "back";
    // Value is set on each new module load. To prevent any further complications, set this value
    // using SearchModulePresenter constants.
    private static int currentlyLoadedView = -1;
    private static String actionLoginHomeHistory = BACK;
    private static String actionLoginAccountHistory = BACK;

    public static Storage get() {
        return INSTANCE;
    }
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

    public static String getActionLoginAccountHistory() {
        return actionLoginAccountHistory;
    }

    public static void setActionLoginAccountHistory(String actionLoginAccountHistory) {
        Storage.actionLoginAccountHistory = actionLoginAccountHistory;
    }

    public static String getActionLoginHomeHistory() {
        return actionLoginHomeHistory;
    }

    public static void setActionLoginHomeHistory(String actionLoginHomeHistory) {
        Storage.actionLoginHomeHistory = actionLoginHomeHistory;
    }

    public static long getDemandId() {
        return demandId;
    }

    public static void setDemandId(long demandId) {
        Storage.demandId = demandId;
    }

    /**
     * Method clears all data of this Storage session object.
     */
    public static void invalidateStorage() {
        setUserDetail(null);
        setBusinessUserDetail(null);
        // TODO martin - shall we clear following values when invalidating Storage? i.e. during logout
        // Will History be working correctly if we clear these values?
        // Martin will try to make new solution for history between login/logout
//        setActionLoginHomeHistory(BACK);
//        setActionLoginAccountHistory(BACK);
//        setDemandId(-1L);
//        setCurrentlyLoadedView(-1);
    }
}
