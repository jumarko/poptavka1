/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.RatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

/**
 * Displays all ratings of client's demands.
 * @author Martin Slavkovsky
 */
@Presenter(view = AbstractClientView.class)
public class ClientRatingsPresenter extends AbstractClientPresenter {

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    /**
     * bind parent table selection handler.
     */
    @Override
    public void bindView() {
        view.getParentTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                initDetailSection((RatingDetail) ((SingleSelectionModel) view.getParentTable()
                            .getSelectionModel()).getSelectedObject());
            }
        });
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    /**
     * Create ClientRatings widget.
     * @param filter - search criteria
     */
    public void onInitClientRatings(SearchModuleDataHolder filter) {
        //Must be present here. Loading data rely on this atrtibute
        Storage.setCurrentlyLoadedView(Constants.CLIENT_RATINGS);
        eventBus.clientDemandsMenuStyleChange(Constants.CLIENT_RATINGS);
        eventBus.createTokenForHistory();
        eventBus.resetSearchBar(new Label("Client's ratings attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.initDetailSection(view.getParentTable(), view.getDetailPanel());
        setParentTableVisible(true);

        eventBus.displayView(view.getWidgetView());
        eventBus.loadingDivHide();
        //init wrapper widget
        view.getParentTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientRatings(List<RatingDetail> data) {
        GWT.log("++ onResponseClientsRatings");

        view.getParentTable().getDataProvider().updateRowData(view.getParentTable().getStart(), data);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Inits detail section - rating detail.
     * @param selectedDetail
     */
    private void initDetailSection(RatingDetail selectedDetail) {
        eventBus.buildDetailSectionTabs(new DetailModuleBuilder.Builder()
            .addRatingTab(selectedDetail.getDemandId())
            .selectTab(DetailModuleBuilder.RATING_TAB)
            .build());
    }

    /**
     * Inits parent table.
     * @return table
     */
    @Override
    protected UniversalAsyncGrid initParentTable() {
        return new UniversalGridFactory.Builder<RatingDetail>()
            .addColumnDemandTitle(null)
            .addColumnPrice(null)
            .addSelectionModel(new SingleSelectionModel(), RatingDetail.KEY_PROVIDER)
            .addDefaultSort(Arrays.asList(new SortPair(FullDemandDetail.DemandField.CREATED)))
            .build();
    }

    /**
     * Inits child table.
     * Child table is no needed for this widget.
     * But in order to use abstractClientView, use workarount.
     *
     * @return empty table
     */
    @Override
    protected UniversalAsyncGrid initChildTable() {
        //return empty table
        return new UniversalGridFactory.Builder<ClientOfferedDemandOffersDetail>().build();
    }
}