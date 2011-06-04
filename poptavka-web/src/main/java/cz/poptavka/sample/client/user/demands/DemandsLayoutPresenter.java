package cz.poptavka.sample.client.user.demands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.tab.PotentialDemandsPresenter;
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

@Presenter(view = DemandsLayoutView.class, multiple = true)
public class DemandsLayoutPresenter
        extends
        BasePresenter<DemandsLayoutPresenter.DemandsLayoutInterface, UserEventBus> {

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public interface DemandsLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        void setMyDemandsToken(String link);

        void setOffersToken(String link);

        void setNewDemandToken(String link);

        void setPotentialDemandsToken(String link);

        void setMyDemandsOperatorToken(String linkString);

        void setMyProblemsToken(String linkString);

        void setAdministrationToken(String linkString);

        Button getDevelButton();



    }

    // TODO clean up after development
    private PotentialDemandsPresenter develPresenter = null;

    public void bind() {
        view.setMyDemandsToken(getTokenGenerator().invokeMyDemands());
        view.setMyProblemsToken(getTokenGenerator().invokeMyProblems());
        view.setOffersToken(getTokenGenerator().invokeOffers());
        view.setNewDemandToken(getTokenGenerator().invokeNewDemand());
        view.setPotentialDemandsToken(getTokenGenerator().invokePotentialDemands());
        view.setMyDemandsOperatorToken(getTokenGenerator().invokeDemandsOperator());
        view.setAdministrationToken(getTokenGenerator().invokeAdministration());
        view.getDevelButton().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                if (develPresenter != null) {
                    develPresenter.cleanDetailWrapperPresenterForDevelopment();
                    eventBus.removeHandler(develPresenter);
                }
                develPresenter = eventBus.addHandler(PotentialDemandsPresenter.class);
                develPresenter.onInvokePotentialDemands();
//                eventBus.createLoadingPopup(String loadingMessage, Widget anchor)
            }
        });
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
