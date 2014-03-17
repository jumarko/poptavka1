/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.admin.demand;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.admin.interfaces.IAdmin.AdminWidget;
import com.eprovement.poptavka.client.user.admin.tab.AbstractAdminPresenter;
import com.eprovement.poptavka.client.user.admin.tab.AbstractAdminView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.TableDisplayDetailModule;
import com.eprovement.poptavka.shared.domain.adminModule.AdminDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
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
    private AdminWidget mode;

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    
    /**
     * Inits AdminDemands in New Demands mode.
     */
    public void onInitNewDemands(SearchModuleDataHolder searchModuleDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_NEW_DEMANDS);
        init(searchModuleDataHolder, AdminWidget.NEW_DEMANDS);
    }

    /**
     * Inits AdminDemands in Assigend Demands mode.
     */
    public void onInitAssignedDemands(SearchModuleDataHolder searchModuleDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_ACTIVE_DEMANDS);
        init(searchModuleDataHolder, AdminWidget.ASSIGNED_DEMANDS);
    }

    /**
     * Inits AdminDemands in Active Demands mode.
     */
    public void onInitActiveDemands(SearchModuleDataHolder searchModuleDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.ADMIN_ACTIVE_DEMANDS);
        init(searchModuleDataHolder, AdminWidget.ACTIVE_DEMANDS);
    }

    private void init(SearchModuleDataHolder searchModuleDataHolder, AdminWidget mode) {
        if (searchModuleDataHolder == null) {
            eventBus.resetSearchBar(null);
        }
        this.mode = mode;
        eventBus.setClientMenuActStyle(mode);
        eventBus.initDetailSection(view.getTable(), view.getDetailPanel());
        eventBus.setFooter(view.getFooterContainer());
        searchDataHolder = searchModuleDataHolder;
        view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
//        view.getWidgetView().setStyleName(Storage.RSCS.common().userContent());
        eventBus.displayView(view.getWidgeView());
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
                view.getToolbar().getApproveBtn().setVisible(view.getSelectedObjects().size() == 1);
                view.getToolbar().getCreateConversationBtn().setVisible(false);
                //  Request for conversation, if doesn't exist yet, create conversation feature will be allowed.
                eventBus.requestConversation(
                    selectedObject.getThreadRootId(), selectedObject.getSenderId(), Storage.getUser().getUserId());
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
                eventBus.requestCreateConversation(
                    ((TableDisplayDetailModule) view.getSelectedObjects().iterator().next()).getDemandId());
            }
        });
    }

    /**************************************************************************/
    /* Business eventsods                                                     */
    /**************************************************************************/
    /**
     * Display demands of selected category.
     * @param list
     */
    public void onDisplayAdminNewDemands(List<AdminDemandDetail> list) {
        view.getTable().getDataProvider().updateRowData(view.getTable().getStart(), list);
    }

    /**
     * Display newly created conversation to detail section.
     * @param threadRootId
     */
    public void onResponseCreateConversation(long threadRootId) {
        initDetailSectionConversation((TableDisplayDetailModule) view.getSelectedObjects().iterator().next());
    }

    /**
     * Allow Create Conversation feature if no conversation yet exist.
     * But if do, display it in detail section.
     * @param chatMessages
     */
    public void onResponseConversation(List<MessageDetail> chatMessages) {
        if (chatMessages.isEmpty()) {
            view.getToolbar().getCreateConversationBtn().setVisible(chatMessages.isEmpty());
        } else {
            initDetailSectionConversation((TableDisplayDetailModule) view.getSelectedObjects().iterator().next());
        }
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
        return new UniversalGridFactory.Builder<AdminDemandDetail>()
            .addColumnCheckbox(checkboxHeader)
            .addColumnDemandCreated(textFieldUpdater)
            .addColumnDemandTitle(textFieldUpdater)
            .addColumnLocality(textFieldUpdater)
            .addColumnUrgency()
            .addSelectionModel(new MultiSelectionModel(), AdminDemandDetail.KEY_PROVIDER)
            .addDefaultSort(Arrays.asList(new SortPair(FullDemandDetail.DemandField.CREATED)))
            .addRowStyles(rowStyles)
            .build();
    }
}