package cz.poptavka.sample.client.user.handler;

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
import cz.poptavka.sample.shared.domain.OfferDetail;
import cz.poptavka.sample.shared.domain.UserDetail;
import cz.poptavka.sample.shared.domain.demand.BaseDemandDetail;
import cz.poptavka.sample.shared.domain.demand.FullDemandDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;
import java.util.ArrayList;

@EventHandler
public class UserHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private UserRPCServiceAsync userService = null;
    @Inject
    private DemandRPCServiceAsync demandService = null;
    @Inject
    private OfferRPCServiceAsync offerService = null;

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

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
    public void onGetDemandDetail(Long demandId, final ViewType typeOfDetail) {
        if (typeOfDetail.equals(ViewType.EDITABLE)) {
            demandService.getFullDemandDetail(demandId, new AsyncCallback<FullDemandDetail>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(FullDemandDetail result) {
                    eventBus.setFullDemandDetail(result);
                }
            });
        }

        if (typeOfDetail.equals(ViewType.POTENTIAL)) {
            demandService.getBaseDemandDetail(demandId, new AsyncCallback<BaseDemandDetail>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert(caught.getMessage());
                }

                @Override
                public void onSuccess(BaseDemandDetail result) {
                    eventBus.setBaseDemandDetail(result);
                }
            });
        }
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
