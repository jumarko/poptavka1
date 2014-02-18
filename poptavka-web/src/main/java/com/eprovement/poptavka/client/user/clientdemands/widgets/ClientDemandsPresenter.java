/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.detail.DetailModuleView;
import com.eprovement.poptavka.client.user.widget.detail.EditableDemandDetailPresenter;
import com.eprovement.poptavka.client.user.widget.detail.EditableDemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandConversationDetail;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

/**
 * Displays client demands that are new.
 * @author Martin Slavkovsky
 */
@Presenter(view = AbstractClientView.class)
public class ClientDemandsPresenter extends AbstractClientPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private EditableDemandDetailPresenter editDemandPresenter;
    private boolean isInitializing;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    /**
     * Binds handlers:
     * <ul>
     *   <li>back button handler</li>
     *   <li>toolbar buttons handler</li>
     *   <li>parent table selection handler</li>
     *   <li>child table selection handler</li>
     * </ul>
     */
    @Override
    public void bindView() {
        super.bindView();
        // Toolbar handlers
        addBackBtnClickHandler();
        addToolbarButtonsClickHandlers();
        // Selection Handlers
        addParentTableSelectionHandler();
        addChildTableSelectionModelHandler();
        // Detail section
        addDetailSelectionHandler();
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * Creates ClientDemands widget.
     * @param filter - search criteria
     */
    public void onInitClientDemands(SearchModuleDataHolder filter) {
        super.initAbstractPresenter(filter, Constants.CLIENT_DEMANDS);
        isInitializing = true;

        eventBus.initActionBox(view.getToolbar().getActionBox(), view.getChildTable());
        eventBus.initDetailSection(view.getChildTable(), view.getDetailPanel());

        //Set visibility
        setChildTableVisible(false);
        setParentTableVisible(true);

        eventBus.resetSearchBar(new Label("Client's projects attibure's selector will be here."));

        //request data to demand(parent) table
        if (view.getParentTable().isVisible()) {
            view.getParentTable().getDataCount(eventBus, new SearchDefinition(filter));
        } else {
            backBtnClickHandlerInner();
        }
    }

    /**************************************************************************/
    /* Display data                                                           */
    /**************************************************************************/
    /**
     * Display client's "My demands" and select item if requested.
     * @param data
     */
    public void onDisplayClientDemands(List<ClientDemandDetail> data) {
        GWT.log("++ onResponseClientsDemands");

//        view.getToolbar().bindPager(view.getParentTable());
        view.getParentTable().getDataProvider().updateRowData(
            view.getParentTable().getStart(), data);
    }

    /**
     * Display conversations for selected demand and select item if requested.
     * If no conversations available:
     * 1) don't display empty conversation table, leave visible demand table
     * 2) display details wrapper for user to be able to edit demand.
     *
     * @param data Conversations for selected demand.
     */
    public void onDisplayClientDemandConversations(List<ClientDemandConversationDetail> data) {
        GWT.log("++ onResponseClientsDemandConversation");
        //if no data availbale, leave all as it is and dispaly details wrapper.
        if (!data.isEmpty()) {
            setParentTableVisible(false);
            setChildTableVisible(true);
//            view.getToolbar().getPager().getPager().startLoading();

            view.getChildTable().getDataProvider().updateRowData(
                view.getChildTable().getStart(), data);
        }
    }

    /**
     * In conversation (child) table is empty for selected demand from parent table, there is
     * no need to display it. And also when it cases problem because details wrapper is
     * bind to selection on Conversation table items, therefore, if no data, no details wrapper
     * can be call automatically. Therefore we need to do it manually.
     */
    public void onResponseConversationNoData() {
        SingleSelectionModel selectionModel = (SingleSelectionModel) view.getParentTable().getSelectionModel();
        //init details
        if (selectionModel.getSelectedSet().isEmpty()) {
            eventBus.displayAdvertisement();
        } else {
            super.initDetailSectionDemand(selectedParentObject);
        }
    }

    /**************************************************************************/
    /* Response methods                                                       */
    /**************************************************************************/
    /**
     * Display notification when demand was deleted.
     * @param result
     */
    public void onResponseDeleteDemand(boolean result) {
        //TODO LATER Martin - make proper notify popup and change layout
        backBtnClickHandlerInner();
        Window.alert("deleted succesfully");
    }

    /**
     * On update refresh table to load new data to table in case they changed.
     * @param result
     */
    public void onResponseUpdateDemand(FullDemandDetail result) {
        view.getToolbar().setEditDemandBtnsVisibility(true);
        //reset & refresh view
        eventBus.setCustomWidget(DetailModuleBuilder.DEMAND_DETAIL_TAB, null);
        //refresh grid
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS);
//        view.getToolbar().getPager().getPager().startLoading();
        view.getParentTable().getDataCount(eventBus, new SearchDefinition(
            view.getParentTable().getStart(),
            view.getToolbar().getPager().getPageSize(),
            searchDataHolder,
            view.getParentTable().getSort().getSortOrder()));
        //notify user
        eventBus.showThankYouPopup(SafeHtmlUtils.fromString("Successfully updated"), null);
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    /** SelectionHandlers. **/
    //--------------------------------------------------------------------------
    /**
     * Show child table and fire event for getting its data.
     * Hides detail section if no row is selected.
     */
    private void addParentTableSelectionHandler() {
        view.getParentTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (!isInitializing) {
                    Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMAND_DISCUSSIONS);
                    view.getToolbar().setEditDemandBtnsVisibility(true);
                }
                isInitializing = false;
            }
        });
    }

    /**
     * Show or Hide details section and action box.
     * Show if and only of one table row is selected.
     * Hide otherwise - not or more than one rows are selected.
     */
    private void addChildTableSelectionModelHandler() {
        view.getChildTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (view.getChildTableSelectedObjects().size() != 1) {
                    view.getToolbar().setEditDemandBtnsVisibility(false);
                }
            }
        });
    }

    /**
     * Displays toolbar edit demands visibility.
     */
    private void addDetailSelectionHandler() {
        eventBus.setCustomSelectionHandler(new SelectionHandler<Integer>() {
            @Override
            public void onSelection(SelectionEvent<Integer> event) {
                view.getToolbar().setEditDemandBtnsVisibility(
                    event.getSelectedItem() == DetailModuleBuilder.DEMAND_DETAIL_TAB);
            }
        });
    }

    /** Field updater. **/
    //--------------------------------------------------------------------------
    //Button handlers
    /**
     * Binds back buttons handler.
     */
    private void addBackBtnClickHandler() {
        view.getToolbar().getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                backBtnClickHandlerInner();
            }
        });
    }

    /**
     * Displays parent table and hides child table on back button action.
     */
    private void backBtnClickHandlerInner() {
        Storage.setCurrentlyLoadedView(Constants.CLIENT_DEMANDS);
        eventBus.displayAdvertisement();
        selectedParentObject = null;
        selectedChildObject = null;
        view.getToolbar().setEditDemandBtnsVisibility(false);
//        view.getToolbar().getPager().getPager().startLoading();
        setChildTableVisible(false);
        setParentTableVisible(true);
        view.getParentTable().getDataCount(eventBus, new SearchDefinition(
            view.getParentTable().getStart(), view.getToolbar().getPager().getPageSize(), searchDataHolder,
            view.getParentTable().getSort().getSortOrder()));
    }

    /**
     * Binds toolbar buttons handlers.
     */
    private void addToolbarButtonsClickHandlers() {
        view.getToolbar().getEditBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                eventBus.setCustomWidget(DetailModuleBuilder.DEMAND_DETAIL_TAB, getEditableDemandPresetner());
            }
        });
        view.getToolbar().getDeleteBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getToolbar().setEditDemandBtnsVisibility(false);
                eventBus.requestDeleteDemand(selectedParentObject.getDemandId());
            }
        });
    }

    /** Others. **/
    /**
     * @return the EditableDemandDetailView
     */
    private EditableDemandDetailView getEditableDemandPresetner() {
        if (editDemandPresenter == null) {
            editDemandPresenter = eventBus.addHandler(EditableDemandDetailPresenter.class);
        }
        EditableDemandDetailView editDemandView = (EditableDemandDetailView) editDemandPresenter.getView();
        editDemandView.resetFields();
        editDemandView.setDemanDetail(
            ((DetailModuleView) view.getDetailPanel().getWidget()).getDemandDetail().getDemandDetail());
        return editDemandView;
    }

    /**
     * Inits parent table.
     * Client demands user case - parent table - demands table
     */
    @Override
    public UniversalAsyncGrid initParentTable() {
        return new UniversalGridFactory.Builder<ClientDemandDetail>()
            .addColumnDemandStatus()
            .addColumnDemandTitle(null)
            .addColumnPrice(null)
            .addColumnEndDate(null)
            .addColumnUrgency()
            .addDefaultSort(Arrays.asList(new SortPair(FullDemandDetail.DemandField.CREATED)))
            .addSelectionModel(new SingleSelectionModel(), ClientDemandDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }

    /**
     * Inits child table.
     * Client demands user case - child table - conversation table
     */
    @Override
    public UniversalAsyncGrid initChildTable() {
        return new UniversalGridFactory.Builder<ClientDemandConversationDetail>()
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnDisplayName(textFieldUpdater)
            .addColumnMessageText(textFieldUpdater)
            .addColumnClientRating(textFieldUpdater) //TODO rename to rating
            .addColumnMessageSent(textFieldUpdater)
            .addDefaultSort(Arrays.asList(new SortPair(FullDemandDetail.DemandField.CREATED)))
            .addSelectionModel(new MultiSelectionModel(), ClientDemandConversationDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }
}