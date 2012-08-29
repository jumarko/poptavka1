package com.eprovement.poptavka.client.homesuppliers;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.client.user.widget.grid.UniversalAsyncGrid;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
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

@Presenter(view = HomeSuppliersView.class)
public class HomeSuppliersPresenter
        extends LazyPresenter<HomeSuppliersPresenter.SuppliersViewInterface, HomeSuppliersEventBus> {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface SuppliersViewInterface extends LazyView, IsWidget {

        CellTree getCellTree();

        CellList getCategoriesList();

        int getPageSize();

        ListBox getPageSizeCombo();

        Button getContactBtn();

        UniversalAsyncGrid getDataGrid();

        SimplePager getPager();

        Widget getWidgetView();

        SingleSelectionModel getSelectionCategoryModel();

        SplitLayoutPanel getSplitter();

        void displaySuppliersDetail(FullSupplierDetail userDetail);

        void hideSuppliersDetail();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //others
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
        // nothing
    }

    public void onForward() {
        eventBus.setUpSearchBar(new HomeSuppliersViewView(), false, true, true);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * @param searchModuleDataHolder - if searching is needed, this object holds conditions to do so.
     *                               - it's also used as pointer to differ root and child sections
     */
    public void onGoToHomeSuppliersModule(SearchModuleDataHolder searchModuleDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.HOME_SUPPLIERS);
        this.searchDataHolder = searchModuleDataHolder;

        if (searchModuleDataHolder == null) {
            view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        } else {
            //Always only one category is sent
            view.getSelectionCategoryModel().setSelected(searchModuleDataHolder.getCategories().get(0), true);
        }
    }

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

                    view.hideSuppliersDetail();
                    view.getDataGrid().getSelectionModel().setSelected(
                            ((SingleSelectionModel) view.getDataGrid().getSelectionModel()).getSelectedObject(), false);

                    view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
                }
            }
        });
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                FullSupplierDetail selected =
                        (FullSupplierDetail) ((SingleSelectionModel) view.getDataGrid().getSelectionModel())
                        .getSelectedObject();

                if (selected != null) {
                    view.displaySuppliersDetail(selected);
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
    /* Additional events used to display data                                 */
    /**************************************************************************/
    /**
     * Display suppliers of selected category.
     * @param list
     */
    /* SUPPLIERS */
    public void onDisplaySuppliers(List<FullSupplierDetail> list) {
        view.getDataGrid().getDataProvider().updateRowData(view.getDataGrid().getStart(), list);
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