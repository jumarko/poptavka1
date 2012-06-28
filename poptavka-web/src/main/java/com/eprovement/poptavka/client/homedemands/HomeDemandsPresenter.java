/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.main.Constants;
import com.eprovement.poptavka.client.main.Storage;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.domain.enums.OrderType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.BasePresenter;
import java.util.List;
import java.util.Map;

/**
 * This presenter is to replace DemandsPresenter.java.
 *
 * @author praso
 */
@Presenter(view = HomeDemandsView.class)
public class HomeDemandsPresenter extends BasePresenter<
        HomeDemandsPresenter.HomeDemandsViewInterface, HomeDemandsEventBus> {

    public interface HomeDemandsViewInterface {

        DemandDetailView getDemandDetail();

        SimplePanel getDemandDetailPanel();

        Widget getWidgetView();

        Button getOfferBtn();

        ListBox getPageSizeCombo();

        int getPageSize();

        UniversalAsyncGrid<FullDemandDetail> getDataGrid();

        SimplePager getPager();

        Label getBannerLabel();
    }

    /**
     * Bind objects and their action handlers.
     */
    @Override
    public void bind() {
        // Add a selection model to handle user selection.
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {

            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FullDemandDetail selected = view.getDataGrid().getSelectionModel().getSelectedObject();
                if (selected != null) {
                    view.getBannerLabel().setVisible(false);
                    view.getDemandDetailPanel().setVisible(true);
                    view.getOfferBtn().setVisible(true);
                    eventBus.setDemand(selected);
                } else {
                    view.getDemandDetailPanel().setVisible(false);
                    view.getOfferBtn().setVisible(false);
                    view.getBannerLabel().setVisible(true);
                }
            }
        });

        view.getPageSizeCombo().addChangeHandler(new ChangeHandler() {

            @Override
            public void onChange(ChangeEvent arg0) {
                view.getDataGrid().setRowCount(0, true);

                int newPage = Integer.valueOf(view.getPageSizeCombo().
                        getItemText(view.getPageSizeCombo().getSelectedIndex()));

                view.getDataGrid().setRowCount(newPage, true);

                int page = view.getPager().getPageStart() / view.getPager().getPageSize();

                view.getPager().setPageStart(page * newPage);
                view.getPager().setPageSize(newPage);
            }
        });
    }

    /**
     * ***********************************************************************
     * General Module events
     * ***********************************************************************
     */
    public void onStart() {
        // TODO praso - probably history initialization will be here
    }

    public void onForward() {
        eventBus.setUpSearchBar(new HomeDemandViewView(), true, true, true);
    }
    /**
     * ***********************************************************************
     * Navigation events
     * ***********************************************************************
     */
    //need to remember for asynchDataProvider if asking for more data
    private SearchModuleDataHolder searchDataHolder = null;

    public void onGoToHomeDemandsModule(SearchModuleDataHolder searchDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS);
        view.getDataGrid().getDataCount(eventBus, searchDataHolder);

        this.searchDataHolder = searchDataHolder;
    }

    /**
     * ***********************************************************************
     * Business events handled by presenter
     * ***********************************************************************
     */
    public void onDisplayDemands(List<FullDemandDetail> result) {
        view.getDataGrid().getDataProvider().updateRowData(view.getDataGrid().getStart(), result);
        view.getDataGrid().flush();
        view.getDataGrid().redraw();
        eventBus.loadingHide();
    }

    public void onSetDemand(FullDemandDetail demand) {
        view.getDemandDetailPanel().setVisible(true);
        view.getOfferBtn().setVisible(true);
        view.getDemandDetail().setDemanDetail(demand);
    }

    /**************************************************************************/
    /* Getter methods                                                         */
    /**************************************************************************/
    public void onGetDataCount(UniversalAsyncGrid grid, SearchModuleDataHolder detail) {
        eventBus.getDemandsCount(grid, detail);
    }

    public void onGetData(int start, int count, SearchModuleDataHolder detail, Map<String, OrderType> orderColumns) {
        eventBus.getDemands(start, count, detail, orderColumns);
    }
}