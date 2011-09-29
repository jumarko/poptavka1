package cz.poptavka.sample.client.user.demands;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.mvp4g.client.annotation.History;
import com.mvp4g.client.annotation.History.HistoryConverterType;
import com.mvp4g.client.history.HistoryConverter;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.admin.tab.DemandsOperatorPresenter;
import cz.poptavka.sample.client.user.demands.tab.AllDemandsPresenter;
import cz.poptavka.sample.client.user.demands.tab.AllSuppliersPresenter;
import cz.poptavka.sample.client.user.demands.tab.MyDemandsPresenter;
//import cz.poptavka.sample.client.user.demands.tab.NewDemandPresenter;
import cz.poptavka.sample.client.user.demands.tab.OffersPresenter;
import cz.poptavka.sample.client.user.demands.tab.PotentialDemandsPresenter;
import java.util.logging.Logger;

/**
 * History Converter for Demands tab in user interface. Instances of view
 * are singletons - loaded only once.
 *
 * During development phase will ALL these presenters set to multiple = true.
 * To achieve faster development without need of view refresh.
 *
 * For production this wil be set back to normal.
 *
 * @author Beho
 */
@History(type = HistoryConverterType.NONE)
public class DemandsHistoryConverter implements HistoryConverter<UserEventBus> {

    /*******************************************************************/
    /**           .DEVEL PRESENTER INITIALIZATION SECTION.               */
    private static final Logger LOGGER = Logger
            .getLogger(DemandsHistoryConverter.class.getName());

    private static final String DEMAND_MY = "invokeMyDemands";
    private static final String DEMAND_OFFERS = "invokeOffers";
    private static final String DEMAND_NEW = "invokeNewDemand";
    private static final String DEMANDS_POTENTIAL = "invokePotentialDemands";
    private static final String DEMANDS_OPERATOR = "invokeDemandsOperator";
    private static final String DEMANDS_ALLDEMANDS = "invokeAtDemands";
    private static final String DEMANDS_ALLSUPPLIERS = "invokeAtSuppliers";

    private MyDemandsPresenter myDemandPresenter = null;
    private OffersPresenter offersPresenter = null;
//    private NewDemandPresenter newDemandPresenter = null;
    private PotentialDemandsPresenter potentialDemandsPresenter = null;
    private DemandsOperatorPresenter operatorPresenter = null;
    private AllDemandsPresenter demandsPresenter = null;
    private AllSuppliersPresenter suppliersPresenter = null;

    /**           DEVEL PRESENTER INITIALIZATION SECTION               */
    /*******************************************************************/

    @Override
    public void convertFromToken(String historyName, String param,
            UserEventBus eventBus) {
        String cookie = Cookies.getCookie("user-presenter");
        LOGGER.fine(" +++++++++ DEMANDS Name: " + historyName + "\nParam: " + param);
        if (cookie.equals("loaded")) {
//            normal behaviour
//            eventBus.dispatch(historyName);

            GWT.log("history name called: " + historyName);
//            Window.alert("history name called: " + historyName);
            eventBus.toggleLoading();

            //devel behaviour
            if (historyName.equals(DEMAND_MY)) {
                if (myDemandPresenter != null) {
                    myDemandPresenter.cleanDetailWrapperPresenterForDevelopment();
                    eventBus.removeHandler(myDemandPresenter);
                }
                myDemandPresenter = eventBus.addHandler(MyDemandsPresenter.class);
                myDemandPresenter.onInvokeMyDemands();
            }

            if (historyName.equals(DEMAND_OFFERS)) {
                if (offersPresenter != null) {
                    offersPresenter.cleanDetailWrapperPresenterForDevelopment();
                    eventBus.removeHandler(offersPresenter);
                }
                offersPresenter = eventBus.addHandler(OffersPresenter.class);
                offersPresenter.onInvokeOffers();
            }
//            if (historyName.equals(DEMAND_NEW)) {
//                if (newDemandPresenter != null) {
//                    eventBus.removeHandler(newDemandPresenter);
//                }
//                newDemandPresenter = eventBus.addHandler(NewDemandPresenter.class);
//                newDemandPresenter.onInvokeNewDemand();
//            }
            if (historyName.equals(DEMANDS_POTENTIAL)) {
                if (potentialDemandsPresenter != null) {
                    potentialDemandsPresenter.cleanDetailWrapperPresenterForDevelopment();
                    eventBus.removeHandler(potentialDemandsPresenter);
                }
                potentialDemandsPresenter = eventBus.addHandler(PotentialDemandsPresenter.class);
                potentialDemandsPresenter.onInvokePotentialDemands();
            }
            if (historyName.equals(DEMANDS_OPERATOR)) {
                if (operatorPresenter != null) {
                    operatorPresenter.cleanDetailWrapperPresenterForDevelopment();
                    eventBus.removeHandler(operatorPresenter);
                }
                operatorPresenter = eventBus.addHandler(DemandsOperatorPresenter.class);
                operatorPresenter.onInvokeDemandsOperator();
            }
            if (historyName.equals(DEMANDS_ALLDEMANDS)) {
                if (demandsPresenter != null) {
//                    DemandsPresenter.cleanDetailWrapperPresenterForDevelopment();
                    eventBus.removeHandler(demandsPresenter);
                }
                demandsPresenter = eventBus.addHandler(AllDemandsPresenter.class);
                demandsPresenter.onInvokeAtDemands();
            }
            if (historyName.equals(DEMANDS_ALLSUPPLIERS)) {
                if (suppliersPresenter != null) {
//                    DemandsPresenter.cleanDetailWrapperPresenterForDevelopment();
                    eventBus.removeHandler(suppliersPresenter);
                }
                suppliersPresenter = eventBus.addHandler(AllSuppliersPresenter.class);
                suppliersPresenter.onInvokeAtSuppliers();
            }

        } else {
            GWT.log("event marked to fire later: " + historyName);
            eventBus.atAccount();
            eventBus.markEventToLoad(historyName);
        }
    }

    @Override
    public boolean isCrawlable() {
        return false;
    }

}
