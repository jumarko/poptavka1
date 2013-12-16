package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

@Presenter(view = AbstractSupplierView.class)
public class SupplierDemandsPresenter extends AbstractSupplierPresenter {

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        super.bindView();
        // Table Change Handlers
        addTableSelectionModelClickHandler();
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    public void onInitSupplierDemands(SearchModuleDataHolder filter) {
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        eventBus.supplierMenuStyleChange(Constants.SUPPLIER_POTENTIAL_DEMANDS);
        eventBus.createTokenForHistory();
        eventBus.initActionBox(view.getToolbar().getActionBox(), view.getTable());
        eventBus.initDetailSection(view.getTable(), view.getDetailPanel());

        eventBus.resetSearchBar(new Label("Supplier's projects attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getTable().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierDemands(List<SupplierPotentialDemandDetail> data) {
        GWT.log("++ onResponseSupplierPotentialDemands");

        view.getTable().getDataProvider().updateRowData(view.getTable().getStart(), data);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    public void addTableSelectionModelClickHandler() {
        view.getTable().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                if (view.getSelectedUserMessageIds().size() == 1) {
                    eventBus.allowSendingOffer();
                }
            }
        });
    }

    /**
     * Create supplier potential demands table.
     */
    @Override
    protected UniversalAsyncGrid initTable() {
        return new UniversalGridFactory.Builder<SupplierPotentialDemandDetail>()
            .addColumnCheckbox(checkboxHeader)
            .addColumnStar(starFieldUpdater)
            .addColumnDemandTitle(textFieldUpdater)
            .addColumnPrice(textFieldUpdater)
            .addColumnUrgency()
            .addColumnClientRating(textFieldUpdater)
            .addDefaultSort(Arrays.asList(new SortPair(FullDemandDetail.DemandField.VALID_TO)))
            .addSelectionModel(new MultiSelectionModel(), SupplierPotentialDemandDetail.KEY_PROVIDER)
            .addRowStyles(rowStyles)
            .build();
    }
}