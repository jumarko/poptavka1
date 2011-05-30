package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.OffersFlexTable;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;

@Presenter(view = OffersView.class)
public class OffersPresenter extends
        LazyPresenter<OffersPresenter.OffersInterface, UserEventBus> {

    private ArrayList<ArrayList<OfferDetail>> offers = new ArrayList<ArrayList<OfferDetail>>();

    private static final Logger LOGGER = Logger.getLogger(OffersPresenter.class
            .getName());

    public interface OffersInterface extends LazyView {

        Button getAnswerBtn();

        Button getRefuseBtn();

        Button getAcceptBtn();

        OffersFlexTable getTable();

        SimplePanel getDetailSection();

        Widget getWidgetView();
    }

    public void bindView() {
        view.getTable().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                HashMap<Integer, Integer> map = view.getTable().getClickedRow(event);
                resolveTableClick(map);
            }
        });
    }

    /** Init method. **/
    public void onInvokeOffers() {
        eventBus.displayContent(view.getWidgetView());
        eventBus.requestClientDemands();
    }

    public void onResponseClientDemands(ArrayList<DemandDetail> demands) {
        if (demands != null) {
            view.getTable().setData(demands);
            eventBus.requestOffers(getDemandIds(demands));
        }
    }

    public void onResponseOffers(ArrayList<ArrayList<OfferDetail>> offersList) {
        view.getTable().setOffers(offersList);
    }

    /** Help method for requestOffers. **/
    private ArrayList<Long> getDemandIds(ArrayList<DemandDetail> demands) {
        ArrayList<Long> idList = new ArrayList<Long>();
        for (DemandDetail demand : demands) {
            idList.add(demand.getId());
        }
        return idList;
    }

    public void resolveTableClick(HashMap<Integer, Integer> map) {
        LOGGER.fine("A-" + map.get(OffersFlexTable.RESULT_ACTION));
        LOGGER.fine("T-" + map.get(OffersFlexTable.RESULT_TYPE));
        LOGGER.fine("ID-" + map.get(OffersFlexTable.RESULT_ID));
        if (map.get(OffersFlexTable.RESULT_TYPE) == OffersFlexTable.ACTION_SORT) {
            //just pure table thing
            return;
        }
        if (map.get(OffersFlexTable.RESULT_ACTION) == OffersFlexTable.ACTION_OFFER) {
            view.getAcceptBtn().setEnabled(true);
            view.getAnswerBtn().setEnabled(true);
            view.getRefuseBtn().setEnabled(true);
        }
        if (map.get(OffersFlexTable.RESULT_ACTION) == OffersFlexTable.ACTION_DEMAND) {
            view.getAcceptBtn().setEnabled(false);
            view.getAnswerBtn().setEnabled(false);
            view.getRefuseBtn().setEnabled(false);
            //demandDetail call
//            eventBus.requestDemandDetail(map.get(OffersFlexTable.RESULT_ID));
        }
//        offer message
//        toggle - to disable buttons
    }

    public void onResponseDemandDetail(Widget widget) {
        view.getDetailSection().setWidget(widget);
    }
}
