package cz.poptavka.sample.client.home.demands;

import java.util.List;
import java.util.logging.Logger;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mvp4g.client.presenter.BasePresenter;
import com.mvp4g.client.annotation.Presenter;
import cz.poptavka.sample.client.service.demand.DemandRPCService;
import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.domain.demand.Demand;

@Presenter(view = DemandsView.class)
public class DemandsPresenter extends BasePresenter<DemandsPresenter.DemandsViewInterface, DemandsEventBus> {

    private static final Logger LOGGER = Logger
            .getLogger(DemandsPresenter.class.getName());

    /**
     * The message displayed to the user when the server cannot be reached or
     * returns an error.
     */
    private static final String SERVER_ERROR = "An error occurred while "
            + "attempting to contact the server. Please check your network "
            + "connection and try again.";

    /**
     * Create a remote service to retrieve demands from database.
     */
    private final DemandRPCServiceAsync demandService = GWT
            .create(DemandRPCService.class);

    public interface DemandsViewInterface {
        void displayDemands(List<Demand> result);
    }

    /**
     * Try retrieve demand from server and display them on Success.
     *
     */
    public void onStart() {
        LOGGER.info("Starting demands presenter...");

        demandService.getAllDemands(new AsyncCallback<List<Demand>>() {

            @Override
            public void onFailure(Throwable caught) {
                LOGGER.info(SERVER_ERROR);
            }

            @Override
            public void onSuccess(List<Demand> result) {
                eventBus.displayDemands(result);
            }
        });
    }

    /**
     * Call DemandsView to display given list of demands.
     *
     * @param result
     */
    public void onDisplayDemands(List<Demand> result) {
        view.displayDemands(result);
    }
}
