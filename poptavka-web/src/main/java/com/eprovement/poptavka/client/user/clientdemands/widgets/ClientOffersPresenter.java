/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGridBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail.OfferField;
import com.eprovement.poptavka.shared.domain.clientdemands.ClientDemandDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

/**
 * Displays client's projects with placed offer on it.
 * @author Martin Slavkovsky
 */
@Presenter(view = AbstractClientView.class)
public class ClientOffersPresenter extends AbstractClientPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private ClientOfferedDemandOffersDetail selectedOfferedDemandOfferObject;
//    private boolean isInitializing;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    /**
     * Bind handlers:
     * <ul>
     *   <li>back button handler</li>
     *   <li>accept button handler</li>
     *   <li>demand table selection handler</li>
     *   <li>offer table selection handler</li>
     * </ul>
     */
    @Override
    public void bindView() {
        super.bindView();
        // Toolbar handlers
        addBackButtonHandler();
        addAcceptOfferButtonHandler();
        // Selection Handlers
        addOfferGridSelectionModelHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    /**
     * Creates ClinetOffers widget.
     * @param filter - search criteria
     */
    public void onInitClientOffers(SearchModuleDataHolder filter) {
        super.initAbstractPresenter(filter, Constants.CLIENT_OFFERED_DEMANDS);
        isInitializing = true;

        eventBus.initActionBox(this.view.getToolbar().getActionBox(), view.getParentTable());
        eventBus.initDetailSection(view.getDetailPanel());

        //Set visibility
        setChildTableVisible(false);
        setParentTableVisible(true);

        eventBus.resetSearchBar(new Label("Client's contests attibure's selector will be here."));

        //request data to demand(parent) table
        if (view.getParentTable().isVisible()) {
            view.getParentTable().getDataCount(eventBus, new SearchDefinition(filter));
        } else {
            backBtnClickHandlerInner();
        }
    }

    /**
     * Displays child table data.
     * @param data to be displayed
     */
    public void onDisplayClientOfferedDemandOffers(List<ClientOfferedDemandOffersDetail> data) {
        GWT.log("++ onResponseClientsOfferedDemandOffers");

        if (!data.isEmpty()) {
            setParentTableVisible(false);
            setChildTableVisible(true);

            view.getChildTable().getDataProvider().updateRowData(
                view.getChildTable().getStart(), data);
        }
    }

    /**
     * Shows thank you popup when offer is accepted and forwards user to ClientAssignedDemands.
     */
    public void onResponseAcceptOffer() {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_ASSIGNED_DEMANDS);
            }
        };
        eventBus.showThankYouPopup(Storage.MSGS.thankYouAcceptOffer(), additionalAction);
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    /** SelectionHandlers. **/
    //--------------------------------------------------------------------------

    /**
     * Show or Hide details section and action box.
     * Show if and only of one table row is selected.
     * Hide otherwise - not or more than one rows are selected.
     */
    private void addOfferGridSelectionModelHandler() {
        view.getChildTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //tool bar items
                view.getToolbar().getAcceptBtn().setVisible(view.getChildTableSelectedObjects().size() == 1);
                if (view.getChildTableSelectedObjects().size() == 1) {
                    selectedOfferedDemandOfferObject =
                        (ClientOfferedDemandOffersDetail) view.getChildTableSelectedObjects().iterator().next();
                } else {
                    selectedOfferedDemandOfferObject = null;
                }
            }
        });
    }

    /**
     * Binds back button handler.
     */
    private void addBackButtonHandler() {
        view.getToolbar().getBackBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (Storage.getCurrentlyLoadedView() == Constants.CLIENT_OFFERED_DEMAND_OFFERS) {
                    backBtnClickHandlerInner();
                }
            }
        });
    }

    /**
     * Displays parent table and hides child table on back button action.
     */
    private void backBtnClickHandlerInner() {
        eventBus.displayAdvertisement();
        selectedOfferedDemandOfferObject = null;
        isInitializing = true;
        view.getToolbar().getPager().getPager().startLoading();
        setChildTableVisible(false);
        setParentTableVisible(true);

        Storage.setCurrentlyLoadedView(Constants.CLIENT_OFFERED_DEMANDS);
        view.getParentTable().getDataCount(eventBus, new SearchDefinition(
            view.getParentTable().getStart(), view.getToolbar().getPager().getPageSize(), searchDataHolder,
            view.getParentTable().getSort().getSortOrder()));
    }

    /**
     * Binds accept offer button handler.
     */
    private void addAcceptOfferButtonHandler() {
        view.getToolbar().getAcceptBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                if (selectedOfferedDemandOfferObject != null) {
                    eventBus.requestAcceptOffer(selectedOfferedDemandOfferObject.getOfferId());
                } else {
                    Window.alert("To accept an offer you need to select one (just one).");
                }
            }
        });
    }

    /**
     * Inits parent table.
     * Client demands user case - parent table - demands table
     */
    @Override
    public UniversalAsyncGrid initParentTable() {
        return new UniversalAsyncGridBuilder<ClientDemandDetail>()
            .addColumnDemandTitle(null)
            .addColumnPrice(null)
            .addColumnEndDate(null)
            .addColumnUrgency()
            .addDefaultSort(Arrays.asList(SortPair.asc(DemandField.VALID_TO)))
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
        return new UniversalAsyncGridBuilder<ClientOfferedDemandOffersDetail>()
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnDisplayName(textFieldUpdater)
            .addColumnPrice(textFieldUpdater)
            .addColumnOfferReceivedDate(textFieldUpdater)
            .addColumnFinishDate(textFieldUpdater)
            .addDefaultSort(Arrays.asList(SortPair.asc(OfferField.CREATED)))
            .addSelectionModel(new MultiSelectionModel(), ClientOfferedDemandOffersDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }
}
