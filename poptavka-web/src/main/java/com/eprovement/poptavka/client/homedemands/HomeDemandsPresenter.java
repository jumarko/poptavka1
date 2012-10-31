package com.eprovement.poptavka.client.homedemands;

import com.eprovement.poptavka.client.common.session.Constants;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.homesuppliers.TreeItem;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
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
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DecoratorPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
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
 * This presenter is to replace DemandsPresenter.java.
 *
 * @author praso, Martin Slavkovsky
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
        UniversalAsyncGrid<FullDemandDetail> getDataGrid();

        SimplePager getPager();

        ListBox getPageSizeCombo();

        int getPageSize();

        //Detail
        void displayDemandDetail(FullDemandDetail fullDemandDetail);

        void hideDemandDetail();

        //Filter
        DecoratorPanel getFilterLabelPanel();

        Label getFilterLabel();

        //Buttons
        Button getOfferBtn1();

        Button getOfferBtn2();

        //Other
        Widget getWidgetView();
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
    private long selectedDemand = -1;
    /**
     * Sometimes we only need to perform half of actions.
     */
    /** Stop processing event CellTree.OpenHandler right after node is opened.
     * When using setChildOpen to open node but not fire open event */
    boolean cancelOpenEvent = false;
    /** Stop processing event DataGrid.RangeChangeHandler.
     * When we want to set page size but not fire create token event */
    boolean cancelPagerEvent = false;
    int pageFromToken = -1;
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
    /* RPC Service*/
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
    public void onGoToHomeDemandsModule(SearchModuleDataHolder filter) {
        //start up initialization
        actualOpenedHierarchy.clear();
        lastOpened = view.getCellTree().getRootTreeNode();
        openedEvent = false;
        selectedEvent = false;

        if (filter == null) {
            //HOME_DEMANDS_BY_DEFAULT
            goToHomeDemands();
        } else {
            //HOME_DEMANDS_BY_WELCOME
            if (filter.getSearchText().isEmpty()
                    && filter.getLocalities().isEmpty()
                    && filter.getAttributes().isEmpty()
                    && filter.getCategories().size() == 1) {
                goToHomeDemands(filter.getCategories().get(0));
            } else {
                //HOME_DEMANDS_BY_SEARCH
                goToHomeDemands(filter);
            }
        }
    }

    /**
     * Forwarded from menu selection.
     */
    private void goToHomeDemands() {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS_BY_DEFAULT);
        //Set visibility
        view.getFilterLabel().setText("");
        view.getFilterLabelPanel().setVisible(false);
        view.getSelectionCategoryModel().setSelected(view.getSelectionCategoryModel().getSelectedObject(), false);
        closeAllNodes(view.getCellTree().getRootTreeNode());
        //get data
        view.getDataGrid().getDataCount(eventBus, new SearchDefinition(null));
    }

    /**
     * Forwarded from welcome view - root category selection
     * @param rootCategoryDetail - category detail object selected from root categories menu on welcome view
     */
    private void goToHomeDemands(CategoryDetail rootCategoryDetail) {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS_BY_WELCOME);
        //select given category
        view.getSelectionCategoryModel().setSelected(rootCategoryDetail, true);
        //open appropiate node
        openNode(getIndex(view.getCellTree().getRootTreeNode(), rootCategoryDetail));
        //remove text
        view.getFilterLabel().setText("");
        //set visibility
        view.getFilterLabelPanel().setVisible(false);
    }

    /**
     * Forwarded from search module, when searching criteria were provided.
     * @param searchDataHolder - searching criteria
     */
    private void goToHomeDemands(SearchModuleDataHolder searchDataHolder) {
        Storage.setCurrentlyLoadedView(Constants.HOME_DEMANDS_BY_SEARCH);
        //unselect categories
        view.getSelectionCategoryModel().setSelected(view.getSelectionCategoryModel().getSelectedObject(), false);
        //close celltree nodes
        closeAllNodes(view.getCellTree().getRootTreeNode());
        //display message that search was performed
        //Ak bude text stale rovnaky, nemusi sa setovat tu (moze v UiBinderi),
        //ale ak bude dynamicky (zobrazia searching criteria), tak ano
        view.getFilterLabel().setText("Results satisfying searching criteria:" + searchDataHolder.toString());
        //set visibility
        view.getFilterLabelPanel().setVisible(true);
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
     * @param demandID - Demand's ID stored and parsed from URL
     */
    public void onSetModuleByHistory(
            LinkedList<TreeItem> historyTree, CategoryDetail categoryDetail, int page, long demandID) {
        //force table to show loading indicator
        view.getPager().startLoading();

        //1 - TREE
        //----------------------------------------------------------------------
        //invoked by URL
        //When app started from scrach, actualOpenedHierarchy is empty, because it only stores
        //CellTree's open nodes hierarchy of actual app state.
        if (actualOpenedHierarchy.isEmpty()) {
            //Therefore, if this method was called and actualOpenedHierarchy is empty,
            //app was invoked from URL, no by back & forward events (actualOpenedHierarchy wouldn't be empty)
            //Open CellTree's nodes accorting to hierarchy stored in URL
            //Set fag to false - tells app that nodes will be opened programicaly, not by user
            selectedEvent = true;
            //Opening will be performed "from scratch" - from CellTree root.
            lastOpened = view.getCellTree().getRootTreeNode();
            //"From scratch" - user while stored URL hierarchy
            temporaryOpenedHierarchy = new LinkedList<TreeItem>(historyTree); //make a copy
            if (!temporaryOpenedHierarchy.isEmpty()) {
                openNode(temporaryOpenedHierarchy.removeFirst().getIndex());
            }
        } else {
            //So, if this method was called and actualOpenedHierarchy is NOT empty,
            //back & forward events were performed
            //And if actual open nodes state differs to the one in URL, we can define
            //according to the length of those lists, what kind of action was performed - back or forward
            if (actualOpenedHierarchy.size() > historyTree.size()) {
                /** BACKWARDS opening must be performed.
                 * - open node to the left from actual node **/
                selectedEvent = true;
                modifyLastOpened(categoryDetail.getLevel());
                modifyTemporaryHierarchy(historyTree, categoryDetail.getLevel());
                if (!temporaryOpenedHierarchy.isEmpty()) {
                    openNode(temporaryOpenedHierarchy.removeFirst().getIndex());
                }
            } else if (actualOpenedHierarchy.size() <= historyTree.size()) {
                /** FORWARD opening must be performed.
                 * - open node to the right from actual node **/
                selectedEvent = true;
                int level = getLevel();
                modifyLastOpened(level);
                modifyTemporaryHierarchy(historyTree, level);
                if (!temporaryOpenedHierarchy.isEmpty()) {
                    openNode(temporaryOpenedHierarchy.removeFirst().getIndex());
                }
            }
        }
        //Set actual state of CellTree's open nodes hierarchy according to history one.
        this.actualOpenedHierarchy = historyTree;

        //2 - TABLE
        //----------------------------------------------------------------------
        //select CATEGORY and PAGER

        //If Selection is different from actual one -> new category is going to be selected.
        //Therefore we need to reset pager to 0 page and cancel event whitch creates token (See Bind method).
        //No new token is wanted, because this all is already called because of history,
        //therefore there is already token - the one we are resuming/reestablishing now.
        //reset table and pager to particular state, but data retrieving leave to categorySelectionEvent.
        this.pageFromToken = page;
        openedEvent = true; //open nodes event have already been handled - in 1 - Tree part
        //Select category
        //Selection can be handled by part one (see above) by not denying open events.
        //But when app is invoked from URL, opening nodes can take some presious time, therefore
        //open nodes and at the same time select category (despite it's not visible yet),
        //but it fires an event for retrieving data - and that can take also some presious time
        //-> do it at the same time - there is everything asynchronous anyway
        view.getSelectionCategoryModel().setSelected(categoryDetail, true);

        //3 - demand DETAIL
        //----------------------------------------------------------------------
        FullDemandDetail demandDetail = (FullDemandDetail) ((SingleSelectionModel) view.getDataGrid()
                .getSelectionModel()).getSelectedObject();
        //If there is already selected some demand detail and we are resuming history state
        //where there is no selection, deselect table selection and hide detail widget.
        if (demandID == -1 && demandDetail != null) {
            view.hideDemandDetail();
            ((SingleSelectionModel) view.getDataGrid().getSelectionModel()).setSelected(demandDetail, false);
        }
        //But if there should be selected some demand detail object, remember its ID and when
        //data are retrieved, select it (See displayDemands method).
        this.selectedDemand = demandID;
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
        dataGridRangeChangeHandler();
        cellTreeLoadingStateChangeHandler();
        cellTreeOpenHandler();
        selectionCategoryChangeHandler();
        dataGridSelectioModelChangeHandler();
        pageSizeChangeHandler();
    }

    /**************************************************************************/
    /* Bind Handlers                                                          */
    /**************************************************************************/
    /**
     * Handle table range change by creating token for new range/page.
     */
    private void dataGridRangeChangeHandler() {
        view.getDataGrid().addRangeChangeHandler(new RangeChangeEvent.Handler() {
            @Override
            public void onRangeChange(RangeChangeEvent event) {
                pageFromToken = -1;
                //In some cases we need to set pager size to 0, which fires this event
                //but we don't want to create token, therefore deny fired event if
                //cancelPagerEvent flag is True.
                if (cancelPagerEvent) {
                    cancelPagerEvent = false;
                } else {
                    createTokenForHistory(selectedDemand == -1 ? false : true);
                }
            }
        });
    }

    /**
     * Open node's child according to temporary opened hierarchy. Must be done this
     * way, because, cellTree's nodes are retrieved asynchronously, therefore sometimes
     * we are trying to open something that doesn't have nodes yet, because they are
     * not retrieved yet. Therefore way, until retrieving process ends and that open wanted node.
     */
    private void cellTreeLoadingStateChangeHandler() {
        view.getCellTree().addHandler(new LoadingStateChangeEvent.Handler() {
            @Override
            public void onLoadingStateChanged(LoadingStateChangeEvent event) {
                if (!temporaryOpenedHierarchy.isEmpty()) {
                    selectedEvent = true;
                    lastOpened.setChildOpen(temporaryOpenedHierarchy.removeFirst().getIndex(), true);
                }
            }
        }, LoadingStateChangeEvent.TYPE);
    }

    /**
     * Handle user selection when opening CellTree nodes.
     * Only one subtree is displayed.
     */
    private void cellTreeOpenHandler() {
        view.getCellTree().addOpenHandler(new OpenHandler<TreeNode>() {
            @Override
            public void onOpen(OpenEvent<TreeNode> event) {
                /**************************************************************/
                //get selected node object
                CategoryDetail selectedCategory = (CategoryDetail) event.getTarget().getValue();

                /**************************************************************/
                openedEvent = true; //OPEN NODE event's semafor BEGIN >>>>>>>
                //If opening node was done first, select opened node's object in selection model
                if (!selectedEvent) {
                    view.getSelectionCategoryModel().setSelected(selectedCategory, true);
                    /**************************************************************/
                    //User could select entirely different category - from different level and index
                    //Therefore define this cases and update opened hierarchy
                    if (manageOpenedHierarchy(selectedCategory, event.getTarget().getIndex())) {
                        //If this is such case, update also last opened node
                        modifyLastOpened(selectedCategory.getLevel());
                        //Modify actual opened hierarchy
                        modifyTemporaryHierarchy(actualOpenedHierarchy, selectedCategory.getLevel() + 1);
                        //And close all nodes except selected one.
                        closeAllNodesExcept(lastOpened, actualOpenedHierarchy.getLast().getIndex());
                    }
                } else {
                    openedEvent = false; //OPEN NODE event's semafor END <<<<<<<
                }
                selectedEvent = false; //SELECT CATEGORY event's semafor END <<<<<<<

                //update last open node state
                lastOpened = event.getTarget();
            }
        });
    }

    /**
     * Handle user selection event on CellTree, when selecting node's object/value.
     * Retrieve data according to selected category and open & close nodes.
     * Only one subtree is displayed.
     */
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
            //User selection was made -> reset filtering
            view.getFilterLabelPanel().setVisible(false);
            //open NODES if selection made first
            //-----------------------------------------------------------
            selectedEvent = true; //SELECT CATEGORY event's semafor BEGIN >>>>>>>
            //open selected item's node
            if (!openedEvent) {
                //User could select totaly different node - different level, index
                //Therefore define such case and update last opened node
                modifyLastOpened(selected.getLevel());
                //Manage actual opened hierarchy according to user selection
                manageOpenedHierarchy(selected, getIndex(lastOpened, selected));
                //According to opened hierarchy, modify temporary
                //Add +1 to level, because of user selection input
                modifyTemporaryHierarchy(actualOpenedHierarchy, selected.getLevel() + 1);
                openNode(getIndex(lastOpened, selected));

            } else {
                selectedEvent = false; //SELECT CATEGORY event's semafor END <<<<<<<
            }
            openedEvent = false; //OPEN NODE event's semafor END <<<<<<<

            //Initialization - table, pager ... create token if need
            //----------------------------------------------------------
            //If new category selected, selected demand won't match -> deselect
            deselectGridSelection();
            //and hide demand detail widget
            view.hideDemandDetail();
            view.getPager().startLoading();
            //if page and pager.page is equal, setting page won't fire rangeChange event -> manual
            //v podstate to bude volane takto vzdy (==ak nenastala ziadna zmena),
            //ak ale nastane zmena stranky tabulky, vtedy token vytvory iny hadler - rangeChanged na dataGride
            if (pageFromToken == -1) { //no history
                //If new category is selected, pager must be set to page 0,
                //but if page is already 0, range change event won't ne fire,
                //therefor new token won't be created -> create token here
                createTokenForHistory(false);
            } else {
                //If new category is selected, pager must be set to page 0
                //It also fires range change event which creates new token
                cancelPagerEvent = true;
                view.getDataGrid().cancelRangeChangedEvent();
                view.getDataGrid().setPageStart(pageFromToken * view.getDataGrid().getPageSize());
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

    /**
     * Display demand detail in detail view when selected by user.
     */
    private void dataGridSelectioModelChangeHandler() {
        view.getDataGrid().getSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                //get selected demand
                FullDemandDetail selected =
                        (FullDemandDetail) ((SingleSelectionModel) view.getDataGrid().getSelectionModel())
                        .getSelectedObject();

                if (selected != null) {
                    //retrieve demand detail info and display it
                    view.displayDemandDetail(selected);
                    //create token for this selection
                    createTokenForHistory(false);
                }
            }
        });
    }

    /**
     * Reset pager and table when page size changed.
     */
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
    /**
     * Used to select table item. When demand id is parsed from token, demand
     * object is retrieved and then selected.
     * @param demandDetail
     */
    public void onSelectDemand(FullDemandDetail demandDetail) {
        view.getDataGrid().getSelectionModel().setSelected(demandDetail, true);
    }

    /**
     * Display demands of selected category.
     * @param list
     */
    public void onDisplayDemands(List<FullDemandDetail> result) {
        view.getDataGrid().getDataProvider().updateRowData(view.getDataGrid().getStart(), result);
        //If demand must be selected, get its detail and select in selectionModel
        if (selectedDemand != -1) {
            eventBus.getDemand(selectedDemand);
        }
//        view.getDataGrid().redraw();
    }

    /**************************************************************************/
    /* Private methods                                                        */
    /**************************************************************************/
    /** FLAG/POINTER MODIFICATION methods. **/
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
    private boolean manageOpenedHierarchy(CategoryDetail selectedCategory, int index) {
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
                index));

        return removeCount == 0 ? false : true;
    }

    /**
     * Get last opened node's level.
     * @return level of lastOpened node or
     *         -1 if actual opened hierarchy doesn't contain last opened node's value.
     */
    private int getLevel() {
        CategoryDetail lastDetail = (CategoryDetail) lastOpened.getValue();
        int level = 1;
        for (TreeItem item : actualOpenedHierarchy) {
            if (item.getCategoryId() == lastDetail.getId()) {
                return level;
            }
            level++;
        }
        return -1;
    }

    /**
     * Updates last opened node according to given level.
     * If last opened node's level is is not referring to given level,
     * find parent that refers the same level and make it last opened node.
     *
     * @param toLevel - level to which we want to find parent of lastOpened node
     */
    private void modifyLastOpened(int toLevel) {
        if (toLevel == 1) {
            lastOpened = view.getCellTree().getRootTreeNode();
        } else {
            int lastOpenedLevel = ((CategoryDetail) lastOpened.getValue()).getLevel();
            for (int i = lastOpenedLevel; i >= 1; i--) {
                if (toLevel <= i) {
                    lastOpened = lastOpened.getParent();
                }
            }
        }
    }

    /**
     * Update/set temporary opened hierarchy according to given hierarchy and level.
     * Copy given hierarchy to temporary one and remove as many first items as given level tells.
     *
     * @param hierarchy - given hierarchy what will be copied to temporary opened hierarchy
     * @param toLevel - level to which should be first items removed
     */
    private void modifyTemporaryHierarchy(LinkedList<TreeItem> hierarchy, int toLevel) {
        if (!hierarchy.isEmpty()) {
            temporaryOpenedHierarchy = new LinkedList<TreeItem>(hierarchy); //make a copy
            for (int i = 0; i < hierarchy.size(); i++) {
                if (hierarchy.get(i).getLevel() < toLevel) {
                    temporaryOpenedHierarchy.removeFirst();
                }
            }
        }
    }

    /** OPEN methods. **/
    /**************************************************************************/
    /**
     * Opens last opened child node at given index.
     * @param indexToOpen - child index
     * @return <b>null</b> if opening fails or
     *         <b>TreeNode</b> of opened child
     */
    private TreeNode openNode(int indexToOpen) {
        //If selecting same level, but different node, close originals
        closeAllNodes(lastOpened);

        if (lastOpened != null) {
            return lastOpened.setChildOpen(indexToOpen, true);
        }
        return null;
    }

    /** CLOSE methods. **/
    /**************************************************************************/
    /**
     * Close all child nodes of given node.
     * @param node, which children will be closed.
     */
    private void closeAllNodes(TreeNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            node.setChildOpen(i, false);
        }
    }

    /**
     * Close all child nodes of given node, except given child's index.
     * @param node, which children will be closed.
     * @param exceptIdx child's index, which won't be closed.
     */
    private void closeAllNodesExcept(TreeNode node, int exceptIdx) {
        for (int i = 0; i < node.getChildCount(); i++) {
            if (i != exceptIdx) {
                node.setChildOpen(i, false);
            }
        }
    }

    /** OTHER methods. **/
    /**************************************************************************/
    /**
     * Return index of given selected category that refers to a given node's child.
     * @param node
     * @param categoryDetail
     * @return index
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
        FullDemandDetail demand = (FullDemandDetail) (selectionModel).getSelectedObject();
        selectionModel.setSelected(demand, false);
    }

    /**
     * Creates token for history according to CellTree's open nodes hierarchy, table page, table selection.
     * @param setStorageHistoryPointerValue
     */
    private void createTokenForHistory(boolean setStorageHistoryPointerValue) {
        //Create token only if new input from user was made and new token must be created
        if (!Storage.isCalledDueToHistory()) {
            //Demand
            FullDemandDetail demand =
                    (FullDemandDetail) ((SingleSelectionModel) view.getDataGrid().getSelectionModel())
                    .getSelectedObject();

            eventBus.createTokenForHistory(actualOpenedHierarchy, view.getPager().getPage(), demand);
        }
        //reset poitner - only if it is to no use anymore ->
        //if selected demand's id was present in URL, pointer will be used to decide
        //whether to select demand, or not, after loading data to table.
        Storage.setCalledDueToHistory(setStorageHistoryPointerValue);
    }
}