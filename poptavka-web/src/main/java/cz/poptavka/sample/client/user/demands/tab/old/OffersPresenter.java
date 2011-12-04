package cz.poptavka.sample.client.user.demands.tab.old;

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
import cz.poptavka.sample.client.user.widget.unused.OldDetailWrapperPresenter;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.demand.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.message.OfferDemandMessage;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

@Presenter(view = OffersView.class, multiple = true)
public class OffersPresenter extends
        LazyPresenter<OffersPresenter.OffersInterface, UserEventBus> {

    public interface OffersInterface extends LazyView {
        Widget getWidgetView();

        Button getReplyButton();
        Button getDeleteButton();
        Button getActionButton();
        Button getRefreshButton();

        ListDataProvider<OfferDemandMessage> getDemandProvider();

        NoSelectionModel<OfferDemandMessage> getDemandModel();

        ListDataProvider<FullOfferDetail> getOfferProvider();

        MultiSelectionModel<FullOfferDetail> getOfferModel();

        Set<OfferDemandDetail> getSelectedSet();

        Set<FullOfferDetail> getSelectedOffers();

        SimplePanel getDetailSection();

        void swapTables();

        Anchor getBackToDemandsButton();
    }

    private OldDetailWrapperPresenter detailPresenter = null;
    private boolean loaded = false;

    public void bindView() {
        // Demand selected -> OffersTable is loaded and shown, FullDemandDetail as well
        view.getDemandModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.swapTables();
                OfferDemandMessage obj = view.getDemandModel().getLastSelectedObject();
//                eventBus.getDemandOffers(obj.getDemandId(), obj.getThreadRootId());
                eventBus.getDemandDetail(obj.getDemandId(), ViewType.OFFER);
            }
        });
        // Offer selected -> that offer is loaded and shown as well
        view.getOfferModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                Set<FullOfferDetail> set = view.getSelectedOffers();
                FullOfferDetail o = set.iterator().next();
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
//            eventBus.displayContent(view.getWidgetView());
            return;
        }
        // TODO is this individual demandDetail type needed?
        eventBus.requestClientOfferDemands();
        loaded = true;
    }

    public void onResponseClientDemandsWithOffers(ArrayList<OfferDemandMessage> demands) {
        List<OfferDemandMessage> list = view.getDemandProvider().getList();
        list.clear();
        for (OfferDemandMessage d : demands) {
            list.add(d);
        }
        view.getDemandProvider().refresh();

        // Init DetailWrapper for this view
        if (detailPresenter  == null) {
            detailPresenter = eventBus.addHandler(OldDetailWrapperPresenter.class);
            detailPresenter.initDetailWrapper(view.getDetailSection(), ViewType.OFFER);
        }

        // widget display
//        eventBus.displayContent(view.getWidgetView());
    }

    /**
     * Initial fill of Offer table.
     *
     * @param offerList list of offer to be added into provider
     */
    public void onSetDemandOffers(ArrayList<FullOfferDetail> offers) {
        List<FullOfferDetail> offerList = view.getOfferProvider().getList();
        // needed clear before inserting offerList of some demand
        offerList.clear();
        for (FullOfferDetail o : offers) {
            GWT.log("FullOfferDetail ID: " + o.getOfferDetail().getId());
            offerList.add(o);
        }
        view.getOfferProvider().refresh();
    }

    public void onSetOfferDetailChange(OfferDetail oferDetail) {
        List<FullOfferDetail> data = view.getOfferProvider().getList();
        data.get(data.indexOf(oferDetail)).getOfferDetail().setState(oferDetail.getState());
        view.getOfferProvider().refresh();
    }

    // TODO delete, just devel tool
    public void cleanDetailWrapperPresenterForDevelopment() {
        if (detailPresenter != null) {
            eventBus.removeHandler(detailPresenter);
        }
    }
}
