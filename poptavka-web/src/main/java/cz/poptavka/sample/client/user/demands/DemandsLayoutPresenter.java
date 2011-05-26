package cz.poptavka.sample.client.user.demands;

import java.util.ArrayList;
import java.util.logging.Logger;

import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;

import cz.poptavka.sample.client.user.StyleInterface;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DemandDetailView;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

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

    private long clientId;
    private Long supplierId;
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

        void setAdministrationToken(String linkString);
    }

    public void bind() {
        view.setMyDemandsToken(getTokenGenerator().invokeMyDemands());
        view.setOffersToken(getTokenGenerator().invokeOffers());
        view.setNewDemandToken(getTokenGenerator().invokeNewDemand(clientId));
        view.setMyDemandsOperatorToken(getTokenGenerator().invokeMyDemandsOperator());
        view.setAdministrationToken(getTokenGenerator().invokeAdministration());
    }

    public void init() {
        eventBus.setTabWidget(view.getWidgetView());
        eventBus.getClientsDemands(clientId);
        eventBus.setUserInteface((StyleInterface) view.getWidgetView());
    }

    public void onSetClientDemands(ArrayList<DemandDetail> demands) {
        clientsDemands = demands;
        if (sendDemandsFlag) {
            eventBus.responseDemands(demands);
        }
    }

    public void onSetOperatorDemands(ArrayList<DemandDetail> demands) {
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
            sendDemandsFlag = true;
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

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

}
