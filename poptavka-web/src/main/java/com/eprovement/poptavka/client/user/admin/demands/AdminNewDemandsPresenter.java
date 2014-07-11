/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.demands;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGridBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdminModule.AdminWidget;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SetSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

/**
 * This presenter handles main actions of AdminNewDemands
 *
 * @author praso, Martin Slavkovsky
 */
@Presenter(view = AbstractAdminView.class)
public class AdminNewDemandsPresenter extends AbstractAdminPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
//    private AdminWidget mode;
    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Inits AdminDemands in New Demands mode.
     */
    public void onInitNewDemands(final SearchModuleDataHolder searchModuleDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_NEW_DEMANDS);
        initAbstractPresenter(searchModuleDataHolder, AdminWidget.NEW_DEMANDS);
        //Comented 11.7 by Martin - once logged as admin, click handler is registered
        //in order to reload new demands after submit (the demand is moved assigned demands)
        //But that click handler brings admin RPC call within it. Even in other roles
        //that have no rights to access admin methods ... access denied is thrown
        //...table won't reload automatically ... only manualy
//        eventBus.registerQuestionSubmitHandler(new ClickHandler() {
//
//            @Override
//            public void onClick(ClickEvent event) {
//                //Need to load again, cause I need replied demand to dissapear from new demands
//                //But reload only when in new demands
//                if (mode == AdminWidget.NEW_DEMANDS) {
//                    eventBus.goToAdminModule(searchModuleDataHolder, AdminWidget.NEW_DEMANDS);
//                }
//            }
//        });
    }

    /**
     * Inits AdminDemands in Assigend Demands mode.
     */
    public void onInitAssignedDemands(SearchModuleDataHolder searchModuleDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_ASSIGEND_DEMANDS);
        initAbstractPresenter(searchModuleDataHolder, AdminWidget.ASSIGNED_DEMANDS);
    }

    /**
     * Inits AdminDemands in Active Demands mode.
     */
    public void onInitActiveDemands(SearchModuleDataHolder searchModuleDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_ACTIVE_DEMANDS);
        initAbstractPresenter(searchModuleDataHolder, AdminWidget.ACTIVE_DEMANDS);
    }

    /**************************************************************************/
    /* Bind handlers                                                          */
    /**************************************************************************/
    /**
     * Events description.
     * - DataGrid.RangeChange
     *        - creates token when table page was changed
     *        - invoked when table page changed
     */
    @Override
    public void bindView() {
        super.bindView();
        addTableSelectionModelClickHandler();
        buttonClickHandlers();
    }

    /**************************************************************************/
    /* Bind helper methods                                                    */
    /**************************************************************************/
    /**
     * Bind table selection model handlers.
     */
    public void addTableSelectionModelClickHandler() {
        view.getTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                boolean isJustOneSelected = view.getSelectedObjects().size() == 1;
                //Sets Approve Btn visibility
                view.getToolbar().getApproveBtn().setVisible(
                    (mode == AdminWidget.NEW_DEMANDS || mode == AdminWidget.ASSIGNED_DEMANDS) && isJustOneSelected);
            }
        });
    }

    /**
     * Bind toolbar buttons handlers.
     */
    private void buttonClickHandlers() {
        view.getToolbar().getApproveBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestApproveDemands(view.getTable(), view.getSelectedObjects());
            }
        });
        view.getToolbar().getCreateConversationBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.requestCreateConversation(selectedObject.getDemandId(), Storage.getUser().getUserId());
                view.getToolbar().getCreateConversationBtn().setVisible(false);
            }
        });
    }

    /**************************************************************************/
    /* Business eventsods                                                     */
    /**************************************************************************/
    /**
     * Display newly created conversation to detail section.
     * @param threadRootId
     */
    public void onResponseCreateConversation(long threadRootId) {
        initDetailSectionConversation(selectedObject, threadRootId, selectedObject.getUserId());
    }

    /**
     * Allow Create Conversation feature if no conversation yet exist.
     * But if do, display it in detail section.
     * @param chatMessages
     */
    public void onResponseConversation(List<MessageDetail> chatMessages) {
        view.getToolbar().getCreateConversationBtn().setVisible(chatMessages.isEmpty());
        if (chatMessages.isEmpty()) {
            initDetailSectionDemand(selectedObject);
        } else {
            initDetailSectionConversation(selectedObject);
        }
    }

    /**
     * Refresh table and disable approve and createConversation btns.
     */
    public void onResponseApproveDemands() {
        ((SetSelectionModel) view.getTable().getSelectionModel()).clear();
        if (view.getTable().getDataProvider().getRanges().length <= 1) {
            view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        } else {
            view.getTable().refresh();
        }
        view.getToolbar().getApproveBtn().setVisible(false);
        view.getToolbar().getCreateConversationBtn().setVisible(false);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Inits table.
     * @return table
     */
    @Override
    protected UniversalAsyncGrid initTable() {
        return new UniversalAsyncGridBuilder<AdminDemandDetail>()
            .addColumnCheckbox(checkboxHeader)
            .addColumnDemandCreated(textFieldUpdater)
            .addColumnDemandTitle(textFieldUpdater)
            .addColumnLocality(textFieldUpdater)
            .addColumnUrgency()
            .addSelectionModel(new MultiSelectionModel(), AdminDemandDetail.KEY_PROVIDER)
            .addDefaultSort(Arrays.asList(SortPair.desc(DemandField.CREATED)))
            .addRowStyles(rowStyles)
            .build();
    }
}
