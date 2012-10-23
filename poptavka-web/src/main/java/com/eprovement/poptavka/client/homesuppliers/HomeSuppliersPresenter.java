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

/**
 * TODO add description of this module.
 * All changes to CellTree, Table range (Pager), Table Selection is stored to history.
 *
 * @author Martin Slavkovsky
 */
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
    /**
     * Represents CellTree's open nodes hierarchy.
     * According this is created URL token.
     */
    private LinkedList<TreeItem> actualOpenedHierarchy = new LinkedList<TreeItem>();
    /**
     * Represents CellTree's open nodes hierarchy while opening nodes.
     * Before opening process, temporaryOpenedHierarchy is the same as actualOpenedHierarchy.
     * During opening process, first item is pulled and opened.
     * After opening process is complete, temporaryOpenedHierarchy attribute is empty.
     */
    private LinkedList<TreeItem> temporaryOpenedHierarchy = new LinkedList<TreeItem>();
    /**
     * Represents last item of actualOpenedHierarchy, but with type of TreeNode.
     * If lastOpened == null -> the last item is leaf
     */
    private TreeNode lastOpened = null;
    //FLAGS
    //--------------------------------------------------------------------------
    private long selectedSupplier = -1;
    /**
     * Sometimes we only need to perform half of actions.
     */
    /** Stop processing event CellTree.OpenHandler right after node is opened.
     * When using setChildOpen to open node but not fire open event */
    boolean cancelOpenEvent = false;
    /** Stop processing event DataGrid.RangeChangeHandler.
     * When we want to set page size but not fire create token event */
    boolean cancelPagerEvent = false;
    /**
     * To control possible dead locks.
     * There are two different events that need to be handled and perform same actions:
     * - CellTree.OpenHandler
     * - SelectionCategoryModel.SelectionChangeHandler
     * Problem:
     * 1. When selecting NODE of CellTree, it fires OpenHandler event, but doesn't SELECT node's object.
     *    Therefore we need to SELECT object manually when handling OPEN event.
     * 2. When selecting node's OBJECT of CellTree, it fires SelectionChangeHandler event, but doesn't OPEN the node.
     *    Therefore we need to OPEN node manually when handling SELECTION event.
     */
    boolean selectedEvent = false; //tells APP that selection event was made first
    boolean openedEvent = false; //tells APP that open event was made first
    /**************************************************************************/
    /* RPC Service                                                            */
    /* Need RPC service defined here because we need to pass it to CellTree   */
    /* and then to CategoryDataProvider                                       */
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
     * Main Navigation method called either by default application startup or by searching mechanism.
     * @param searchModuleDataHolder - if searching is needed, this object holds conditions to do so.
     *                               - it's also used as pointer to differ root and child sections
     */
    public void onGoToHomeSuppliersModule(SearchModuleDataHolder searchModuleDataHolder, int homeSuppliersViewType) {
        //start up initialization
        actualOpenedHierarchy.clear();
        lastOpened = view.getCellTree().getRootTreeNode();
        openedEvent = false;
        selectedEvent = false;
        //differ cases - BY DEFAULT, BY SEARCH
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

    /**
     * History Navigation method called either when application started from URL
     * or called by back & forward browser actions.
     *
     * @param historyTree - CellTree's open nodes hierarchy stored and parsed from URL.
     * @param categoryDetail - Retrieved CategoryDetail object according to category ID stored and parsed from URL.
     * @param page - Table's page stored and parsed from URL
     * @param supplierID - Supplier's ID stored and parsed from URL
     */
    public void onSetModuleByHistory(
            LinkedList<TreeItem> historyTree, CategoryDetail categoryDetail, int page, long supplierID) {
        //1 - TREE
        //----------------------------------------------------------------------
        //invoked by URL
        //When app started from scrach, actualOpenedHierarchy is empty, because it only stores
        //CellTree's open nodes hierarchy of actual app state.
        if (actualOpenedHierarchy.isEmpty()) {
            //Therefore, if this method was called and actualOpenedHierarchy is empty,
            //app was invoked from URL, no by back & forward events (actualOpenedHierarchy wouldn't be empty)
            //Open CellTree's nodes accorting to hierarchy stored in URL
            openNodesHierarchy(historyTree);
        } else {
            //So, if this method was called and actualOpenedHierarchy is NOT empty,
            //back & forward events were performed
            //And if actual open nodes state differs to the one in URL, we can define
            //according to the length of those lists, what kind of action was performed - back or forward
            if (actualOpenedHierarchy.size() > historyTree.size()) {
                /** BACK action was performed. */
                //if leaf is going to be closed - deny
                if (!lastOpened.isChildLeaf(actualOpenedHierarchy.getLast().getIndex())) {
                    lastOpened = lastOpened.getParent();
                    lastOpened.setChildOpen(actualOpenedHierarchy.getLast().getIndex(), false);
                }
            } else if (actualOpenedHierarchy.size() < historyTree.size()) {
                /** FORWARD action was performed. */
                cancelOpenEvent = true; //stop processing event right after node is opened
                //if leaf is going to be opened - deny
                if (!lastOpened.isChildLeaf(historyTree.getLast().getIndex())) {
                    lastOpened = lastOpened.setChildOpen(historyTree.getLast().getIndex(), true);
                }
            }
        }
        //Set actual state of CellTree's open nodes hierarchy according to history one.
        this.actualOpenedHierarchy = historyTree;

        //2 - TABLE
        //----------------------------------------------------------------------
        //select CATEGORY and PAGER

        if (!categoryDetail.equals(view.getSelectionCategoryModel().getSelectedObject())) {
            openedEvent = true; //node events have already been handled - in 1 - Tree part
            //Select category
            //Selection can be handled by part one (see above) by not denying open events.
            //But when app is invoked from URL, opening nodes can take some presious time, therefore
            //open nodes and at the same time select category (despite it's not visible yet),
            //but it fires an event for retrieving data - and that can take also some presious time
            //-> do it at the same time - there is everything asynchronous anyway
            view.getSelectionCategoryModel().setSelected(categoryDetail, true);
        } else {
            //If Selection is different from actual one -> new category is going to be selected.
            //Therefore we need to reset pager to 0 page and cancel event whitch creates token (See Bind method).
            //No new token is wanted, because this all is already called because of history,
            //therefore there is already token - the one we are resuming/reestablishing now.
            cancelPagerEvent = true;
            view.getPager().setPage(page);
        }

        //3 - supplier DETAIL
        //----------------------------------------------------------------------
        FullSupplierDetail supplierDetail = (FullSupplierDetail) ((SingleSelectionModel) view.getDataGrid()
                .getSelectionModel()).getSelectedObject();
        //If there is already selected some supplier detail and we are resuming history state
        //where there is no selection, deselect table selection and hide detail widget.
        if (supplierID == -1 && supplierDetail != null) {
            view.hideSuppliersDetail();
            ((SingleSelectionModel) view.getDataGrid().getSelectionModel()).setSelected(supplierDetail, false);
        }
        //But if there should be selected some supplier detail object, remember its ID and when
        //data are retrieved, select it (See displaySuppliers method).
        this.selectedSupplier = supplierID;
    }

    /**************************************************************************/
    /* Bind events                                                            */
    /**************************************************************************/
    /**
     * Events description.
     * - DataGrid.RangeChange
     *        - creates token when table page was changed
     *        - invoked when table page changed
     * - CellTree.LoadingState
     *        - opens node when data are finally retrieve
     *        - needs to be done like this because data retrieving is done asynchronously
     *          and opening nodes is done faster than retrieving data (child nodes).
     *          Therefore we are trying open something that doesn't exist at that time
     *          -> we need to wait until data are retrieved
     *        - used when application is opening nodes, not user
     *        - invoked when CellTree's categories are retrieved (See CategoryDataProvider class)
     * - CellTree.Open
     *        - opens node and selects node's object in category selection model
     *        - also manages CellTree's open nodes actual hierarchy state and update last opened node
     *        - invoked each time opening node is performed - application or user
     * - CellTree.Selection
     *        - according to selection retrieve data to table
     *        - also if selected object (node) is not opened, open it
     *        - invoked when selecting category detail object in category selection model.
     *          CategorySelectionModel is bind to CellTree
     */
    @Override
    public void bindView() {
        dataGridRangeChange();
        cellTreeLoadingStateChange();
        cellTreeOpenHandler();
        selectionCategoryChangeHandler();
        dataGridSelectioModelCgangeHandler();
        pageSizeChangeHandler();
    }

    /**************************************************************************/
    /* Bind Handlers                                                          */
    /**************************************************************************/
    private void dataGridRangeChange() {
        view.getDataGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                //In some cases we need to set pager size to 0, which fires this event
                //but we don't want to create token, therefore deny fired event if
                //cancelPagerEvent flag is True.
                if (cancelPagerEvent) {
                    cancelPagerEvent = false;
                } else {
                    createTokenForHistory(selectedSupplier == -1 ? false : true);
                }
            }
        });
    }

    private void cellTreeLoadingStateChange() {
        view.getCellTree().addHandler(new LoadingStateChangeEvent.Handler() {
            @Override
            public void onLoadingStateChanged(LoadingStateChangeEvent event) {
                if (!temporaryOpenedHierarchy.isEmpty()) {
                    cancelOpenEvent = true; //stop processing event right after node is opened
                    lastOpened.setChildOpen(temporaryOpenedHierarchy.removeFirst().getIndex(), true);
                }
            }
        }, LoadingStateChangeEvent.TYPE);
    }

    private void cellTreeOpenHandler() {
        view.getCellTree().addOpenHandler(new OpenHandler<TreeNode>() {
            @Override
            public void onOpen(OpenEvent<TreeNode> event) {
                /**************************************************************/
                //get selected node object
                CategoryDetail selectedCategory = (CategoryDetail) event.getTarget().getValue();
                //update last open node state
                lastOpened = event.getTarget();
                /**************************************************************/
                //cancel event if needed - when opening nodes by application
                if (cancelOpenEvent) {
                    cancelOpenEvent = false;
                    selectedEvent = false;
                    return;
                }
                /**************************************************************/
                openedEvent = true; //OPEN NODE event's semafor BEGIN >>>>>>>
                //If opening node was done first, select opened node's object in selection model
                if (!selectedEvent) {
                    view.getSelectionCategoryModel().setSelected(selectedCategory, true);
                } else {
                    openedEvent = false; //OPEN NODE event's semafor END <<<<<<<
                }
                selectedEvent = false; //SELECT CATEGORY event's semafor END <<<<<<<
                /**************************************************************/
                //len ak by uzivatel zvolil uplne inu vetvu - zavri vsetky a otvor len novo zvolenu
                //User could select entirely different category - from different level and index
                //Therefore define this cases and update opened hierarchy
                if (manageOpenedHierarchy(selectedCategory, event.getTarget())) {
                    //If this is such case, update also last opened node
                    modifyLastOpenedIfNeeded(selectedCategory);
                    cancelOpenEvent = true;
                    //And finally open that node
                    openNode(selectedCategory);
                }
            }
        });
    }

    private void selectionCategoryChangeHandler() {
        view.getSelectionCategoryModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //get selected item
                CategoryDetail selected = (CategoryDetail) view.getSelectionCategoryModel().getSelectedObject();

                selectCategoryChangeHandlerInner(selected);
            }
        });
    }

    private void selectCategoryChangeHandlerInner(CategoryDetail selected) {
        if (selected != null) {
            //open NODES if selection made first
            //-----------------------------------------------------------
            selectedEvent = true; //SELECT CATEGORY event's semafor BEGIN >>>>>>>
            //open selected item's node
            if (!openedEvent) {
                //User could select totaly different node - different level, index
                //Therefore define such case and update last opened node
                modifyLastOpenedIfNeeded(selected);
                cancelOpenEvent = true;
                //Open node refering to selected object
                openNode(selected);
                //update CellTree's open nodes hierarchy actual state
                manageOpenedHierarchy(selected, lastOpened);
            } else {
                selectedEvent = false; //SELECT CATEGORY event's semafor END <<<<<<<
            }
            openedEvent = false; //OPEN NODE event's semafor END <<<<<<<

            //Initialization - table, pager ... create token if need
            //----------------------------------------------------------
            //If new category selected, selected supplier won't match -> deselect
            deselectGridSelection();
            //and hide supplier detail widget
            view.hideSuppliersDetail();
            //Clear data - causes loading indicator to show until new data are retrieved
            view.getDataGrid().clearData();
            //if page and pager.page is equal, setting page won't fire rangeChange event -> manual
            //v podstate to bude volane takto vzdy (==ak nenastala ziadna zmena),
            //ak ale nastane zmena stranky tabulky, vtedy token vytvory iny hadler - rangeChanged na dataGride
            if (view.getPager().getPage() == 0) {
                //If new category is selected, pager must be set to page 0,
                //but if page is already 0, range change event won't ne fire,
                //therefor new token won't be created -> create token here
                createTokenForHistory(false);
            } else {
                //If new category is selected, pager must be set to page 0
                //It also fires range change event which creates new token
                view.getPager().setPage(0);
            }

            //Retrieve data
            //----------------------------------------------------------
            //Set selected category as filter and pass it to filter through that category
            SearchModuleDataHolder searchDataHolder = new SearchModuleDataHolder();
            searchDataHolder.getCategories().add(selected);

            //Retrieve data
            view.getDataGrid().getDataCount(eventBus, new SearchDefinition(searchDataHolder));
        } else {
            //If no category is selected, close all nodes
            closeAllNodes(view.getCellTree().getRootTreeNode());
        }
    }

    private void dataGridSelectioModelCgangeHandler() {
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //get selected supplier
                FullSupplierDetail selected =
                        (FullSupplierDetail) ((SingleSelectionModel) view.getDataGrid().getSelectionModel())
                        .getSelectedObject();

                if (selected != null) {
                    //retrieve supplier detail info and display it
                    view.displaySuppliersDetail(selected);
                    //create token for this selection
                    createTokenForHistory(false);
                }
            }
        });
    }

    private void pageSizeChangeHandler() {
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
    //TODO unused ???
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

    /**************************************************************************/
    /* Private methods                                                        */
    /**************************************************************************/
    /**
     * Manages openedHierarchy attributes due to new user selection.
     * If user selected category of different or same level, update CellTree's open nodes hierarchy
     * actual state to match new selection.
     *
     * @param selectedCategory
     * @param openedNode
     * @return define if update/correction was made
     */
    private boolean manageOpenedHierarchy(CategoryDetail selectedCategory, TreeNode openedNode) {
        //remove all levels which levels are more or even to selected category
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
                openedNode == null ? getIndex(openedNode, selectedCategory) : openedNode.getIndex()));

        return removeCount == 0 ? false : true;
    }

    /**
     * Updates last opened node according to given category detail.
     * If last opened node's level is is not referring to selected category's level,
     * find parent that refers the same and make it last opened node.
     *
     * @param selectedCategory
     * @return true if last opened node was update, false otherwise
     */
    private boolean modifyLastOpenedIfNeeded(CategoryDetail selectedCategory) {
        boolean was = false;
        //nemusi byt - len kvoli rychlosti, ale to asi nebude postrehnutelne
        if (selectedCategory.getLevel() == 1) {
            lastOpened = view.getCellTree().getRootTreeNode();
        } else {
            int lastOpenedLevel = ((CategoryDetail) lastOpened.getValue()).getLevel();
            for (int i = lastOpenedLevel; i >= 1; i--) {
                if (selectedCategory.getLevel() < i) {
                    lastOpened = lastOpened.getParent();
                    was = true;
                }
            }
        }
        return was;
    }

    /**
     * Open nodes according to <b>CellTree's open nodes Hierarchy</b> attribute.
     * It opens first item from list and the others are opened when child nodes of opened
     * node are retrieved. Otherwise we cannot open something that doesn't exist wile opening process.
     * The rest of openings is made in CellTree.LoadingStateChange event handler.
     * Opening process is done according to <b>temporaryOpenedHierarchy</b> attribute
     *          - needs for asynchronous opening of nodes
     *          - after each time data are retrieved, fist item is pulled and opened
     *            until list is empty.
     *
     * @param hierarchy - CellTree's open nodes hierarchy parsed from URL
     */
    private void openNodesHierarchy(LinkedList<TreeItem> hierarchy) {
        //First close all nodes
        closeAllNodes(view.getCellTree().getRootTreeNode());
        cancelOpenEvent = true; //stop processing event right after node is opened
        openedEvent = true;
        temporaryOpenedHierarchy = new LinkedList<TreeItem>(hierarchy); //make a copy

        lastOpened = view.getCellTree().getRootTreeNode();
        //Open first node, others in CellTree.LoadingStateChange event handler after child nodes are retieved.
        lastOpened.setChildOpen(temporaryOpenedHierarchy.removeFirst().getIndex(), true);
    }

    /**
     * Opens child node of last opened node represented by given selected category.
     * @param selected - selected category
     * @return null if opening fails, TreeNode of opened child
     */
    private TreeNode openNode(CategoryDetail selected) {
        closeAllNodes(lastOpened);
        int idx = getIndex(lastOpened, selected);
        return lastOpened.setChildOpen(idx, true);
    }

    /**
     * Close all child nodes of given node.
     * @param node
     */
    private void closeAllNodes(TreeNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            node.setChildOpen(i, false);
        }
    }

    /**
     * Return index of given selected category that refers to a given node's child.
     * @param node
     * @param categoryDetail
     * @return
     */
    private int getIndex(TreeNode node, CategoryDetail categoryDetail) {
        for (int idx = 0; idx < node.getChildCount(); idx++) {
            if (categoryDetail.equals((CategoryDetail) node.getChildValue(idx))) {
                return idx;
            }
        }
        return -1;
    }

    /**
     * Cancel table selection.
     */
    private void deselectGridSelection() {
        SingleSelectionModel selectionModel = (SingleSelectionModel) view.getDataGrid().getSelectionModel();
        FullSupplierDetail supplier = (FullSupplierDetail) (selectionModel).getSelectedObject();
        selectionModel.setSelected(supplier, false);
    }

    /**
     * Creates token for history according to CellTree's open nodes hierarchy, table page, table selection.
     * @param setStorageHistoryPointerValue
     */
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