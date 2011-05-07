package cz.poptavka.sample.client.user.demands;

import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.UserEventBus;

/**
 *
 * Just for user, not operator functionality implemented. Serves as holder for demands related stuff.
 *
 * @author Beho
 *
 */

@Presenter(view = DemandsLayoutView.class)
public class DemandsLayoutPresenter extends BasePresenter<DemandsLayoutPresenter.DemandsLayoutInterface, UserEventBus> {


    private static final Logger LOGGER = Logger
            .getLogger(DemandsLayoutPresenter.class.getName());

    public interface DemandsLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        void setMyDemandsToken(String link);

        void setOffersToken(String link);

        void setNewDemandToken(String link);
    }

    public void bind() {
        view.setMyDemandsToken(getTokenGenerator().invokeMyDemands());
        view.setOffersToken(getTokenGenerator().invokeOffers());
        view.setNewDemandToken(getTokenGenerator().invokeNewDemand());

    }

    public void onAtAccount() {
        eventBus.setTabWidget(view.getWidgetView());
    }

    public void onDisplayContent(Widget contentWidget) {
        LOGGER.fine("display content");
        view.setContent(contentWidget);
    }

}
