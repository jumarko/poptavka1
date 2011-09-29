package cz.poptavka.sample.client.user.demands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.ui.Anchor;
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

@Presenter(view = OldDemandsLayoutView.class, multiple = true)
public class OldDemandsLayoutPresenter
        extends
        BasePresenter<OldDemandsLayoutPresenter.DemandsLayoutInterface, UserEventBus> {

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public interface DemandsLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        //menu client
        void setMyDemandsToken(String link);

        void setOffersToken(String link);

        void setNewDemandToken(String link);

        void setAllDemandsToken(String linkString);

        void setAllSuppliersToken(String linkString);

        //menu supplier
        void setPotentialDemandsToken(String link);

        SimplePanel getContentPanel();

        //beho devel section
        Anchor getDevelAnchor();
        HasClickHandlers getCreateDemandButton();

    }

    // TODO clean up after development
    private PotentialDemandsPresenter develPresenter = null;
    private LoadingDiv loading = null;

    public void bind() {
        // MENU - CLIENT
        view.setMyDemandsToken(getTokenGenerator().invokeMyDemands());
        view.setOffersToken(getTokenGenerator().invokeOffers());
        // TODO praso - temporarily commetnted for the prupose of trying to fix the navigation linking between modules
        //        view.setNewDemandToken(getTokenGenerator().invokeNewDemand());
        view.setAllDemandsToken(getTokenGenerator().invokeAtDemands());
        view.setAllSuppliersToken(getTokenGenerator().invokeAtSuppliers());

        //MENU - SUPPLIER
        view.setPotentialDemandsToken(getTokenGenerator().invokePotentialDemands());

        //DEVEl - BEHO
        view.getDevelAnchor().addClickHandler(new ClickHandler() {

            @Override
            public void onClick(ClickEvent arg0) {
                // TODO Auto-generated method stub

            }
        });

        // TODO praso - test linking to DemandCreationModule
        view.getCreateDemandButton().addClickHandler(new ClickHandler() {

            public void onClick(ClickEvent event) {
                eventBus.goToCreateDemand();
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
        onToggleLoading();
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
