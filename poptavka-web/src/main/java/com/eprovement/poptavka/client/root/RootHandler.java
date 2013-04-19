package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.client.common.CommonAccessRoles;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.RootRPCServiceAsync;
import com.eprovement.poptavka.client.service.demand.UserRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.ServiceDetail;
import com.eprovement.poptavka.shared.domain.UserDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import com.eprovement.poptavka.shared.domain.root.UserActivationResult;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.google.gwt.core.client.GWT;
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
     * DevelDetailWrapper widget methods
     */
    public void onRequestDemandDetail(Long demandId) {
        rootService.getFullDemandDetail(demandId, new SecuredAsyncCallback<FullDemandDetail>(eventBus) {
            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDemandDetail(result);
            }
        });
    }

    public void onRequestClientDetail(Long clientId) {
        rootService.getFullClientDetail(clientId, new SecuredAsyncCallback<FullClientDetail>(eventBus) {
            @Override
            public void onSuccess(FullClientDetail result) {
                eventBus.responseClientDetail(result);
            }
        });
    }

    public void onRequestSupplierDetail(Long supplierId) {
        rootService.getFullSupplierDetail(supplierId, new SecuredAsyncCallback<FullSupplierDetail>(eventBus) {
            @Override
            public void onSuccess(FullSupplierDetail result) {
                eventBus.responseSupplierDetail(result);
            }
        });
    }

    /**
     * Load conversation between client and supplier related to particular demand / threadRoot
     *
     * @param threadId
     * @param userId
     */
    public void onRequestConversation(Long threadId, final Long userId) {
        rootService.getConversation(threadId, userId,
                new SecuredAsyncCallback<List<MessageDetail>>(eventBus) {
                    @Override
                    public void onSuccess(List<MessageDetail> result) {
                        eventBus.responseConversation(result);
                    }
                });
    }

    /**
     * Update isRead status of all messages for given User.
     *
     * @param userId user whose UserMessages will be udpated
     * @param messages messages to be updated as read
     */
    public void onUpdateUserMessagesReadStatus(Long userId, List<MessageDetail> messages) {
        rootService.updateUserMessagesReadStatus(userId, messages,
                new SecuredAsyncCallback<Void>(eventBus) {
                    @Override
                    public void onSuccess(Void result) {
                        // userMessages in DB are updated thus there is no need to invoke other methods.
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
     * Changes demands star status. Changes are displayed immediately on frontend. No onSuccess code is needed.
     *
     * @param userMessageIdList list od demands which star status should be changed
     * @param newStatus of demandList
     */
    public void onRequestStarStatusUpdate(List<Long> userMessageIdList, boolean newStatus) {
        rootService.setMessageStarStatus(userMessageIdList, newStatus, new SecuredAsyncCallback<Void>(eventBus) {
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
     */
    public void onSendQuestionMessage(MessageDetail messageToSend) {
        rootService.sendQuestionMessage(messageToSend, new SecuredAsyncCallback<MessageDetail>(eventBus) {
            @Override
            public void onSuccess(MessageDetail sentMessage) {
                eventBus.addConversationMessage(sentMessage);
            }
        });
    }

    public void onSendOfferMessage(OfferMessageDetail offerMessageToSend) {
        rootService.sendOfferMessage(offerMessageToSend, new SecuredAsyncCallback<MessageDetail>(eventBus) {
            @Override
            public void onSuccess(MessageDetail sentMessage) {
                GWT.log("Offer message [messageId=" + sentMessage.getMessageId()
                        + "]  has been successfully sent to client");
                eventBus.addConversationMessage(sentMessage);
                eventBus.responseSendOfferMessage();
            }
        });
    }

    /**
     * This method populates Storage i.e. our custom GWT session object with UserDetail.
     * A secured RPC service is invoked so this method is succesfylly called only if user is logged in and he opened our
     * website in new browser tab, which obviously starts the whole app from the begining.
     * If user is not logged in the RPC service will cause the initiation of loginPopupView via SecuredAsyncCallback.
     */
    public void onLoginFromSession(final int widgetToLoad) {
        userService.getLoggedUser(new SecuredAsyncCallback<UserDetail>(eventBus) {
            @Override
            public void onSuccess(UserDetail userDetail) {
                Storage.setUserDetail(userDetail);
                userService.getLoggedBusinessUser(new SecuredAsyncCallback<BusinessUserDetail>(eventBus) {
                    @Override
                    public void onSuccess(BusinessUserDetail businessUserDetail) {
                        Storage.setBusinessUserDetail(businessUserDetail);
                        Storage.loadClientAndSupplierIDs();
                        GWT.log("login from session,  user id " + businessUserDetail.getUserId());
                        forwardUser(widgetToLoad);
                    }
                });

            }
        });
    }

    /**************************************************************************/
    /* Activation methods                                                     */
    /**************************************************************************/
    public void onActivateUser(BusinessUserDetail user, String activationCode) {
        // TODO RELEASE martin review: whole (new) activation related logic and also "SentActivationCodeAgain" process
        rootService.activateUser(user, activationCode, new SecuredAsyncCallback<UserActivationResult>(eventBus) {
            @Override
            public void onSuccess(UserActivationResult result) {
                eventBus.responseActivateUser(result);
            }
        });
    }

    public void onSendActivationCodeAgain(BusinessUserDetail user) {
        rootService.sendActivationCodeAgain(user, new SecuredAsyncCallback<Boolean>(eventBus) {
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
    private void forwardUser(int widgetToLoad) {
        //Set account layout
        eventBus.atAccount();

        GWT.log("BUSSINESS ROLES ++++ " + Storage.getBusinessUserDetail().getBusinessRoles().toString());
        GWT.log("ACCESS ROLES ++++ " + Storage.getUser().getAccessRoles().toString());
        //If exact module is known to be loaded, do it
        switch (widgetToLoad) {
            case Constants.SKIP:
                return;
            case Constants.USER_SETTINGS_MODULE:
                eventBus.goToSettingsModule();
                break;
            case Constants.HOME_DEMANDS_MODULE:
                eventBus.goToHomeDemandsModule(null);
                break;
            case Constants.HOME_SUPPLIERS_MODULE:
                eventBus.goToHomeSuppliersModule(null);
                break;
            case Constants.CREATE_DEMAND:
                eventBus.goToCreateDemandModule();
                break;
            case Constants.CREATE_SUPPLIER:
                eventBus.goToCreateSupplierModule();
                break;
            case Constants.MESSAGES_INBOX:
                eventBus.goToMessagesModule(null, Constants.MESSAGES_INBOX);
                break;
            case Constants.USER_ADMININSTRATION_MODULE:
                eventBus.goToAdminModule(null, Constants.NONE);
                break;
            default:
                //otherwise forward user to welcome view of appropriate module according to his roles
                if (Storage.getUser().getAccessRoles().contains(CommonAccessRoles.ADMIN)) {
                    eventBus.goToAdminModule(null, widgetToLoad);
                } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                        BusinessUserDetail.BusinessRole.SUPPLIER)) {
                    eventBus.goToSupplierDemandsModule(null, widgetToLoad);
                } else if (Storage.getBusinessUserDetail().getBusinessRoles().contains(
                        BusinessUserDetail.BusinessRole.CLIENT)) {
                    eventBus.goToClientDemandsModule(null, widgetToLoad);
                }
                break;
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

    /**************************************************************************/
    /* Account Info                                                           */
    /**************************************************************************/
    public void onCheckFreeEmail(String email) {
        rootService.checkFreeEmail(email, new SecuredAsyncCallback<Boolean>(eventBus) {
            @Override
            public void onSuccess(Boolean result) {
                LOGGER.fine("result of compare " + result);
                eventBus.checkFreeEmailResponse(result);
            }
        });
    }
}
