package cz.poptavka.sample.client.user;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.TabLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.demands.DemandsLayoutPresenter;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

/**
 * Main presenter for User account.
 *
 * @author Beho
 *
 */
@Presenter(view = UserView.class)
public class UserPresenter extends LazyPresenter<UserPresenter.UserViewInterface, UserEventBus> {

    private static final Logger LOGGER = Logger.getLogger("UserPresenter");

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface UserViewInterface extends LazyView {

        TabLayoutPanel getLayoutPanel();

        void setBody(Widget body);

        Widget getWidgetView();
    }

    private DemandsLayoutPresenter demandsLayoutPresenter = null;
    private UserDetail user = null;

    private String eventMarkedToFire;
    private boolean fireMarkedEvent = false;

    // initial user call after login
    // while developing we can enter this url even without login operation
    public void onAtAccount() {
        Cookies.setCookie("user-presenter", "loaded");
        if (user == null) {
            eventBus.loadingShow(MSGS.progressGetUserDetail());
            eventBus.getUser();
        } else {
            onSetUser(user);
        }
    }

    // setting of user
    // init of demandLayout tab
    public void onSetUser(UserDetail userDetail) {
        this.user = userDetail;
        eventBus.loadingHide();

        showDevelUserInfoPopupThatShouldBedeletedAfter();

        // DemandslayoutInit
        if (demandsLayoutPresenter != null) {
            eventBus.removeHandler(demandsLayoutPresenter);
        }
        demandsLayoutPresenter = eventBus.addHandler(DemandsLayoutPresenter.class);
        demandsLayoutPresenter.init(user);

        eventBus.setUserLayout();
        eventBus.setBodyHolderWidget(view.getWidgetView());

    }

    public void onFireMarkedEvent() {
        LOGGER.fine("Fire Marked Event");
        if (fireMarkedEvent) {

            LOGGER.fine("    Firing event: " + eventMarkedToFire);
            fireMarkedEvent = false;
            eventBus.dispatch(eventMarkedToFire);
        } else {
            //TODO delete
            LOGGER.fine("There's nothing to fire");
        }
    }

    public void onSetUserInteface(StyleInterface widget) {
        // TODO
        //set interface according to user roles
        if (user.getRoleList().contains(Role.CLIENT)) {
            widget.setRoleInterface(Role.CLIENT);
        }
        if (user.getRoleList().contains(Role.SUPPLIER)) {
            widget.setRoleInterface(Role.SUPPLIER);
        }
    }

    public void onSetTabWidget(Widget tabBody) {
        view.setBody(tabBody);
    }

    /** REQUESTs && RESPONSEs **/
    public void onRequestClientId() {
        eventBus.responseClientId(user.getClientId());
    }

    public void onRequestSupplierId() {
        eventBus.responseSupplierId(user.getSupplierId());
    }

    public void onMarkEventToLoad(String historyName) {
        fireMarkedEvent = true;
        this.eventMarkedToFire  = historyName;
    }

    public void onClearUserOnUnload() {
        this.user = null;
    }

 // TODO delete for production
    private void showDevelUserInfoPopupThatShouldBedeletedAfter() {
        PopupPanel userInfoPanel = new PopupPanel(true);
        String br = "<br />";
        StringBuilder sb = new StringBuilder("<b>User Info:</b>" + br);
        sb.append("ID: " + user.getId() + br);

        sb.append("<i>-- user roles --</i>" + br);
        if (user.getRoleList().contains(Role.CLIENT)) {
            sb.append("<b><i>CLIENT</i></b>" + br);
            sb.append("ClientID: " + user.getClientId() + br);
            sb.append("Demand Count: " + user.getDemandsId().size() + br);
            sb.append("Demands Messages: " + "n/a" + " / " + "n/a" + br);
            sb.append("Demands Offers: " + "n/a" + " / " + "n/a" + br);
            sb.append("<i>-- -- -- --</i>" + br);
        }
        if (user.getRoleList().contains(Role.SUPPLIER)) {
            sb.append("<b><i>SUPPLIER</i></b>" + br);
            sb.append("Potentional Demands: " + "n/a" + " / " + "n/a" + br);
            sb.append("<i>-- -- -- --</i>" + br);
        }
        if (user.getRoleList().contains(Role.PARTNER)) {
            sb.append("<b><i>PARTNER</i></b>" + br);
            sb.append("<i>-- -- -- --</i>" + br);
        }
        if (user.getRoleList().contains(Role.OPERATOR)) {
            sb.append("<b><i>OPERATOR</i></b>" + br);
            sb.append("<i>-- -- -- --</i>" + br);
        }
        if (user.getRoleList().contains(Role.ADMIN)) {
            sb.append("<b><i>ADMIN</i></b>" + br);
            sb.append("<i>-- -- -- --</i>" + br);
        }
        sb.append("Messages: " + "n/a" + " / " + "n/a" + br);

        userInfoPanel.getElement().setInnerHTML(sb.toString());
        userInfoPanel.show();
    }
}
