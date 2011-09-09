package cz.poptavka.sample.client.main;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;

import cz.poptavka.sample.client.main.common.LoadingPopup;
import cz.poptavka.sample.client.resources.StyleResource;
import cz.poptavka.sample.client.user.demands.widget.LoadingDiv;

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

    //constants
    private static final int OFFSET_X = 60;
    private static final int OFFSET_Y = 35;

    private static final Logger LOGGER = Logger.getLogger("MainPresenter");

    public static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    public static final StyleResource RSCS = GWT.create(StyleResource.class);

    private static PopupPanel popup = null;
    private static LoadingDiv loading = null;

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

}
