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
        Button getOfferBtn();

        Label getBannerLabel();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private SearchModuleDataHolder searchDataHolder = null;
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
        // TODO praso - probably history initialization will be here
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
    public void onGoToHomeDemandsModule(SearchModuleDataHolder searchDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS);

        if (searchDataHolder == null) {
            view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        } else {
            //Always only one category is sent
            view.getSelectionCategoryModel().setSelected(searchDataHolder.getCategories().get(0), true);
        }
    }

    public void onGoToHomeDemandsBySearch(SearchModuleDataHolder searchDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS);

        //celltree visible false
        view.getCellTree().setVisible(false);
        //display message that search was performed

        //getData
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

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
                    if (searchDataHolder == null) {
                        searchDataHolder = new SearchModuleDataHolder();
                    }
                    searchDataHolder.getCategories().clear();
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
                    view.displayDemandDetail(selected);
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