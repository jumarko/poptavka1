/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.user.clientdemands.widgets;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.user.clientdemands.ClientDemandsModuleEventBus;
import com.eprovement.poptavka.client.user.widget.detail.RatingDetailView;
import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.DemandRatingsDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.core.client.GWT;
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

@Presenter(view = ClientRatingsView.class, multiple = true)
public class ClientRatingsPresenter extends LazyPresenter<
        ClientRatingsPresenter.ClientRatingsLayoutInterface, ClientDemandsModuleEventBus> {

    public interface ClientRatingsLayoutInterface extends LazyView, IsWidget {

        UniversalAsyncGrid getDataGrid();

        SimplePager getPager();

        RatingDetailView getRatingDetail();

        SimplePanel getDetailPanel();

        IsWidget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private SearchModuleDataHolder searchDataHolder;

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
    public void onInitClientRatings(SearchModuleDataHolder filter) {
        //Must be present here. Loading data rely on this atrtibute
        Storage.setCurrentlyLoadedView(Constants.CLIENT_RATINGS);
        eventBus.createTokenForHistory();
        eventBus.setUpSearchBar(new Label("Client's ratings attibure's selector will be here."));
        searchDataHolder = filter;

        eventBus.displayView(view.getWidgetView());
        //init wrapper widget
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Business events handled by presenter */
    /**************************************************************************/
    /**
     * Response method for onInitSupplierList()
     * @param data
     */
    public void onDisplayClientRatings(List<IUniversalDetail> data) {
        GWT.log("++ onResponseClientsRatings");

        view.getDataGrid().getDataProvider().updateRowData(
                view.getDataGrid().getStart(), data);
    }

    /**************************************************************************/
    /* Bind View helper methods                                               */
    /**************************************************************************/
    public void addTableSelectionModelClickHandler() {
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                DemandRatingsDetail selected = (DemandRatingsDetail) ((SingleSelectionModel) view.getDataGrid()
                        .getSelectionModel()).getSelectedObject();
                view.getDetailPanel().setVisible(false);
                view.getRatingDetail().setVisible(true);
                view.getRatingDetail().setDemanRatingDetail(selected);
            }
        });
    }
}