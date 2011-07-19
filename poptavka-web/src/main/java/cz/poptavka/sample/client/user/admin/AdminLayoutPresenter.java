package cz.poptavka.sample.client.user.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

@Presenter(view = AdminLayoutView.class)
public class AdminLayoutPresenter         extends
        BasePresenter<AdminLayoutPresenter.AdminLayoutInterface, UserEventBus> {

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public interface AdminLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        void setOperatorDemandsToken(String link);

        void setAdminUsersToken(String link);

        void setAdminOffersToken(String link);



    }

    public void bind() {
        view.setOperatorDemandsToken(getTokenGenerator().invokeAdministration());
        view.setAdminOffersToken(getTokenGenerator().invokeAdminOffers());
    }

    public void onInitAdmin() {
        GWT.log("som tu");
        eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
        eventBus.setTabAdminWidget(view.getWidgetView());
        eventBus.fireMarkedEvent();
        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    public void init(UserDetail user) {
        // hiding window for this is after succesfull Userhandler call
        eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
        if (user.getRoleList().contains(Role.CLIENT)) {
            // TODO execute client specific demands init methods/calls
        }
        if (user.getRoleList().contains(Role.SUPPLIER)) {
            // TODO using businessUserId and NOT supplier ID
            // DEBUGING popup
            // TODO Maybe do nothing
//            PopupPanel panel = new PopupPanel(true);
//            panel.getElement().setInnerHTML("<br/>Getting SupplierDemands<")
//            panel.center();
//            eventBus.getPotentialDemands(user.getId());
        }


        eventBus.setTabWidget(view.getWidgetView());
        eventBus.fireMarkedEvent();

        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    public void onDisplayContent(Widget contentWidget) {
        view.setContent(contentWidget);
    }
}