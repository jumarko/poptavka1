/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGridBuilder;
import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.RatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail.DemandField;
import com.eprovement.poptavka.shared.domain.offer.ClientOfferedDemandOffersDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;

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
                if (view.getParentTable().getSelectionModel() != null
                    && ((SingleSelectionModel) view.getParentTable().getSelectionModel()).getSelectedObject() != null) {
                    initDetailSection((RatingDetail) ((SingleSelectionModel) view.getParentTable()
                                        .getSelectionModel()).getSelectedObject());
                }
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
        super.initAbstractPresenter(filter, Constants.CLIENT_RATINGS);

        eventBus.initDetailSection(view.getDetailPanel());

        //Set visibility
        setParentTableVisible(true);
        setChildTableVisible(false);

        eventBus.resetSearchBar(new Label("Client's ratings attibure's selector will be here."));

        //init wrapper widget
        view.getParentTable().getDataCount(eventBus, new SearchDefinition(filter));
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
        return new UniversalAsyncGridBuilder<RatingDetail>()
            .addColumnDemandTitle(null)
            .addColumnPrice(null)
            .addSelectionModel(new SingleSelectionModel(), RatingDetail.KEY_PROVIDER)
            .addDefaultSort(Arrays.asList(SortPair.desc(DemandField.CREATED)))
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
        return new UniversalAsyncGridBuilder<ClientOfferedDemandOffersDetail>().build();
    }
}