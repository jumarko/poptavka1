package cz.poptavka.sample.client.user.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widget.LoadingDiv;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

@Presenter(view = AdminLayoutView.class) //, multiple = true)
public class AdminLayoutPresenter         extends
        BasePresenter<AdminLayoutPresenter.AdminLayoutInterface, UserEventBus> {

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public interface AdminLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        void setDemandsToken(String link);

        void setUsersToken(String link);

        void setOffersToken(String link);

        void setSuppliersToken(String link);

        SimplePanel getContentPanel();
    }

    private LoadingDiv loading = null;

    public void bind() {
        view.setDemandsToken(getTokenGenerator().invokeAdminDemands());
        view.setOffersToken(getTokenGenerator().invokeAdminOffers());
//        view.setUsersToken(getTokenGenerator().invokeAdminUsers());
        view.setSuppliersToken(getTokenGenerator().invokeAdminSuppliers());
    }

    public void onInitAdmin() {
        GWT.log("som tu");
        //eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
        eventBus.setTabAdminWidget(view.getWidgetView());
        eventBus.fireMarkedEvent();
        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    public void init(UserDetail user) {
        // hiding window for this is after succesfull Userhandler call
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

    public void onDisplayAdminContent(Widget contentWidget) {
//        onToggleLoading();
        view.setContent(contentWidget);
    }

    public void onToggleLoading() {
        if (loading == null) {
            GWT.log("  - loading created");
            loading = new LoadingDiv(view.getContentPanel().getParent());
        } else {
            GWT.log("  - loading removed");
            loading.getElement().removeFromParent();
            loading = null;
        }
    }
}