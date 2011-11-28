package cz.poptavka.sample.client.user.messages;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;

import cz.poptavka.sample.client.service.demand.MessageRPCServiceAsync;
import cz.poptavka.sample.client.user.UserEventBus;
import cz.poptavka.sample.domain.common.OrderType;
import cz.poptavka.sample.shared.domain.message.UserMessageDetail;
import java.util.List;
import java.util.Map;

@EventHandler
public class MessagesHandler extends BaseEventHandler<UserEventBus> {

    @Inject
    private MessageRPCServiceAsync messageService = null;

    /**********************************************************************************************
     ***********************  MESSAGE SECTION. *****************************************************
     **********************************************************************************************/
    public void onGetMessages(List<String> states, Map<String, OrderType> orderColumns, Boolean negation) {
        messageService.getMessagesByState(states, orderColumns, negation,
                new AsyncCallback<List<UserMessageDetail>>() {

                    @Override
                    public void onFailure(Throwable caught) {
                        throw new UnsupportedOperationException("Not supported yet.");
                    }

                    @Override
                    public void onSuccess(List<UserMessageDetail> result) {
                        eventBus.displayMessages(result);
                    }
                });
    }
}
