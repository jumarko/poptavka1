package cz.poptavka.sample.client.user.handler;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.DemandRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.OfferRPCServiceAsync;
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;
import cz.poptavka.sample.shared.domain.demand.PotentialDemandDetail;

@EventHandler
public class UserHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private DemandRPCServiceAsync demandService = null;
    @Inject
    private UserRPCServiceAsync userService = null;
    @Inject
    private OfferRPCServiceAsync offerService = null;

    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public void onGetAllDemands() {
        demandService.getAllDemands(new AsyncCallback<List<DemandDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
            }

            @Override
            public void onSuccess(List<DemandDetail> result) {
                eventBus.setAllDemands(result);
            }
        });
    }

    public void onUpdateDemand(DemandDetail demand) {
        demandService.updateDemand(demand, new AsyncCallback<DemandDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                throw new UnsupportedOperationException("Not supported yet.");
            }

            @Override
            public void onSuccess(DemandDetail result) {
                eventBus.refreshUpdatedDemand(result);
            }
        });
    }

    /**
     * Get User according to stored sessionID from DB after login.
     */
    public void onGetUser() {
        // get sessionId cookie
        String sessionID = Cookies.getCookie("sid");
        if (sessionID == null) {
            Window.alert("sessionID is null and it shouldn't be");
            return;
        }
        userService.getSignedUser(sessionID, new AsyncCallback<UserDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                eventBus.loadingHide();
                Window.alert("Error during getting logged User detail\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(UserDetail result) {
                eventBus.loadingShow(MSGS.progressCreatingUserInterface());
                eventBus.setUser(result);
            }
        });
    }

    /**
     * Get Detail according to demand id.
     */
    public void onGetDemandDetail(Long demandId, final DetailType typeOfDetail) {
        GWT.log("REACH RPC");
        demandService.getDemand(demandId, new AsyncCallback<DemandDetail>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DemandDetail result) {
                eventBus.setDemandDetail(result, typeOfDetail);
            }
        });
    }

    /**
     * Get Supplier's potential demands list.
     *
     * @param businessUserId
     */
    public void onGetPotentialDemands(long businessUserId) {
        demandService.getPotentialDemandsForSupplier(businessUserId,
                new AsyncCallback<ArrayList<PotentialDemandDetail>>() {
                    @Override
                    public void onFailure(Throwable caught) {
                        Window.alert("Error in UserHandler in method: onGetPotentialDemandsList"
                                + caught.getMessage());
                    }

                    @Override
                    public void onSuccess(
                            ArrayList<PotentialDemandDetail> result) {
                        eventBus.responsePotentialDemands(result);
                    }
                });
    }


    /**
     * Get Client's demands for offers.
     *
     * @param clientId
     */
    public void onGetClientDemandsWithOffers(Long clientId) {
        offerService.getClientDemands(clientId, new AsyncCallback<ArrayList<OfferDemandDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("UserHandler at onGetClientDemandsWithOffers exception:\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<OfferDemandDetail> result) {
                eventBus.responseClientDemandsWithOffers(result);
            }
        });
    }

    public void onGetDemandOffers(long demandId, long threadRootId) {
        offerService.getDemandOffers(demandId, threadRootId, new AsyncCallback<ArrayList<OfferDetail>>() {
            @Override
            public void onFailure(Throwable caught) {
                Window.alert("UserHandler at getDemandOffers exception:\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<OfferDetail> offers) {
                eventBus.setDemandOffers(offers);
            }
        });
    }

}
