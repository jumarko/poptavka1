package cz.poptavka.sample.client.user.messages;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.tab.PotentialDemandsPresenter;
import cz.poptavka.sample.client.user.demands.widget.LoadingDiv;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.UserDetail.Role;

/**
 * For every user - default tab
 *
 * Consists of left menu only and holder for demands related
 * stuff. Contains list of all demands for faster working with
 * demands.
 *
 * @author Beho
 */
@Presenter(view = MessagesLayoutView.class)//, multiple = true)
public class MessagesLayoutPresenter extends
        BasePresenter<MessagesLayoutPresenter.MessagesLayoutInterface, UserEventBus> {

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    public interface MessagesLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        //menu client
        void setInboxToken(String link);

        void setSentToken(String link);

        void setTrashToken(String linkString);

        SimplePanel getContentPanel();
    }
    // TODO clean up after development
    private PotentialDemandsPresenter develPresenter = null;
    private LoadingDiv loading = null;

    public void bind() {
        view.setInboxToken(getTokenGenerator().invokeInbox());
        view.setSentToken(getTokenGenerator().invokeSent());
        view.setTrashToken(getTokenGenerator().invokeDeleted());
    }

    public void onInitMessages() {
        GWT.log("som tu");
        //eventBus.loadingShow(MSGS.progressDemandsLayoutInit());
        eventBus.setTabMessagesWidget(view.getWidgetView());
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

    public void onDisplayMessagesContent(Widget contentWidget) {
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
