/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin;

import com.eprovement.poptavka.client.common.security.GetDataCallback;
import com.eprovement.poptavka.client.common.security.GetDataCountCallback;
import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.admin.AdminRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.UnreadMessagesDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.EventHandler;
import com.mvp4g.client.event.BaseEventHandler;
import java.util.List;
import java.util.Set;

/**
 * Handle RPC calls for Admin module.
 *
 * @author Martin Slavkovsky
 */
@EventHandler
public class AdminHandler extends BaseEventHandler<AdminEventBus> {

    /**************************************************************************/
    /* Inject RPC services                                                    */
    /**************************************************************************/
    @Inject
    private AdminRPCServiceAsync adminService = null;

    //*************************************************************************/
    // Overriden methods of IEventBusData interface.                          */
    //*************************************************************************/
    /**
     * Request for table data count.
     * @param grid - table
     * @param searchDefinition -search criteria
     */
    public void onGetDataCount(final UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.ADMIN_NEW_DEMANDS:
                getAdminNewDemandsCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_ASSIGEND_DEMANDS:
                getAdminAssignedDemandsCount(grid, searchDefinition);
                break;
            case Constants.ADMIN_ACTIVE_DEMANDS:
                getAdminActiveDemandsCount(grid, searchDefinition);
                break;
            default:
                break;
        }
    }

    /**
     * Request for table data.
     * @param grid - table
     * @param searchDefinition -search criteria
     */
    public void onGetData(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        switch (Storage.getCurrentlyLoadedView()) {
            case Constants.ADMIN_NEW_DEMANDS:
                getAdminNewDemands(grid, searchDefinition, requestId);
                break;
            case Constants.ADMIN_ASSIGEND_DEMANDS:
                getAdminAssignedDemands(grid, searchDefinition, requestId);
                break;
            case Constants.ADMIN_ACTIVE_DEMANDS:
                getAdminActiveDemands(grid, searchDefinition, requestId);
                break;
            default:
                break;
        }
    }

    /**********************************************************************************************
     ***********************  Assigned Demands SECTION. *******************************************
     **********************************************************************************************/
    public void getAdminAssignedDemandsCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        adminService.getAdminAssignedDemandsCount(
            Storage.getUser().getUserId(), searchDefinition, DemandStatus.NEW,
            new GetDataCountCallback(eventBus, grid));
    }

    public void getAdminAssignedDemands(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        adminService.getAdminAssignedDemands(
            Storage.getUser().getUserId(), DemandStatus.NEW, searchDefinition,
            new GetDataCallback<AdminDemandDetail>(eventBus, grid, requestId));
    }

    /**********************************************************************************************
     ***********************  Active Demands SECTION. *********************************************
     **********************************************************************************************/
    public void getAdminActiveDemandsCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        adminService.getAdminActiveDemandsCount(searchDefinition,
            new GetDataCountCallback(eventBus, grid));
    }

    public void getAdminActiveDemands(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        adminService.getAdminActiveDemands(searchDefinition,
            new GetDataCallback<AdminDemandDetail>(eventBus, grid, requestId));
    }

    /**********************************************************************************************
     ***********************  New Demands SECTION. ************************************************
     **********************************************************************************************/
    public void getAdminNewDemandsCount(UniversalAsyncGrid grid, SearchDefinition searchDefinition) {
        adminService.getAdminNewDemandsCount(new GetDataCountCallback(eventBus, grid));
    }

    public void getAdminNewDemands(UniversalAsyncGrid grid, SearchDefinition searchDefinition, int requestId) {
        adminService.getAdminNewDemands(searchDefinition,
            new GetDataCallback<AdminDemandDetail>(eventBus, grid, requestId));
    }

    public void onRequestApproveDemands(UniversalAsyncGrid grid, Set<AdminDemandDetail> demandsToApprove) {
        adminService.approveDemands(demandsToApprove, new SecuredAsyncCallback<Void>(eventBus) {
            @Override
            public void onSuccess(Void result) {
                eventBus.responseApproveDemands();
            }
        });
    }

    public void onRequestConversation(long threadRootId, long loggedUserId, long counterPartyUserId) {
        adminService.getConversation(threadRootId, loggedUserId, counterPartyUserId,
            new SecuredAsyncCallback<List<MessageDetail>>(eventBus) {
                @Override
                public void onSuccess(List<MessageDetail> result) {
                    eventBus.responseConversation(result);
                }
            });
    }

    public void onUpdateUnreadMessagesCount() {
        adminService.updateUnreadMessagesCount(new SecuredAsyncCallback<UnreadMessagesDetail>(eventBus) {
            @Override
            public void onSuccess(UnreadMessagesDetail result) {
                eventBus.setUpdatedUnreadMessagesCount(result);
            }
        });
    }

    /**************************************************************************/
    /* DevelDetailWrapper widget methods                                      */
    /**************************************************************************/
    /**
     * Creates conversation between <code>Client</code> and Admin/Operator user. Conversation is created in such a
     * way that new <code>UserMessage</code> is created for every <code>User</code> who invokes this method. Thus
     * enabling the user to write a reply message to <code>Client</code> in order to update <code>Demand</code>
     * description or title before this demand is approved.
     *
     * @param demandId for which the conversation is created
     * @param userAdminId id of operator or admin user
     */
    public void onRequestCreateConversation(long demandId, long userId) {
        adminService.createConversation(demandId, userId,
            new SecuredAsyncCallback<Long>(eventBus) {
                @Override
                public void onSuccess(Long result) {
                    eventBus.responseCreateConversation(result);
                }
            });
    }
}
