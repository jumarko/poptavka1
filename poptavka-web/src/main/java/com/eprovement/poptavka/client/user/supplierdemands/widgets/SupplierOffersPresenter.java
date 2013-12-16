/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.FullClientDetail;
import com.eprovement.poptavka.shared.domain.offer.SupplierOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.MultiSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

/**
 * Part of SupplierDemands module widgets.
 * Displays supplier's offers.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AbstractSupplierView.class)
public class SupplierOffersPresenter extends AbstractSupplierPresenter {

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        super.bindView();
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Creates SupplierOffers widget.
     * @param filter - search criteria
     */
    public void onInitSupplierOffers(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_OFFERS);
        eventBus.supplierMenuStyleChange(Constants.SUPPLIER_OFFERS);
        eventBus.createTokenForHistory();
        eventBus.initActionBox(view.getToolbar().getActionBox(), view.getTable());
        eventBus.initDetailSection(view.getTable(), view.getDetailPanel());

        eventBus.resetSearchBar(new Label("Supplier's contests attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**
     * Displays supplier's offers data.
     * @param data to be displayed
     */
    public void onDisplaySupplierOffers(List<IUniversalDetail> data) {
        GWT.log("++ onResponseSupplierOffers");

        view.getTable().getDataProvider().updateRowData(view.getTable().getStart(), data);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Create supplier offers table using UniversalGridFactory.
     */
    @Override
    protected UniversalAsyncGrid initTable() {
        return new UniversalGridFactory.Builder<SupplierOffersDetail>(view.getToolbar().getPager().getPageSize())
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnDemandTitle(textFieldUpdater)
            .addColumnPrice(textFieldUpdater)
            .addColumnClientRating(textFieldUpdater) //TODO rename to rating
            .addColumnOfferReceivedDate(textFieldUpdater)
            .addColumnFinishDate(textFieldUpdater)
            .addDefaultSort(Arrays.asList(new SortPair(FullClientDetail.ClientField.OVERALL_RATING)))
            .addSelectionModel(new MultiSelectionModel(), SupplierOffersDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }
}