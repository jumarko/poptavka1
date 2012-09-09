package com.eprovement.poptavka.client.user.handler;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.root.email.EmailDialogPopupView;
import com.eprovement.poptavka.client.service.demand.OfferRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.client.user.UserEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

@EventHandler
public class UserHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private UserRPCServiceAsync userService = null;
    @Inject
    private OfferRPCServiceAsync offerService = null;
    private EmailDialogPopupView errorDialog;

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    /**
     * Get User according to stored sessionID from DB after login.
     */
    public void onGetUser(long userId) {
        userService.getUserById(userId, new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
            @Override
            public void onSuccess(BusinessUserDetail result) {
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
            // TODO Martin check and remove
//            demandService.getFullDemandDetail(demandId, new SecuredAsyncCallback<FullDemandDetail>() {
//                @Override
//                public void onSuccess(FullDemandDetail result) {
//                    eventBus.setFullDemandDetail(result);
//                }
//            });
        }

        if (typeOfDetail.equals(ViewType.POTENTIAL)) {
            // TODO Martin check and remove
//            demandService.getBaseDemandDetail(demandId, new SecuredAsyncCallback<BaseDemandDetail>() {
//                @Override
//                public void onSuccess(BaseDemandDetail result) {
//                    eventBus.setBaseDemandDetail(result);
//                }
//            });
        }
    }

    public void onGetDemandOffers(long demandId, long threadRootId) {
//        offerService.getDemandOffers(demandId, threadRootId, new SecuredAsyncCallback<ArrayList<FullOfferDetail>>() {
//            @Override
//            public void onSuccess(ArrayList<FullOfferDetail> offers) {
//                eventBus.setDemandOffers(offers);
//            }
//        });
    }
}
