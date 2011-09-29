package cz.poptavka.sample.client.user.admin.tab;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.OfferRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.offer.FullOfferDetail;
import java.util.Map;

@EventHandler
public class AdminOffersHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private OfferRPCServiceAsync offerService = null;

    /**********************************************************************************************
     ***********************  OFFER SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetAdminOffersCount() {
        offerService.getAllOffersCount(new AsyncCallback<Long>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(Long result) {
                eventBus.createAdminOffersAsyncDataProvider(result.intValue());
            }
        });
    }

    public void onGetAdminOffers(int start, int count) {
        offerService.getOffers(start, count, new AsyncCallback<List<FullOfferDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(List<FullOfferDetail> result) {
                eventBus.displayAdminTabOffers(result);
            }
        });

    }

    public void onGetSortedOffers(int start, int count, Map<String, OrderType> orderColumns) {
        offerService.getSortedOffers(start, count, orderColumns,
                new AsyncCallback<List<FullOfferDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<FullOfferDetail> result) {
                        eventBus.displayAdminTabOffers(result);
                    }
                });
    }

    public void onUpdateOffer(FullOfferDetail offer) {
        offerService.updateOffer(offer, new AsyncCallback<FullOfferDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(FullOfferDetail result) {
//                eventBus.refreshUpdatedOffer(result);
            }
        });
    }
}
