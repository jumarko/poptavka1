package com.eprovement.poptavka.client.common.actionBox;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.ActionBoxRPCServiceAsync;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;

@EventHandler
public class ActionBoxHandler extends BaseEventHandler<ActionBoxEventBus> {

    @Inject
    private ActionBoxRPCServiceAsync rootService;

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
                eventBus.responseReadStatusUpdate();
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
                eventBus.responseStarStatusUpdate();
            }
        });
    }
}
