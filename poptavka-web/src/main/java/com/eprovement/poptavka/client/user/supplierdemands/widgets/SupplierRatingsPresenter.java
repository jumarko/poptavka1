/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.supplierdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.supplierdemands.SupplierDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.DetailsWrapperPresenter;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.List;

@Presenter(view = SupplierRatingsView.class, multiple = true)
public class SupplierRatingsPresenter extends LazyPresenter<
        SupplierRatingsPresenter.SupplierRatingsLayoutInterface, SupplierDemandsModuleEventBus> {

    public interface SupplierRatingsLayoutInterface extends LazyView, IsWidget {

        UniversalAsyncGrid getDataGrid();

        SimplePager getPager();

        SimplePanel getWrapperPanel();

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private DetailsWrapperPresenter detailSection;
    private SearchModuleDataHolder searchDataHolder;
    private DemandRatingsDetail selectedObject;

    /**************************************************************************/
    /* Bind actions                                                           */
    /**************************************************************************/
    @Override
    public void bindView() {
        addTableSelectionModelClickHandler();
    }

    /**************************************************************************/
    /* Navigation events */
    /**************************************************************************/
    public void onInitSupplierRatings(SearchModuleDataHolder filter) {
        //Must be present here. Loading data rely on this atrtibute
        Storage.setCurrentlyLoadedView(Constants.SUPPLIER_RATINGS);

        initWidget(filter);
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    public void onResponseDetailWrapperPresenter(DetailsWrapperPresenter detailSection) {
        if (this.detailSection == null) {
            this.detailSection = detailSection;
            this.detailSection.initDetailWrapper(null, view.getWrapperPanel());
            this.detailSection.getView().getReplyHolder().setVisible(false);
            if (selectedObject != null) {
                this.detailSection.initDetails(
                        selectedObject.getDemandId(),
                        selectedObject.getSupplierId(),
                        selectedObject.getThreadRootId());
            }
        }
    }

    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplaySupplierRatings(List<IUniversalDetail> data) {
        GWT.log("++ onResponseSupplierRatings");

        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), data);
    }

    /**************************************************************************/
    /* Business events handled by eventbus or RPC                             */
    /**************************************************************************/
    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    private void initWidget(SearchModuleDataHolder filter) {
        eventBus.setUpSearchBar(new Label("Supplier's ratings attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    public void addTableSelectionModelClickHandler() {
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                detailSection.getView().getWidgetView().getElement().getStyle().setDisplay(Style.Display.BLOCK);
                DemandRatingsDetail selected = (DemandRatingsDetail) ((SingleSelectionModel) view.getDataGrid()
                        .getSelectionModel()).getSelectedObject();
                detailSection.initDetails(
                        selected.getDemandId(),
                        selected.getSupplierId(),
                        selected.getThreadRootId());
            }
        });
    }
}