package cz.poptavka.sample.client.user;

import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
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

    // initial user call after login
    // while developing we can enter this url even without login operation
    public void onAtAccount() {
        eventBus.loadingShow(MSGS.progressGetUserDetail());
        eventBus.getUser();
    }

    // setting of user
    // init of demandLayout tab
    public void onSetUser(UserDetail userDetail) {
        this.user = userDetail;
        eventBus.loadingHide();

        // DemandslayoutInit
        demandsLayoutPresenter = eventBus.addHandler(DemandsLayoutPresenter.class);
        if (user.getRoleList().contains(Role.CLIENT)) {
            demandsLayoutPresenter.setClientId(user.getClientId());
        }
        if (user.getRoleList().contains(Role.SUPPLIER)) {
            demandsLayoutPresenter.setSupplierId(user.getSupplierId());
        }
        demandsLayoutPresenter.init();

        eventBus.setUserLayout();
        eventBus.setBodyHolderWidget(view.getWidgetView());
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
}
