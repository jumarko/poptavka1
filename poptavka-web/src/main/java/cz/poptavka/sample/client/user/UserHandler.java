package cz.poptavka.sample.client.user;

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
import cz.poptavka.sample.client.service.demand.UserRPCServiceAsync;
import cz.poptavka.sample.shared.domain.DemandDetail;
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.DetailType;

@EventHandler
public class UserHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private DemandRPCServiceAsync demandService = null;
    @Inject
    private UserRPCServiceAsync userService = null;


    private static final LocalizableMessages MSGS = GWT
            .create(LocalizableMessages.class);

    public void onGetClientsDemands(Long id) {
        demandService.getClientDemands(id,
                new AsyncCallback<ArrayList<DemandDetail>>() {

                    @Override
                    public void onSuccess(ArrayList<DemandDetail> list) {
                        eventBus.setClientDemands(list);
                        eventBus.loadingHide();
                    }

                    @Override
                    public void onFailure(Throwable exc) {
                        eventBus.loadingHide();
                        Window.alert(exc.getMessage());
                    }
                });
    }

    public void onRequestOffers(ArrayList<Long> idList) {
        demandService.getDemandOffers(idList,
                new AsyncCallback<ArrayList<ArrayList<OfferDetail>>>() {

                    @Override
                    public void onFailure(Throwable arg0) {
                    }

                    @Override
                    public void onSuccess(
                            ArrayList<ArrayList<OfferDetail>> result) {
                        eventBus.responseOffers(result);
                    }
                });
    }

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
     * Get User according to stored sessionID from DB after login
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
                // TODO
                Window.alert(caught.getMessage());
            }

            @Override
            public void onSuccess(DemandDetail result) {
                eventBus.setDemandDetail(result, typeOfDetail);
            }
        });

    }
}
