package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.common.SecuredAsyncCallback;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.common.errorDialog.ErrorDialogPopupView;
import com.eprovement.poptavka.client.service.demand.RootRPCServiceAsync;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.google.gwt.user.client.History;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.ListDataProvider;

@EventHandler
public class RootHandler extends BaseEventHandler<RootEventBus> {

    @Inject
    private RootRPCServiceAsync rootService;
    private ErrorDialogPopupView errorDialog;
    private static final Logger LOGGER = Logger.getLogger("RootHandler");

    /**************************************************************************/
    /* Localities methods                                                     */
    /**************************************************************************/
    public void onGetLocalities(final LocalityType localityType, final AsyncDataProvider dataProvider) {
        rootService.getLocalities(localityType, new SecuredAsyncCallback<List<LocalityDetail>>() {

            @Override
            public void onSuccess(List<LocalityDetail> list) {
                if (dataProvider != null) {
                    dataProvider.updateRowData(0, list);
                }
                eventBus.setLocalityData(localityType, list);
            }
        });
    }

    public void onGetChildLocalities(final LocalityType localityType, String locCode,
            final ListDataProvider dataProvider) {
        rootService.getLocalities(locCode, new SecuredAsyncCallback<List<LocalityDetail>>() {
            @Override
            public void onSuccess(List<LocalityDetail> list) {
                if (dataProvider != null) {
                    dataProvider.setList(list);
                }
                eventBus.setLocalityData(localityType, list);
            }
        });
    }

    /**************************************************************************/
    /* Categories methods                                                     */
    /**************************************************************************/
    public void onGetRootCategories(final AsyncDataProvider dataProvider) {
        rootService.getCategories(new SecuredAsyncCallback<List<CategoryDetail>>() {
            @Override
            public void onSuccess(List<CategoryDetail> list) {
                if (dataProvider != null) {
                    dataProvider.updateRowData(0, list);
                }
                eventBus.setCategoryData(list);
            }
        });
    }

    public void onGetChildCategories(long categoryId, final ListDataProvider dataProvider) {
        rootService.getCategoryChildren(categoryId, new SecuredAsyncCallback<List<CategoryDetail>>() {

            @Override
            public void onSuccess(List<CategoryDetail> list) {
                if (dataProvider != null) {
                    dataProvider.setList(list);
                }
                eventBus.setCategoryData(list);
            }
        });
    }

    /**************************************************************************/
    /* User methods                                                           */
    /**************************************************************************/
    /**
     * Get User according to stored sessionID from DB after login.
     */
    public void onGetUser(long userId) {
        rootService.getUserById(userId, new SecuredAsyncCallback<BusinessUserDetail>() {

            @Override
            protected void onServiceFailure(Throwable caught) {
                // TODO: review this failure handling code
                eventBus.loadingHide();
                Window.alert("Error during getting logged User detail\n"
                        + caught.getMessage());
                //Set layouts back when unsuccess login.
                //TODO Martin - not good aproach in my opinion, onAccount method
                //should first try to login and then change layouts
                History.back();
            }

            @Override
            public void onSuccess(BusinessUserDetail result) {
                eventBus.loadingShow(Storage.MSGS.progressCreatingUserInterface());
                Storage.setUser(result);
                eventBus.loadingHide();
//                eventBus.setUser(result);
            }
        });
    }

    /**************************************************************************/
    /* DevelDetailWrapper widget methods                                      */
    /**************************************************************************/
    public void onRequestDemandDetail(Long demandId, final ViewType type) {
        rootService.getFullDemandDetail(demandId, new SecuredAsyncCallback<FullDemandDetail>() {
            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDemandDetail(result, type);
            }
        });
    }

    public void onRequestSupplierDetail(Long supplierId, final ViewType type) {
        rootService.getFullSupplierDetail(supplierId, new SecuredAsyncCallback<FullSupplierDetail>() {
            @Override
            public void onSuccess(FullSupplierDetail result) {
                eventBus.responseSupplierDetail(result, type);
            }
        });
    }

    /**
     * Load demand/related conversation from DB.
     *
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    public void onRequestConversation(long messageId, Long userMessageId, Long userId) {
        rootService.getConversation(messageId, userId, userMessageId, new SecuredAsyncCallback<List<MessageDetail>>() {
            @Override
            public void onSuccess(List<MessageDetail> result) {
                eventBus.responseConversation(result, ViewType.POTENTIAL);
            }
        });
    }

    /**************************************************************************/
    /* Messages methods                                                       */
    /**************************************************************************/
    /**
     * Send message.
     * IMPORTANT: further implementation of other parts will show, if we need more than this method
     * for chat related stuff
     * @param messageToSend
     * @param type
     */
    public void onSendQuestionMessage(MessageDetail messageToSend, final ViewType type) {
        rootService.sendQuestionMessage(messageToSend, new SecuredAsyncCallback<MessageDetail>() {
            @Override
            public void onSuccess(MessageDetail sentMessage) {
                eventBus.addConversationMessage(sentMessage, type);
            }
        });
    }

    public void onSendOfferMessage(OfferMessageDetail offerMessageToSend, final ViewType type) {
        rootService.sendOfferMessage(offerMessageToSend, new SecuredAsyncCallback<OfferMessageDetail>() {
            @Override
            public void onSuccess(OfferMessageDetail sentMessage) {
                //Zobrazit offer spravu tiez v konverzacii, alebo ta sa zobrazli
                //len v SupplierContests???
//                eventBus.addConversationMessage(sentMessage, type);
            }
        });
    }
}
