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
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SplitLayoutPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.LinkedList;
import java.util.List;

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

        Label getFilterLabel();

        void displaySuppliersDetail(FullSupplierDetail userDetail);

        void hideSuppliersDetail();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    //<cat, index>
    private LinkedList<TreeItem> openedHierarchy = new LinkedList<TreeItem>();
    private TreeNode openedNode = null;
    private TreeNode lastNode = null;
    private long selectedSupplier = -1;
    //pointers
    boolean selectedFromNode = false;
    boolean cancelOpenEvent = false;
    boolean cancelSelectionEvent = false;
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
        //nothing
    }

    public void onForward() {
        //This sets content of tab: current view attribute selector (fourth tab) in popup.
        //However supplier attribute selector is loaded loaded by default in second tab,
        //another setting in fourth tab is not needed
        eventBus.setUpSearchBar(null);
    }

    /**************************************************************************/
    /* Navigation events                                                      */
    /**************************************************************************/
    /**
     * @param searchModuleDataHolder - if searching is needed, this object holds conditions to do so.
     *                               - it's also used as pointer to differ root and child sections
     */
    public void onGoToHomeSuppliersModule(SearchModuleDataHolder searchModuleDataHolder, int homeSuppliersViewType) {
        switch (homeSuppliersViewType) {
            case Constants.HOME_SUPPLIERS_BY_DEFAULT:
                goToHomeSuppliers();
                break;
            case Constants.HOME_SUPPLIERS_BY_SEARCH:
                goToHomeSuppliers(searchModuleDataHolder);
                break;
            default:
                break;
        }
    }

    public void onSetModuleByHistory(
            LinkedList<TreeItem> tree, CategoryDetail categoryDetail, int page, long supplierID) {
        //1 - Tree
        //open nodes accorting to history records
//        if (openedHierarchy.size() > tree.size()) {
//            onOpenNodesAccoirdingToHistory(tree);
//        }
        openedHierarchy = tree;
        openedNode = view.getCellTree().getRootTreeNode();
        //select category last category
        cancelSelectionEvent = true;
        view.getSelectionCategoryModel().setSelected(categoryDetail, true);
        //2 - Table
        //retrieve data with new filter and particular page
        view.getPager().setPage(page);
        view.hideSuppliersDetail();
        SearchModuleDataHolder searchDataHolder = new SearchModuleDataHolder();
        searchDataHolder.getCategories().add(categoryDetail);
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        //3 - Supplier Detail
        //select supplier when requested from history - if supplierID != -1 its fires event
        //to retrieve supplier's detail and select it in table selection model, which
        //forces loading detail to detail window
        selectedSupplier = supplierID;
    }

    /**
     * Forwarded from menu.
     */
    private void goToHomeSuppliers() {
        Storage.setCurrentlyLoadedView(Constants.HOME_SUPPLIERS_BY_DEFAULT);
        //Set visibility
        view.getFilterLabel().setVisible(false);
        view.getCellTree().setVisible(true);
        view.getSelectionCategoryModel().setSelected(view.getSelectionCategoryModel().getSelectedObject(), false);

        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(null));
    }

    /**
     * Forwarded from search module.
     *
     * @param searchDataHolder
     */
    private void goToHomeSuppliers(SearchModuleDataHolder searchDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.HOME_SUPPLIERS_BY_SEARCH);
        //Set visibility
        //display message that search was performed
        //Ak bude text stale rovnaky, nemusi sa setovat tu,
        //ale ak bude dynamicky (zobrazia searching criteria), tak ano
        view.getFilterLabel().setText("Results satisfying searching criteria:");
        view.getFilterLabel().setVisible(true);
        view.getCellTree().setVisible(false);
        view.getSelectionCategoryModel().setSelected(view.getSelectionCategoryModel().getSelectedObject(), false);

        //getData
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    @Override
    public void bindView() {
        view.getCellTree().addHandler(new LoadingStateChangeEvent.Handler() {
            @Override
            public void onLoadingStateChanged(LoadingStateChangeEvent event) {
                if (openedNode != null && !openedHierarchy.isEmpty()) {
                    if (openedHierarchy.size() > 1) {
                        cancelOpenEvent = true;
                    }
                    if (openedHierarchy.getFirst().getIndex() != -1) {
                        openedNode = openedNode.setChildOpen(openedHierarchy.removeFirst().getIndex(), true);
                    }
                }
            }
        }, LoadingStateChangeEvent.TYPE);
        view.getCellTree().addOpenHandler(new OpenHandler<TreeNode>() {
            @Override
            public void onOpen(OpenEvent<TreeNode> event) {
                //cancel event if needed
                if (cancelOpenEvent) {
                    cancelOpenEvent = false;
                    return;
                }
                TreeNode openedNode = event.getTarget();
//                lastNode = openedNode;
                CategoryDetail selectedCategory = (CategoryDetail) openedNode.getValue();

                if (manageOpenedHierarchy(selectedCategory, openedNode)) {
                    onOpenNodesAccoirdingToHistory(openedHierarchy);
                }

                selectedFromNode = true;
                view.getSelectionCategoryModel().setSelected(selectedCategory, true);
            }
        });
        view.getSelectionCategoryModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //cancel event if needed
                if (cancelSelectionEvent) {
                    cancelSelectionEvent = false;
                    return;
                }
                view.getDataGrid().clearData();

                CategoryDetail selected = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

                if (selected != null) {

                    SearchModuleDataHolder searchDataHolder = new SearchModuleDataHolder();
                    searchDataHolder.getCategories().add(selected);

                    view.hideSuppliersDetail();

                    view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));

                    if (!selectedFromNode) {
                        if (manageOpenedHierarchy(selected, null)) {
                            onOpenNodesAccoirdingToHistory(openedHierarchy);
                        }
                    }

                    if (!Storage.isCalledDueToHistory()) {
                        createTokenForHistory();
                    }

                    //reset pointers
                    selectedFromNode = false;
                    Storage.setCalledDueToHistory(false);
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
                    createTokenForHistory();
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
    /* Additional methods                                                     */
    /**************************************************************************/
    public void onSelectSupplier(FullSupplierDetail supplierDetail) {
        view.getDataGrid().getSelectionModel().setSelected(supplierDetail, true);
    }
    /**
     * Display suppliers of selected category.
     * @param list
     */
    /* SUPPLIERS */
    public void onDisplaySuppliers(List<FullSupplierDetail> list) {
        view.getDataGrid().getDataProvider().updateRowData(view.getDataGrid().getStart(), list);
        //If supplier must be selected, get its detail and select in selectionModel
        if (selectedSupplier != -1) {
            eventBus.getSupplier(selectedSupplier);
        }
        view.getDataGrid().redraw();
    }

    /**
     * According to <b>openedHierarchy</b> attribute opens nodes except the last one from .
     */
    public void onOpenNodesAccoirdingToHistory(LinkedList<TreeItem> categoryHierarchy) {
        openedHierarchy = categoryHierarchy;
        TreeNode root = view.getCellTree().getRootTreeNode();
        for (int i = 0; i < root.getChildCount(); i++) {
            root.setChildOpen(i, false);
        }
        root = view.getCellTree().getRootTreeNode();

//        boolean fireEvent = false;
        for (TreeItem item : categoryHierarchy) {
            cancelOpenEvent = true;
            root = root.setChildOpen(item.getIndex(), true);
        }
    }

    /**************************************************************************/
    /* Private methods                                                        */
    /**************************************************************************/
    /**
     * Manages openedHierarchy attributes due to new user selection.
     */
    /**
     *
     * @param selectedCategory
     * @param openedNode
     * @return define of recreating open nodes is needed
     */
    private boolean manageOpenedHierarchy(CategoryDetail selectedCategory, TreeNode openedNode) {
        //remove all levels which are childrens of level that was selected by user.
        int removeCount = 0;
        for (TreeItem item : openedHierarchy) {
            if (selectedCategory.getLevel() <= item.getLevel()) {
                removeCount++;
            }
        }
        //Remove selected levels
        for (int i = 0; i < removeCount; i++) {
            openedHierarchy.removeLast();
        }
        //replace last level index with actual one - selected by user
        openedHierarchy.add(new TreeItem(
                selectedCategory.getId(),
                selectedCategory.getLevel(),
                openedNode == null ? -1 : openedNode.getIndex()));

        return removeCount == 0 ? false : true;
    }

//    private void openNodesAccoirdingToHistory() {
//        TreeNode root = view.getCellTree().getRootTreeNode();
//        int depth = openedHierarchy.size();
//        for (TreeItem item : openedHierarchy) {
//            //close only the last
//            if (item.getLevel() == depth) {
//                root = root.setChildOpen(item.getIndex(), false, false);
//            } else {
//                root = root.setChildOpen(item.getIndex(), true);
//            }
//        }
//    }
    private void createTokenForHistory() {
        //Category
//        CategoryDetail selectedCategory = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

        //Supplier
        FullSupplierDetail selectedSupplier =
                (FullSupplierDetail) ((SingleSelectionModel) view.getDataGrid().getSelectionModel())
                .getSelectedObject();

        eventBus.createTokenForHistory(openedHierarchy, view.getPager().getPage(), selectedSupplier);
    }
}