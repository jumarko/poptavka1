/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.detail.DetailModuleBuilder;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.RatingDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
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

@Presenter(view = AbstractSupplierView.class)
public class SupplierRatingsPresenter extends AbstractSupplierPresenter {

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                initDetailSection((RatingDetail) ((SingleSelectionModel) view.getTable()
                            .getSelectionModel()).getSelectedObject());
            }
        });
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierRatings(SearchModuleDataHolder filter) {
        //Must be present here. Loading data rely on this atrtibute
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_RATINGS);
        eventBus.supplierMenuStyleChange(Constants.SUPPLIER_RATINGS);
        eventBus.createTokenForHistory();
        eventBus.resetSearchBar(new Label("Supplier's ratings attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.initDetailSection(view.getTable(), view.getDetailPanel());

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierRatings(List<String> data) {
        GWT.log("++ onResponseSupplierRatings");

        view.getTable().getDataProvider().updateRowData(view.getTable().getStart(), data);
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    private void initDetailSection(RatingDetail selectedDetail) {
        eventBus.buildDetailSectionTabs(new DetailModuleBuilder.Builder()
            .addRatingTab(selectedDetail.getDemandId())
            .selectTab(DetailModuleBuilder.RATING_TAB)
            .build());
    }

    @Override
    UniversalAsyncGrid initTable() {
        return new UniversalGridFactory.Builder<RatingDetail>()
            .addColumnDemandTitle(null)
            .addColumnPrice(null)
            .addSelectionModel(new SingleSelectionModel(), RatingDetail.KEY_PROVIDER)
            .addDefaultSort(Arrays.asList(new SortPair(FullDemandDetail.DemandField.CREATED)))
            .build();
    }
}