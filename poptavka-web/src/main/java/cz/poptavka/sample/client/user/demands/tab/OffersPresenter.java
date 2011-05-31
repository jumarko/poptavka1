package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;

import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.client.user.demands.widgets.DetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@Presenter(view = OffersView.class, multiple = true)
public class OffersPresenter extends
        LazyPresenter<OffersPresenter.OffersInterface, UserEventBus> {

    private ArrayList<ArrayList<OfferDetail>> offers = new ArrayList<ArrayList<OfferDetail>>();

    public interface OffersInterface extends LazyView {
        Widget getWidgetView();

        Button getReplyButton();
        Button getDeleteButton();
        Button getActionButton();
        Button getRefreshButton();

        ListDataProvider<OfferDemandDetail> getDataProvider();

        MultiSelectionModel<OfferDemandDetail> getSelectionModel();

        Set<OfferDemandDetail> getSelectedSet();

        SimplePanel getDetailSection();
    }

    private DetailWrapperPresenter detailPresenter = null;
    private boolean loaded = false;

    public void bindView() {
        view.getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
//                // TODO fix multiSelection
//                GWT.log("SIZE of result set: " + view.getSelectedSet().size());
//                if (view.getSelectedSet().size() != 1) {
//                    // do not show any demand detail
//                    return;
//                }
                Iterator<OfferDemandDetail> iter = view.getSelectedSet().iterator();
                OfferDemandDetail selected = iter.next();

                // event calls from the click

//                eventBus.getDemandDetail(selected.getDemandId(), DetailType.POTENTIAL);
//                eventBus.requestPotentialDemandConversation(selected.getMessageId(), Random.nextInt(6) + 1);
            }
        });
    }

    /** Init method. **/
    public void onInvokeOffers() {
        if (loaded) {
            eventBus.displayContent(view.getWidgetView());
            return;
        }
        // TODO is this individual demandDetail type needed?
        eventBus.requestOfferClientDemands();
        loaded = true;
    }

    public void onResponseClientDemandsWithOffers(ArrayList<OfferDemandDetail> data) {
        List<OfferDemandDetail> list = view.getDataProvider().getList();
        list.clear();
        for (OfferDemandDetail d : data) {
            list.add(d);
        }
        view.getDataProvider().refresh();

        // Init DetailWrapper for this view
        if (detailPresenter  == null) {
            detailPresenter = eventBus.addHandler(DetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(), DetailType.POTENTIAL);
        }

        // widget display
        eventBus.displayContent(view.getWidgetView());
    }

//    public void onResponseOffers(ArrayList<ArrayList<OfferDetail>> offersList) {
//        view.getTable().setOffers(offersList);
//    }

//    /** Help method for requestOffers. **/
//    private ArrayList<Long> getDemandIds(ArrayList<DemandDetail> demands) {
//        ArrayList<Long> idList = new ArrayList<Long>();
//        for (DemandDetail demand : demands) {
//            idList.add(demand.getId());
//        }
//        return idList;
//    }

//    public void resolveTableClick(HashMap<Integer, Integer> map) {
//        LOGGER.fine("A-" + map.get(OffersFlexTable.RESULT_ACTION));
//        LOGGER.fine("T-" + map.get(OffersFlexTable.RESULT_TYPE));
//        LOGGER.fine("ID-" + map.get(OffersFlexTable.RESULT_ID));
//        if (map.get(OffersFlexTable.RESULT_TYPE) == OffersFlexTable.ACTION_SORT) {
//            //just pure table thing
//            return;
//        }
//        if (map.get(OffersFlexTable.RESULT_ACTION) == OffersFlexTable.ACTION_OFFER) {
//            view.getAcceptBtn().setEnabled(true);
//            view.getAnswerBtn().setEnabled(true);
//            view.getRefuseBtn().setEnabled(true);
//        }
//        if (map.get(OffersFlexTable.RESULT_ACTION) == OffersFlexTable.ACTION_DEMAND) {
//            view.getAcceptBtn().setEnabled(false);
//            view.getAnswerBtn().setEnabled(false);
//            view.getRefuseBtn().setEnabled(false);
//            //demandDetail call
////            eventBus.requestDemandDetail(map.get(OffersFlexTable.RESULT_ID));
//        }
////        offer message
////        toggle - to disable buttons
//    }

    // TODO delete
    public void onResponseDemandDetail(Widget widget) {
        view.getDetailSection().setWidget(widget);
    }

    // TODO delete, just devel tool
    public void cleanDetailWrapperPresenterForDevelopment() {
        GWT.log("WRAPPER REMOVED");
        eventBus.removeHandler(detailPresenter);
    }
}
