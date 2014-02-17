/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.client.user.widget.grid.UniversalGridFactory;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.supplierdemands.SupplierPotentialDemandDetail;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.shared.search.SortPair;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import java.util.Arrays;
import java.util.List;

/**
 * Part of SupplierDemands module widgets.
 * Displays supplier's potential demands.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = AbstractSupplierView.class)
public class SupplierDemandsPresenter extends AbstractSupplierPresenter {

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    /**
     * Binds table selection handler.
     */
    @Override
    public void bindView() {
        super.bindView();
        // Table Change Handlers
        addTableSelectionModelClickHandler();
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * Creates SupplierDemands widget.
     */
    public void onInitSupplierDemands(SearchModuleDataHolder filter) {
        super.initAbstractPresenter(filter, Constants.SUPPLIER_POTENTIAL_DEMANDS);

        eventBus.resetSearchBar(new Label("Supplier's projects attibure's selector will be here."));
    }

    /**
     * Displays supplier's potential demands data.
     * @param data to be displyed
     */
    public void onDisplaySupplierDemands(List<SupplierPotentialDemandDetail> data) {
        GWT.log("++ onResponseSupplierPotentialDemands");

        view.getTable().getDataProvider().updateRowData(view.getTable().getStart(), data);
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     *  Binds table selection handler. Allows sending offer button.
     */
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
     * Create supplier potential demands table using UniversalGridFactory.
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