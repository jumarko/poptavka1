package cz.poptavka.sample.client.user.demands.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.NoSelectionModel;
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

    public interface OffersInterface extends LazyView {
        Widget getWidgetView();

        Button getReplyButton();
        Button getDeleteButton();
        Button getActionButton();
        Button getRefreshButton();

        ListDataProvider<OfferDemandDetail> getDemandProvider();

        NoSelectionModel<OfferDemandDetail> getDemandModel();

        ListDataProvider<OfferDetail> getOfferProvider();

        MultiSelectionModel<OfferDetail> getOfferModel();

        Set<OfferDemandDetail> getSelectedSet();

        Set<OfferDetail> getSelectedOffers();

        SimplePanel getDetailSection();

        void swapTables();

        Anchor getBackToDemandsButton();
    }

    private DetailWrapperPresenter detailPresenter = null;
    private boolean loaded = false;

    public void bindView() {
        // Demand selected -> OffersTable is loaded and shown, DemandDetail as well
        view.getDemandModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.swapTables();
                OfferDemandDetail obj = view.getDemandModel().getLastSelectedObject();
                eventBus.getDemandOffers(obj.getDemandId(), obj.getThreadRootId());
                eventBus.getDemandDetail(obj.getDemandId(), DetailType.OFFER);
            }
        });
        // Offer selected -> that offer is loaded and shown as well
        view.getOfferModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Set<OfferDetail> set = view.getSelectedOffers();
                OfferDetail o = set.iterator().next();
                eventBus.setOfferMessage(o);
                // TODO display every single offer and display it only ONCE
            }
        });
        view.getBackToDemandsButton().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.swapTables();
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
        eventBus.requestClientOfferDemands();
        loaded = true;
    }

    public void onResponseClientDemandsWithOffers(ArrayList<OfferDemandDetail> demands) {
        List<OfferDemandDetail> list = view.getDemandProvider().getList();
        list.clear();
        for (OfferDemandDetail d : demands) {
            list.add(d);
        }
        view.getDemandProvider().refresh();

        // Init DetailWrapper for this view
        if (detailPresenter  == null) {
            detailPresenter = eventBus.addHandler(DetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(), DetailType.OFFER);
        }

        // widget display
        eventBus.displayContent(view.getWidgetView());
    }

    /**
     * Initial fill of Offer table.
     *
     * @param offerList list of offer to be added into provider
     */
    public void onSetDemandOffers(ArrayList<OfferDetail> offers) {
        List<OfferDetail> offerList = view.getOfferProvider().getList();
        // needed clear before inserting offerList of some demand
        offerList.clear();
        for (OfferDetail o : offers) {
            GWT.log("OfferDetail ID: " + o.getOfferId());
            offerList.add(o);
        }
        view.getOfferProvider().refresh();
    }

    public void onSetOfferDetailChange(OfferDetail offerDetail) {
        List<OfferDetail> data = view.getOfferProvider().getList();
        data.get(data.indexOf(offerDetail)).setState(offerDetail.getState());
        view.getOfferProvider().refresh();
    }

    // TODO delete, just devel tool
    public void cleanDetailWrapperPresenterForDevelopment() {
        if (detailPresenter != null) {
            eventBus.removeHandler(detailPresenter);
        }
    }
}
