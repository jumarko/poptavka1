package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.RootRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.LocalityDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.domain.type.ViewType;
import com.google.gwt.core.client.GWT;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.ListDataProvider;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@EventHandler
public class RootHandler extends BaseEventHandler<RootEventBus> {

    @Inject
    private RootRPCServiceAsync rootService;
    @Inject
    private UserRPCServiceAsync userService;
    private static final Logger LOGGER = Logger.getLogger("RootHandler");


    /*
     * Localities methods
     */
    public void onGetLocalities(final LocalityType localityType, final AsyncDataProvider dataProvider) {
        rootService.getLocalities(localityType, new SecuredAsyncCallback<List<LocalityDetail>>(eventBus) {
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
        rootService.getLocalities(locCode, new SecuredAsyncCallback<List<LocalityDetail>>(eventBus) {
            @Override
            public void onSuccess(List<LocalityDetail> list) {
                if (dataProvider != null) {
                    dataProvider.setList(list);
                }
                eventBus.setLocalityData(localityType, list);
            }
        });
    }

    /*
     * Categories methods
     */
    public void onGetRootCategories(final AsyncDataProvider dataProvider) {
        rootService.getCategories(new SecuredAsyncCallback<List<CategoryDetail>>(eventBus) {
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
        rootService.getCategoryChildren(categoryId, new SecuredAsyncCallback<List<CategoryDetail>>(eventBus) {
            @Override
            public void onSuccess(List<CategoryDetail> list) {
                if (dataProvider != null) {
                    dataProvider.setList(list);
                }
                eventBus.setCategoryData(list);
            }
        });
    }

    /*
     * DevelDetailWrapper widget methods
     */
    public void onRequestDemandDetail(Long demandId, final ViewType type) {
        rootService.getFullDemandDetail(demandId, new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDemandDetail(result, type);
            }
        });
    }

    public void onRequestSupplierDetail(Long supplierId, final ViewType type) {
        rootService.getFullSupplierDetail(supplierId, new SecuredAsyncCallback<FullSupplierDetail>(eventBus) {
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
        rootService.getConversation(messageId, userId, userMessageId,
                new SecuredAsyncCallback<List<MessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<MessageDetail> result) {
                        eventBus.responseConversation(result, ViewType.POTENTIAL);
                    }
                });
    }

    /*
     * Messages methods
     */
    /**
     * Changes demands Read status. Changes are displayed immediately on frontend. No onSuccess code is needed.
     *
     * @param selectedIdList list of demands which read status should be changed
     * @param newStatus of demandList
     */
    public void onRequestReadStatusUpdate(List<Long> selectedIdList, boolean newStatus) {
        rootService.setMessageReadStatus(selectedIdList, newStatus, new SecuredAsyncCallback<Void>(eventBus) {
            @Override
            public void onSuccess(Void result) {
                //Empty by default
            }
        });
    }
    /**
     * Send message. IMPORTANT: further implementation of other parts will show, if we need more than this method for
     * chat related stuff
     *
     * @param messageToSend
     * @param type
     */
    public void onSendQuestionMessage(MessageDetail messageToSend, final ViewType type) {
        rootService.sendQuestionMessage(messageToSend, new SecuredAsyncCallback<MessageDetail>(eventBus) {
            @Override
            public void onSuccess(MessageDetail sentMessage) {
                eventBus.addConversationMessage(sentMessage, type);
            }
        });
    }

    public void onSendOfferMessage(OfferMessageDetail offerMessageToSend, final ViewType type) {
        rootService.sendOfferMessage(offerMessageToSend, new SecuredAsyncCallback<MessageDetail>(eventBus) {
            @Override
            public void onSuccess(MessageDetail sentMessage) {
                //Zobrazit offer spravu tiez v konverzacii, alebo ta sa zobrazli
                //len v SupplierContests???
                // TODO martin - presmerovat po poslani ponuky do noveho okna myOffers
                eventBus.addConversationMessage(sentMessage, type);
                GWT.log("Offer message [messageId=" + sentMessage.getMessageId()
                        + "]  has been successfully sent to client");
            }
        });
    }

    /**
     * This method populates Storage i.e. our custom GWT session object with UserDetail.
     * A secured RPC service is invoked so this method is succesfylly called only if user is logged in and he opened our
     * website in new browser tab, which obviously starts the whole app from the begining.
     * If user is not logged in the RPC service will cause the initiation of loginPopupView via SecuredAsyncCallback.
     */
    public void onLoginFromSession() {
        userService.getLoggedUser(new SecuredAsyncCallback<UserDetail>(eventBus) {
            @Override
            public void onSuccess(UserDetail userDetail) {
                Storage.setUserDetail(userDetail);
                userService.getLoggedBusinessUser(new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
                    @Override
                    public void onSuccess(BusinessUserDetail businessUserDetail) {
                        Storage.setBusinessUserDetail(businessUserDetail);
                        GWT.log("login from session,  user id " + businessUserDetail.getUserId());
                        forwardUser();
                    }
                });

            }
        });
    }

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    public void onActivateUser(String activationCode) {
        // TODO martin review: whole (new) activation related logic and also "SentActivationCodeAgain" process
        rootService.activateClient(activationCode, new SecuredAsyncCallback<UserActivationResult>(eventBus) {
            @Override
            public void onSuccess(UserActivationResult result) {
                eventBus.responseActivateUser(result);
            }
        });
    }


    public void onSendActivationCodeAgain(BusinessUserDetail client) {
        rootService.sendActivationCodeAgain(client, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean activationResult) {
                eventBus.responseSendActivationCodeAgain(activationResult);
            }
        });
    }

    /**************************************************************************/
    /* History helper methods                                                 */
    /**************************************************************************/
    /**
     * Set account layout and forward user to appropriate module according to his role.
     * Called by loginFromSession from HistoryConverter.
     */
    private void forwardUser() {
        //Set account layout
        eventBus.atAccount();

        //forward user to welcome view of appropriate module according to his roles
        if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.ADMIN)) {
            eventBus.goToAdminModule(null, Constants.NONE);
        } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.SUPPLIER)) {
            eventBus.goToSupplierDemandsModule(null, Constants.NONE);
        } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                BusinessUserDetail.BusinessRole.CLIENT)) {
            eventBus.goToClientDemandsModule(null, Constants.NONE);
        }
    }

    public void onGetServices() {
        rootService.getSupplierServices(new SecuredAsyncCallback<ArrayList<ServiceDetail>>(eventBus) {
            @Override
            public void onSuccess(ArrayList<ServiceDetail> data) {
                eventBus.setServices(data);
            }
        });
    }
}
