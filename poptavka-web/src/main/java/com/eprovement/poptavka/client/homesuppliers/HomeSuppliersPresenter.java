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
import com.google.gwt.view.client.RangeChangeEvent;
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
    private LinkedList<TreeItem> actualOpenedHierarchy = new LinkedList<TreeItem>();
    private LinkedList<TreeItem> temporaryOpenedHierarchy = new LinkedList<TreeItem>();
    private int page = -1;
    private long selectedSupplier = -1;
    private TreeNode openedNode = null;
    //pointers
    boolean selectedFromNode = false;
    boolean cancelOpenEvent = false;
    boolean cancelSelectionEvent = false;
    /**************************************************************************/
    /* RPC Service                                                            */
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
        //Martin - toto moze zbytocne spomalit - docane kym nenajdem lepsie riesenie ako zatvarat uzly od konca
        if (actualOpenedHierarchy.size() > tree.size()) {
            openNodesAccoirdingToOpenedHierarchy();
        }

        this.actualOpenedHierarchy = tree;
        this.temporaryOpenedHierarchy = tree;
        this.openedNode = view.getCellTree().getRootTreeNode();

        //2 - Table
        //retrieve data with new filter at particular page
        this.page = page;
        //select category
        view.getSelectionCategoryModel().setSelected(categoryDetail, true);

        //3 - Supplier Detail
        //select supplier when requested from history - if supplierID != -1 its fires event
        //to retrieve supplier's detail and select it in table selection model, which
        //forces loading detail to detail window
        this.selectedSupplier = supplierID;
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
    /**
     * Eventy:
     * - LoadingState - nema mat nic so ziskavanim dat
     *                - len kvoli tomu, ze categorie su do stromu dotahovane asynchronne
     *                  a pri otvarani uzlov vznika indexOutOfBound pretoze chcem otvorit
     *                  nieco co tam este nie je
     *                - mal by starat o programove otvaranie uzlov
     * - Open - nema mat nic so ziskavanim dat
     *        - len kvoli tomu, ze ak uzivatel zvoli otvorenie uzlu, aby sa hned aj vyvolal
     *          selection event na category selection modeli pre ziskanie dat pre dany uzol
     * - Selection - nema mat nic s otvaranim uzlov (i ked pri zvoleni categorie,
     *               by sa mohol otvorit dany uzol - na zvazenie, takze zrobit)
     *             - mal by postarat o zavolanie metody getDataCount pre ziskanie dat pre dany categoriu

     */
    @Override
    public void bindView() {
        view.getDataGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                createTokenForHistory(selectedSupplier == -1 ? false : true);
            }
        });
        view.getCellTree().addHandler(new LoadingStateChangeEvent.Handler() {
            @Override
            public void onLoadingStateChanged(LoadingStateChangeEvent event) {
                if (!temporaryOpenedHierarchy.isEmpty()) {
                    if (temporaryOpenedHierarchy.getFirst().getIndex() != -1) {
                        cancelOpenEvent = true;
                        openedNode = openedNode.setChildOpen(temporaryOpenedHierarchy.removeFirst().getIndex(), true);
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
                CategoryDetail selectedCategory = (CategoryDetail) event.getTarget().getValue();

                //len ak by uzivatel zvolil uplne inu vetvu - zavri vsetky a otvor len novo zvolenu
                if (manageOpenedHierarchy(selectedCategory, event.getTarget())) {
                    openNodesAccoirdingToOpenedHierarchy();
                }

                view.getSelectionCategoryModel().setSelected(selectedCategory, true);
            }
        });
        view.getSelectionCategoryModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                CategoryDetail selected = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

                if (selected != null) {
                    //Initialization
                    view.getDataGrid().clearData();
                    //if page and pager.page is equal, setting page won't fire rangeChange event -> manual
                    if (page == -1 || page == view.getPager().getPage()) {
                        createTokenForHistory(selectedSupplier == -1 ? false : true);
                    } else {
                        view.getPager().setPage(page);
                    }
                    SearchModuleDataHolder searchDataHolder = new SearchModuleDataHolder();
                    searchDataHolder.getCategories().add(selected);
                    view.hideSuppliersDetail();

                    //Retrieve data
                    view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
                } else {
                    closeAllNodes(view.getCellTree().getRootTreeNode());
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
                    createTokenForHistory(false);
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
    /**
     * temporaryOpenedHierarchy - needs for asynchronous opening of nodes
     * actualOpenedHierarchy - needs for archiving actual state of opened nodes
     */
    public void openNodesAccoirdingToOpenedHierarchy() {
        closeAllNodes(view.getCellTree().getRootTreeNode());
        cancelOpenEvent = true;
        temporaryOpenedHierarchy = actualOpenedHierarchy;

        openedNode = view.getCellTree().getRootTreeNode();
        openedNode = openedNode.setChildOpen(temporaryOpenedHierarchy.removeFirst().getIndex(), true);
    }

    /**************************************************************************/
    /* Private methods                                                        */
    /**************************************************************************/
    private void closeAllNodes(TreeNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            node.setChildOpen(i, false);
        }
    }

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
        for (TreeItem item : actualOpenedHierarchy) {
            if (selectedCategory.getLevel() <= item.getLevel()) {
                removeCount++;
            }
        }
        //Remove selected levels
        for (int i = 0; i < removeCount; i++) {
            actualOpenedHierarchy.removeLast();
        }
        //replace last level index with actual one - selected by user
        actualOpenedHierarchy.add(new TreeItem(
                selectedCategory.getId(),
                selectedCategory.getLevel(),
                openedNode == null ? -1 : openedNode.getIndex()));

        return removeCount == 0 ? false : true;
    }

    private void createTokenForHistory(boolean setStorageHistoryPointerValue) {
        //Create token only if new input from user was made and new token must be created
        if (!Storage.isCalledDueToHistory()) {
            //Supplier
            FullSupplierDetail supplier =
                    (FullSupplierDetail) ((SingleSelectionModel) view.getDataGrid().getSelectionModel())
                    .getSelectedObject();

            eventBus.createTokenForHistory(actualOpenedHierarchy, view.getPager().getPage(), supplier);
        }
        //reset poitner - only if it is to no use anymore ->
        //if selected supplier's id was present in URL, pointer will be used to decide
        //whether to select supplier, or not, after loading data to table.
        Storage.setCalledDueToHistory(setStorageHistoryPointerValue);
    }
}