/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.detail.FeedbackPopupView;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.client.user.widget.grid.columns.DisplayNameColumn.TableDisplayDisplayName;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;

/**
 * Displays client's demands with accpeted offer.
 * @author Martin Slavkovsky
 */
@Presenter(view = AbstractClientView.class)
public class ClientAssignedDemandsPresenter extends AbstractClientPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /** Class Attributes. **/
    private FeedbackPopupView ratePopup;
    private boolean assignedDemandsMode = false;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    /**
     * Binds handlers:
     * <ul>
     *   <li>table selection handler</li>
     *   <li>close button handler</li>
     * </ul>
     */
    @Override
    public void bindView() {
        super.bindView();
        // Table handlers
        addTableSelectionModelClickHandler();
        // buttons handlers
        addCloseButtonHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    /**
     * Creates client assigned demands widget.
     * @param filter - search criteria
     */
    public void onInitClientAssignedDemands(SearchModuleDataHolder filter) {
        initWidget(filter, Constants.CLIENT_ASSIGNED_DEMANDS);
        this.assignedDemandsMode = true;
    }

    /**
     * Creates client closed demands widget.
     * @param filter - search criteria
     */
    public void onInitClientClosedDemands(SearchModuleDataHolder filter) {
        initWidget(filter, Constants.CLIENT_CLOSED_DEMANDS);
    }

    /**
     * Displays thank you popup and forwards user to closed demands.
     */
    public void onResponseFeedback() {
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToClientDemandsModule(null, Constants.CLIENT_CLOSED_DEMANDS);
            }
        };
        eventBus.showThankYouPopup(Storage.MSGS.thankYouClosedDemand(), additionalAction);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Creates common parts of ClientAssigendDemands and ClientClosedDemands widgets.
     * @param filter - saerch criteria
     */
    private void initWidget(SearchModuleDataHolder filter, int widgetId) {
        super.initAbstractPresenter(filter, widgetId);

        eventBus.initActionBox(view.getToolbar().getActionBox(), view.getChildTable());
        eventBus.initDetailSection(view.getChildTable(), view.getDetailPanel());

        //Set visibility
        setParentTableVisible(false);
        setChildTableVisible(true);
        view.getToolbar().getCloseBtn().setVisible(assignedDemandsMode);

        eventBus.resetSearchBar(new Label("Client's assigned/closed projects attibure's selector will be here."));

        //request data
        view.getChildTable().getDataCount(eventBus, new SearchDefinition(filter));
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    /**
     * Binds table selection. Set close button visibility.
     */
    public void addTableSelectionModelClickHandler() {
        view.getChildTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //  display Close button in toolbar if needed
                if (view.getChildTableSelectedObjects().size() == 1) {
                    if (assignedDemandsMode) {
                        view.getToolbar().getCloseBtn().setVisible(true);
                    }
                } else {
                    view.getToolbar().getCloseBtn().setVisible(false);
                }
            }
        });
    }

    /**
     * Binds close button handler.
     * Closes demand and rates supplier.
     */
    private void addCloseButtonHandler() {
        view.getToolbar().getCloseBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                ratePopup = new FeedbackPopupView(FeedbackPopupView.CLIENT);
                ratePopup.setDisplayName(((TableDisplayDisplayName) selectedChildObject).getDisplayName());
                ratePopup.getRateBtn().addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        eventBus.requestCloseAndRateSupplier(
                            selectedChildObject.getDemandId(),
                            ((IUniversalDetail) selectedChildObject).getOfferId(),
                            Integer.valueOf(ratePopup.getRating()), ratePopup.getComment());
                    }
                });
            }
        });
    }

    /**
     * Sets child table visibility.
     * @param visible true to show, false to hide
     */
    @Override
    protected void setChildTableVisible(boolean visible) {
        super.setChildTableVisible(visible);
        view.getToolbar().getBackBtn().setVisible(false);
    }

    /**
     * Inits parent table.
     * Client demands user case - child table - conversation table
     * @return parent table
     */
    @Override
    UniversalAsyncGrid initParentTable() {
        //return empty table
        return new UniversalGridFactory.Builder<ClientOfferedDemandOffersDetail>().build();
    }

    /**
     * Inits child table.
     * @return child table
     */
    @Override
    UniversalAsyncGrid initChildTable() {
        return new UniversalGridFactory.Builder<ClientOfferedDemandOffersDetail>()
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnDemandTitle(textFieldUpdater)
            .addColumnPrice(textFieldUpdater)
            .addColumnSupplierRating(textFieldUpdater)
            .addColumnOfferReceivedDate(textFieldUpdater)
            .addColumnFinishDate(textFieldUpdater)
            .addDefaultSort(Arrays.asList(new SortPair(OfferDetail.OfferField.FINISH_DATE)))
            .addSelectionModel(new MultiSelectionModel(), ClientOfferedDemandOffersDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }
}