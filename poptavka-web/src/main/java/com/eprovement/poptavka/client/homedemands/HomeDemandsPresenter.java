/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.detail.DemandDetailView;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * This presenter is to replace DemandsPresenter.java.
 *
 * @author praso
 */
@Presenter(view = HomeDemandsView.class)
public class HomeDemandsPresenter extends LazyPresenter<
        HomeDemandsPresenter.HomeDemandsViewInterface, HomeDemandsEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface HomeDemandsViewInterface extends LazyView, IsWidget {

        //CellTree
        CellTree getCellTree();

        SingleSelectionModel getSelectionCategoryModel();

        //Table
        ListBox getPageSizeCombo();

        int getPageSize();

        UniversalAsyncGrid<FullDemandDetail> getDataGrid();

        SimplePager getPager();

        //Detail
        DemandDetailView getDemandDetail();

        SimplePanel getDemandDetailPanel();

        void displayDemandDetail(FullDemandDetail fullDemandDetail);

        void hideDemandDetail();

        //Other
        Button getOfferBtn1();

        Button getOfferBtn2();

        Label getBannerLabel();

        Label getFilterLabel();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //<level, index>
    private Map<Integer, Integer> openedHierarchy = new TreeMap<Integer, Integer>();
    /**************************************************************************/
    /* RPC Service*/
    /**************************************************************************/
    @Inject
    private CategoryRPCServiceAsync categoryService;

    public CategoryRPCServiceAsync getCategoryService() {
        return categoryService;
    }

    /**************************************************************************/
    /* General Module events                                                  */
    /**************************************************************************/
    public void onStart() {
        if (Storage.isRootStartMethodCalledFirst() == null) {
            Storage.setRootStartMethodCalledFirst(false);
        }
    }

    public void onForward() {
        //This sets content of tab: current view attribute selector in popup.
        //However demands attribute selector is already loaded by default in first tab,
        //another setting in fourth tab is not needed
        eventBus.setUpSearchBar(null);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * Navigate processing according to given view type: depends if forwarded from menu, welcome, search.
     *
     * @param filter
     * @param homeDemandsViewType
     */
    public void onGoToHomeDemandsModule(SearchModuleDataHolder filter, int homeDemandsViewType) {
        switch(homeDemandsViewType) {
            case Constants.HOME_DEMANDS_BY_DEFAULT:
                goToHomeDemands();
                break;
            case Constants.HOME_DEMANDS_BY_WELCOME:
                goToHomeDemands(filter.getCategories().get(0));
                break;
            case Constants.HOME_DEMANDS_BY_SEARCH:
                goToHomeDemands(filter);
                break;
            default:
                break;
        }
    }

    /**
     * Forwarded from menu selection.
     */
    private void goToHomeDemands() {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS_BY_DEFAULT);
        //Set visibility
        view.getFilterLabel().setVisible(false);
        view.getCellTree().setVisible(true);
        //get data
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(null));
    }
    /**
     * Forwarded from welcome view - root category selection
     * @param rootCategoryDetail - category detail object selected from root categories menu on welcome view
     */
    private void goToHomeDemands(CategoryDetail rootCategoryDetail) {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS_BY_WELCOME);
        //Set visibility
        view.getFilterLabel().setVisible(false);
        view.getCellTree().setVisible(true);

        //select category in cellTree, which fires an event, which gets data
        view.getSelectionCategoryModel().setSelected(rootCategoryDetail, true);
    }

    /**
     * Forwarded from search module, when searching criteria were provided.
     * @param searchDataHolder - searching criteria
     */
    private void goToHomeDemands(SearchModuleDataHolder searchDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS_BY_SEARCH);
        //Set visibility
        //display message that search was performed
        //Ak bude text stale rovnaky, nemusi sa setovat tu,
        //ale ak bude dynamicky (zobrazia searching criteria), tak ano
        view.getFilterLabel().setText("Results satisfying searching criteria:");
        view.getFilterLabel().setVisible(true);
        view.getCellTree().setVisible(false);
        //getData
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Bind objects and their action handlers.
     */
    @Override
    public void bindView() {
        view.getCellTree().addOpenHandler(new OpenHandler<TreeNode>() {
            @Override
            public void onOpen(OpenEvent<TreeNode> event) {
                TreeNode openedNode = event.getTarget();
                CategoryDetail selectedCategory = (CategoryDetail) openedNode.getValue();
                //If node is requested to be opened, select also node object - it fires selected event
                view.getSelectionCategoryModel().setSelected(selectedCategory, true);

                manageOpenedHierarchy(selectedCategory, openedNode);
            }
        });
        // Add a selection model to handle celltree user selection.
        view.getSelectionCategoryModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.getDataGrid().clearData();

                CategoryDetail selected = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

                if (selected != null) {
                    SearchModuleDataHolder searchDataHolder = new SearchModuleDataHolder();
                    searchDataHolder.getCategories().add(selected);

                    view.hideDemandDetail();
                    view.getDataGrid().getSelectionModel().setSelected(
                            (FullDemandDetail) ((SingleSelectionModel) view.getDataGrid()
                                .getSelectionModel()).getSelectedObject(), false);

                    view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
                }
            }
        });

        // Add a selection model to handle table user selection.
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FullDemandDetail selected =
                        (FullDemandDetail) ((SingleSelectionModel) view.getDataGrid().getSelectionModel())
                        .getSelectedObject();
                if (selected != null) {
                    view.getOfferBtn1().setEnabled(true);
                    view.getOfferBtn2().setVisible(true);
                    view.displayDemandDetail(selected);
                } else {
                    view.getOfferBtn1().setEnabled(false);
                    view.getOfferBtn2().setVisible(false);
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

    /**************************************************************************/
    /* Business events handled by presenter                                   */
    /**************************************************************************/
    public void onDisplayDemands(List<FullDemandDetail> result) {
        view.getDataGrid().getDataProvider().updateRowData(view.getDataGrid().getStart(), result);
        view.getDataGrid().redraw();
    }

    /**************************************************************************/
    /* Private methods                                                        */
    /**************************************************************************/
    /**
     * Manages openedHierarchy attributes due to new user selection.
     */
    private void manageOpenedHierarchy(CategoryDetail selectedCategory, TreeNode openedNode) {
        List<Integer> levelsToRemove = new ArrayList<Integer>();
        boolean was = false;
        //remove all levels which are childrens of level that was selected by user.
        for (Integer level : openedHierarchy.keySet()) {
            if (selectedCategory.getLevel() < level) {
                //define whitch items(levels) remove
                levelsToRemove.add(level);
            } else if (selectedCategory.getLevel() == level) {
                was = true;
            }
        }
        //Remove selected levels
        if (!levelsToRemove.isEmpty()) {
            for (Integer levelToRemove : levelsToRemove) {
                openedHierarchy.remove(levelToRemove);
            }
        }
        if (was) {
            openNodesAccoirdingToHistory();
        }
        //replace last level index with actual one - selected by user
        openedHierarchy.put(selectedCategory.getLevel(), openedNode.getIndex());
    }

    /**
     * According to <b>openedHierarchy</b> attribute opens nodes except the last one from .
     */
    private void openNodesAccoirdingToHistory() {
        TreeNode root = view.getCellTree().getRootTreeNode();
        int depth = openedHierarchy.size();
        for (int i : openedHierarchy.keySet()) {
            //close only the last
            if (i == depth) {
                root = root.setChildOpen(openedHierarchy.get(i), false, false);
            } else {
                root = root.setChildOpen(openedHierarchy.get(i), true);
            }
        }
    }
}