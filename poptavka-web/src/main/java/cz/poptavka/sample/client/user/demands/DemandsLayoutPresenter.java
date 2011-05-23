package cz.poptavka.sample.client.user.demands;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DemandDetailView;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.UserDetail;

import java.util.ArrayList;
import java.util.logging.Logger;

/**
 *
 * Just for user, not operator functionality implemented. Serves as holder for demands related stuff.
 * Contains list of all demands for faster working with demands.
 *
 * @author Beho
 *
 */

@Presenter(view = DemandsLayoutView.class)
public class DemandsLayoutPresenter extends BasePresenter<DemandsLayoutPresenter.DemandsLayoutInterface, UserEventBus> {

    private static final long TEST_CLIENT_ID = 1L;

    //will be assigned during login process
    //devel client with ID = 17
    private long clientId = TEST_CLIENT_ID;
    private ArrayList<DemandDetail> clientsDemands = null;
    private ArrayList<ArrayList<OfferDetail>> client = new ArrayList<ArrayList<OfferDetail>>();
    private boolean sendDemandsFlag = false;

    private static final Logger LOGGER = Logger
            .getLogger(DemandsLayoutPresenter.class.getName());

    public interface DemandsLayoutInterface {

        Widget getWidgetView();

        void setContent(Widget contentWidget);

        void setMyDemandsToken(String link);

        void setOffersToken(String link);

        void setNewDemandToken(String link);

        void setMyDemandsOperatorToken(String linkString);
    }

    public void bind() {
        view.setMyDemandsToken(getTokenGenerator().invokeMyDemands());
        view.setOffersToken(getTokenGenerator().invokeOffers());
        view.setNewDemandToken(getTokenGenerator().invokeNewDemand());
        view.setMyDemandsOperatorToken(getTokenGenerator().invokeMyDemandsOperator());
    }

    public void onAtAccount(UserDetail user) {
        eventBus.setTabWidget(view.getWidgetView());
        if (clientsDemands == null) {
            eventBus.getClientsDemands(clientId);
        }
    }

    public void onSetClientDemands(ArrayList<DemandDetail> demands) {
        clientsDemands = demands;
        if (sendDemandsFlag) {
            eventBus.responseDemands(demands);
        }
    }

    public void onDisplayContent(Widget contentWidget) {
        view.setContent(contentWidget);
    }

    public void onRequestDemands() {
        if (clientsDemands == null) {
            sendDemandsFlag  = true;
        } else {
            eventBus.responseDemands(clientsDemands);
        }

    }

    public void onShowDemandDetail(long demandId) {
        for (DemandDetail demand : clientsDemands) {
            if (demand.getId() == demandId) {
                eventBus.setDetailSection(new DemandDetailView(demand));
                return;
            }
        }
    }

}
