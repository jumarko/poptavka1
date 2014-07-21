/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGridBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.detail.FeedbackPopupView;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.columns.DisplayNameColumn.TableDisplayDisplayName;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail.OfferField;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
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
 * Part of SupplierDemands widgets.
 * In assigned demands mode displays supplier's demands with accpeted offer.
 * In closed demands mode displays supplier's demands with finnished offer.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AbstractSupplierView.class)
public class SupplierAssignedDemandsPresenter extends AbstractSupplierPresenter {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private FeedbackPopupView ratePopup;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    /**
     * Binds handlers:
     * <ul>
     *   <li>finnish button handler,</li>
     *   <li>table selection handler</li>
     * </ul>
     */
    @Override
    public void bindView() {
        super.bindView();
        // Toolbar buttons handlers
        addFinnishButtonHandler();
        // Table Handlers
        addTableSelectionModelClickHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    /**
     * Creates SupplierAssignedDemands widget.
     * @param filter - search criteria
     */
    public void onInitSupplierAssignedDemands(SearchModuleDataHolder filter) {
        initWidget(filter, Constants.SUPPLIER_ASSIGNED_DEMANDS);
    }

    /**
     * Creates SupplierClosedDemands widget.
     * @param filter - search criteria
     */
    public void onInitSupplierClosedDemands(SearchModuleDataHolder filter) {
        initWidget(filter, Constants.SUPPLIER_CLOSED_DEMANDS);
    }

    /**
     * Displays thank you popup when offer has been finnished and clien has been rated.
     */
    public void onResponseFeedback() {
        view.getToolbar().getFinishBtn().setEnabled(false);
        Timer additionalAction = new Timer() {
            @Override
            public void run() {
                eventBus.goToSupplierDemandsModule(null, Constants.SUPPLIER_ASSIGNED_DEMANDS);
            }
        };
        eventBus.showThankYouPopup(Storage.MSGS.thankYouFinishDemand(), additionalAction);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Creates widget's commons.
     * @param filter - search criteria
     */
    private void initWidget(SearchModuleDataHolder filter, int widgetId) {
        super.initAbstractPresenter(filter, widgetId);

        eventBus.resetSearchBar(new Label("Supplier's assigned/closed projects attibure's selector will be here."));
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    /**
     * Binds table selection handler.
     * Displays toolbar finnih button if needed.
     */
    public void addTableSelectionModelClickHandler() {
        view.getTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //init details
                if (view.getSelectedObjects().size() == 1) {
                    view.getToolbar().getFinishBtn().setVisible(
                        Storage.getCurrentlyLoadedView() == Constants.SUPPLIER_ASSIGNED_DEMANDS);
                } else {
                    view.getToolbar().getFinishBtn().setVisible(false);
                }
            }
        });
    }

    /**
     * Binds finnish hanlder.
     * Finishs and rates client.
     */
    private void addFinnishButtonHandler() {
        view.getToolbar().getFinishBtn().addClickHandler(new ClickHandler() {
            @Override
            public void onClick(ClickEvent event) {
                view.getToolbar().getFinishBtn().setEnabled(false);
                ratePopup = new FeedbackPopupView(FeedbackPopupView.SUPPLIER);
                ratePopup.setDisplayName(((TableDisplayDisplayName) selectedObject).getDisplayName());
                ratePopup.getRateBtn().addClickHandler(new ClickHandler() {
                    @Override
                    public void onClick(ClickEvent event) {
                        eventBus.requestFinishAndRateClient(
                            selectedObject.getDemandId(),
                            ((IUniversalDetail) selectedObject).getOfferId(),
                            Integer.valueOf(ratePopup.getRating()), ratePopup.getComment());
                    }
                });
            }
        });
    }

    /**
     * Creates table using UniversalGridFactory.
     */
    @Override
    public UniversalAsyncGrid initTable() {
        return new UniversalAsyncGridBuilder<SupplierOffersDetail>(view.getToolbar().getPager().getPageSize())
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnDemandTitle(textFieldUpdater)
            .addColumnPrice(textFieldUpdater)
            //Martin 21.7.2014 - commented until some relevant rating data will be available
            //.addColumnClientRating(textFieldUpdater) //TODO rename to rating
            .addColumnOfferReceivedDate(textFieldUpdater)
            .addColumnFinishDate(textFieldUpdater)
            .addDefaultSort(Arrays.asList(SortPair.desc(OfferField.FINISH_DATE)))
            .addSelectionModel(new MultiSelectionModel(), SupplierOffersDetail.KEY_PROVIDER)
            .addRowStyles(null)
            .build();
    }
}
