package com.eprovement.poptavka.client.user.handler;

import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.main.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.DemandRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.OfferRPCServiceAsync;

import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.client.user.UserEventBus;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.demand.BaseDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.FullOfferDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.ArrayList;

@EventHandler
public class UserHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private UserRPCServiceAsync userService = null;
    @Inject
    private DemandRPCServiceAsync demandService = null;
    @Inject
    private OfferRPCServiceAsync offerService = null;
    private ErrorDialogPopupView errorDialog;

    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);

    /**
     * Get User according to stored sessionID from DB after login.
     */
    public void onGetUser(long userId) {
        userService.getUserById(userId, new AsyncCallback<BusinessUserDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                eventBus.loadingHide();
                Window.alert("Error during getting logged User detail\n"
                        + caught.getMessage());
            }

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
            demandService.getFullDemandDetail(demandId, new AsyncCallback<FullDemandDetail>() {

                @Override
                public void onFailure(Throwable caught) {
                    if (caught instanceof RPCException) {
                        ExceptionUtils.showErrorDialog(errorDialog, caught);
                    }
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
                    if (caught instanceof RPCException) {
                        ExceptionUtils.showErrorDialog(errorDialog, caught);
                    }
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
        offerService.getDemandOffers(demandId, threadRootId, new AsyncCallback<ArrayList<FullOfferDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                Window.alert("UserHandler at getDemandOffers exception:\n\n" + caught.getMessage());
            }

            @Override
            public void onSuccess(ArrayList<FullOfferDetail> offers) {
                eventBus.setDemandOffers(offers);
            }
        });
    }
}
