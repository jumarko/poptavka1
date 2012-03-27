package cz.poptavka.sample.client.user.demands.handler;

import java.util.ArrayList;

import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.DemandsRPCServiceAsync;
import cz.poptavka.sample.client.user.demands.DemandEventBus;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.type.ViewType;

/**
 * TODO Praso - Preco mame dve Handler triedy pre modul Demands?
 * 1) DemandModuleMessageHandler.java
 * 2) DemandModuleContentHandler.java
 * Ked sa nacita modul Demands prvy krat tak sa stiahnu zo servera oba subory naraz. Preto
 * mi nedava vyznam aby sme kvoli 2 metodam tykajucih sa len tohto modulu vytvarali novy
 * handler.
 * @author Praso
 */
@EventHandler
public class DemandMessageHandler extends BaseEventHandler<DemandEventBus> {

//    @Inject
//    private MessageRPCServiceAsync messageService;
    @Inject
    private DemandsRPCServiceAsync demandsService;

    /**
     * Load demand/related conversation from DB.
     *
     * @param messageId
     * @param userMessageId
     * @param userId
     */
    public void onRequestChatForSupplierList(long messageId, Long userMessageId, Long userId) {
        demandsService.loadSuppliersPotentialDemandConversation(messageId, userId, userMessageId,
                new AsyncCallback<ArrayList<MessageDetail>>() {

                @Override
                public void onFailure(Throwable caught) {
                    Window.alert("DemandModuleMessageHandler: onRequestConversationForSupplierList:\n\n"
                            + caught.getMessage());
                }

                @Override
                public void onSuccess(ArrayList<MessageDetail> result) {
                    eventBus.responseChatForSupplierList(result, ViewType.POTENTIAL);
                }
            });
    }


    /**
     * Send message.
     * IMPORTANT: further implementation of other parts will show, if we need more than this method
     * for chat related stuff
     * @param messageToSend
     * @param type
     */
    public void onSendMessage(MessageDetail messageToSend, final ViewType type) {
        demandsService.sendQueryToPotentialDemand(messageToSend, new AsyncCallback<MessageDetail>() {

            @Override
            public void onFailure(Throwable caught) {
                Window.alert("DemandModuleMessageHandler: onSendMessage:\n\n"
                        + caught.getMessage());
            }

            @Override
            public void onSuccess(MessageDetail sentMessage) {
                eventBus.sendMessageResponse(sentMessage, type);
            }
        });
    }
}
