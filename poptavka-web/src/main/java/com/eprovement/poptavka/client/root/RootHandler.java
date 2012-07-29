package com.eprovement.poptavka.client.root;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.message.OfferMessageDetail;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.eprovement.poptavka.shared.exceptions.RPCException;
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
        rootService.getLocalities(localityType,
                new AsyncCallback<List<LocalityDetail>>() {

                    @Override
                    public void onSuccess(List<LocalityDetail> list) {
                        if (dataProvider != null) {
                            dataProvider.updateRowData(0, list);
                        }
                        eventBus.setLocalityData(localityType, list);
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }
                });
    }

    public void onGetChildLocalities(final LocalityType localityType, String locCode,
            final ListDataProvider dataProvider) {
        rootService.getLocalities(locCode,
                new AsyncCallback<List<LocalityDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                    }

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
    public void onGetRootCategories() {
        rootService.getCategories(new AsyncCallback<List<CategoryDetail>>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

            @Override
            public void onSuccess(List<CategoryDetail> list) {
                // eventBus.setCategoryDisplayData(CategoryType.ROOT,
                // list);
                Window.alert("fix this method return value"
                        + "onGetRootCategories() - MainHandler.class");
            }
        });
    }

    public void onGetChildListCategories(final int newListPosition,
            String categoryId) {
        LOGGER.info("starting category service call");
        if (categoryId.equals("ALL_CATEGORIES")) {
            LOGGER.info(" --> root categories");
            rootService.getCategories(new AsyncCallback<List<CategoryDetail>>() {

                @Override
                public void onFailure(Throwable caught) {
                    if (caught instanceof RPCException) {
                        ExceptionUtils.showErrorDialog(errorDialog, caught);
                    }
                }

                @Override
                public void onSuccess(List<CategoryDetail> list) {
                    eventBus.setCategoryListData(newListPosition, list);
                }
            });
        } else {
            LOGGER.info(" --> child categories");
            rootService.getCategoryChildren(Long.valueOf(categoryId),
                    new AsyncCallback<List<CategoryDetail>>() {

                        @Override
                        public void onSuccess(List<CategoryDetail> list) {
                            eventBus.setCategoryListData(newListPosition, list);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            if (caught instanceof RPCException) {
                                ExceptionUtils.showErrorDialog(errorDialog, caught);
                            }
                        }
                    });
        }
        LOGGER.info("ending category service call");
    }

    /**************************************************************************/
    /* User methods                                                           */
    /**************************************************************************/
    /**
     * Get User according to stored sessionID from DB after login.
     */
    public void onGetUser(long userId) {
        rootService.getUserById(userId, new AsyncCallback<BusinessUserDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                eventBus.loadingHide();
                Window.alert("Error during getting logged User detail\n"
                        + caught.getMessage());
                //Set layouts back when unsuccess login.
                //TODO Martin - not good aproach in my opinion, onAccount method
                //should first try to login and then change layouts
                History.back();
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
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
        rootService.getFullDemandDetail(demandId, new AsyncCallback<FullDemandDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in DemandModuleHandler in method: onRequestDemandDetail"
                        + caught.getMessage());
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

            @Override
            public void onSuccess(FullDemandDetail result) {
                eventBus.responseDemandDetail(result, type);
            }
        });
    }

    public void onRequestSupplierDetail(Long supplierId, final ViewType type) {
        rootService.getFullSupplierDetail(supplierId, new AsyncCallback<FullSupplierDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("Error in DemandModuleHandler in method: onRequestDemandDetail"
                        + caught.getMessage());
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
            }

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
                new AsyncCallback<List<MessageDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        if (caught instanceof RPCException) {
                            ExceptionUtils.showErrorDialog(errorDialog, caught);
                        }
                        Window.alert("DemandModuleMessageHandler: onRequestConversationForSupplierList:\n\n"
                                + caught.getMessage());
                    }

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
        rootService.sendQuestionMessage(messageToSend, new AsyncCallback<MessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                Window.alert("DemandModuleMessageHandler: onSendMessage:\n\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(MessageDetail sentMessage) {
                eventBus.addConversationMessage(sentMessage, type);
            }
        });
    }

    public void onSendOfferMessage(OfferMessageDetail offerMessageToSend, final ViewType type) {
        rootService.sendOfferMessage(offerMessageToSend, new AsyncCallback<OfferMessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                if (caught instanceof RPCException) {
                    ExceptionUtils.showErrorDialog(errorDialog, caught);
                }
                Window.alert("DemandModuleMessageHandler: onSendMessage:\n\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(OfferMessageDetail sentMessage) {
                //Zobrazit offer spravu tiez v konverzacii, alebo ta sa zobrazli
                //len v SupplierContests???
//                eventBus.addConversationMessage(sentMessage, type);
            }
        });
    }
}
