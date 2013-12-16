package com.eprovement.poptavka.client.catLocSelector.treeBrowser;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorEventBus;
import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.service.demand.CatLocSelectorRPCServiceAsync;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocTreeItem;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.event.logical.shared.CloseEvent;
import com.google.gwt.event.logical.shared.CloseHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

@Presenter(view = TreeBrowserView.class, multiple = true)
public class TreeBrowserPresenter
        extends LazyPresenter<TreeBrowserPresenter.TreeBrowserInterface, CatLocSelectorEventBus>
        implements PresentersInterface {

    public interface TreeBrowserInterface extends LazyView {

        void createTreeBrowser();

        CellTree getCellTree();

        SingleSelectionModel<ICatLocDetail> getTreeSelectionModel();

        Widget getWidgetView();
    }
    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private LinkedList<CatLocTreeItem> todoOpen;
    private LinkedList<CatLocTreeItem> openedHierarchy;
    private TreeNode lastOpened;
    private boolean cancelNextOpenEvent;
    private boolean cancelNextCloseEvent;
    private CatLocSelectorBuilder builder;
    private int instanceId;
    /**************************************************************************/
    /* CatLocRPCServiceAsync                                                */
    /**************************************************************************/
    @Inject
    private CatLocSelectorRPCServiceAsync service;

    @Override
    public CatLocSelectorRPCServiceAsync getService() {
        return service;
    }

    /**************************************************************************/
    /* CellTreeLoadingHandler                                                 */
    /**************************************************************************/
    /**
     * Open node's child according to temporary opened hierarchy. Must be done this
     * way, because, cellTree's nodes are retrieved asynchronously, therefore sometimes
     * we are trying to open something that doesn't have nodes yet, because they are
     * not retrieved yet. Therefore way, until retrieving process ends and that open wanted node.
     */
    @Override
    public LoadingStateChangeEvent.Handler getLoadingHandler() {
        return new LoadingStateChangeEvent.Handler() {
            @Override
            public void onLoadingStateChanged(LoadingStateChangeEvent event) {
                if (todoOpen != null && !todoOpen.isEmpty()
                        && !lastOpened.isChildLeaf(todoOpen.getFirst().getIndex())) {
                    cancelNextOpenEvent = true;
                    lastOpened = lastOpened.setChildOpen(todoOpen.removeFirst().getIndex(), true);
                }
            }
        };
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    private void bindTreeBrowserHanlders() {
        view.getCellTree().addOpenHandler(new OpenHandler<TreeNode>() {
            @Override
            public void onOpen(OpenEvent<TreeNode> event) {
                if (!cancelNextOpenEvent) {
                    //Close previous opened node
                    //Find previous opened node of the same level as the new one
                    CatLocTreeItem sameLevelOldItem = getSameLevelOldItem(
                            openedHierarchy,
                            ((ICatLocDetail) event.getTarget().getValue()).getLevel());
                    //and close it if needed
                    if (sameLevelOldItem != null) {
                        manageLastOpenedNode(sameLevelOldItem.getCatLoc().getLevel());
                        cancelNextCloseEvent = true;
                        lastOpened.setChildOpen(sameLevelOldItem.getIndex(), false);
                    }
                    //set flags according to new selection
                    manageOpenedHierarchy(
                            (ICatLocDetail) event.getTarget().getValue(),
                            event.getTarget().getIndex());
                    lastOpened = event.getTarget();
                    //set the selection to selection model
                    view.getTreeSelectionModel().setSelected((ICatLocDetail) event.getTarget().getValue(), true);
                }
                cancelNextOpenEvent = false;
            }
        });
        view.getCellTree().addCloseHandler(new CloseHandler<TreeNode>() {
            @Override
            public void onClose(CloseEvent<TreeNode> event) {
                if (!cancelNextCloseEvent) {
                    manageOpenedHierarchy(
                            (ICatLocDetail) event.getTarget().getValue(),
                            event.getTarget().getIndex());
                    manageLastOpenedNode(((ICatLocDetail) event.getTarget().getValue()).getLevel());
                }
                cancelNextCloseEvent = false;
            }
        });
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Call when new intance of TreeBrowser is requested.
     */
    public void onInitNewCatLocSelectorTreeBrowser(
            SimplePanel embedWidget, CatLocSelectorBuilder builder, int instanceId) {
        this.builder = builder;
        this.instanceId = instanceId;
        initCatLocSelectorTreeBrowser(embedWidget);
    }

    /**
     * Call when same intance of TreeBrowser is requested.
     */
    public void onInitSameCatLocSelectorTreeBrowser(
            SimplePanel embedWidget, CatLocSelectorBuilder builder, int instanceId) {
        if (this.instanceId == instanceId) {
            initCatLocSelectorTreeBrowser(embedWidget);
        }
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * This method is called on click in onBrowse method in CatLocCell.
     * Method should provide open/close node functionality if selected cell (not tree arrow)
     *
     * @param result nodes hierarchy to restore
     */
    public void onResponseHierarchyForTreeBrowser(LinkedList<CatLocTreeItem> result, int instanceId) {
        if (this.instanceId == instanceId) {
            openedHierarchy = result;
            //nothing opened yet
            if (lastOpened.getValue() != null
                    && lastOpened.getValue().equals(result.getLast().getCatLoc())) {
                //if lastOpen exuals with selected category, it means that its open
                //and it shoud be closed
                lastOpened = lastOpened.getParent();
                closeAllNodes(lastOpened);
            } else {
                //if already selected, fire selection event manually
                if (view.getTreeSelectionModel().isSelected(result.getLast().getCatLoc())) {
                    SelectionChangeEvent.fire(view.getTreeSelectionModel());
                } else {
                    view.getTreeSelectionModel().setSelected(result.getLast().getCatLoc(), true);
                }
                //otherwise open node hierarchy from scrach
                lastOpened = view.getCellTree().getRootTreeNode();
                closeAllNodes(lastOpened);
                openNodes(result);
            }
        }
    }

    /**
     * Append given list with selected categories.
     * If given list is null, initialize it first.
     * Only active presenter response to this call.
     */
    public void onFillCatLocsFromTreeBrowser(List<ICatLocDetail> selectedCatLocs, int instanceId) {
        if (this.instanceId == instanceId) {
            if (selectedCatLocs == null) {
                selectedCatLocs = new ArrayList<ICatLocDetail>();
            }
            if (view.getTreeSelectionModel().getSelectedObject() != null) {
                selectedCatLocs.add(view.getTreeSelectionModel().getSelectedObject());
            }
        }
    }

    /**
     * Register selection model to cellTree in TreeBrowser widget.
     * Since TreeBrowser holds functionality to get data, open, close, select items over cellTree,
     * therefor if another "outside" widget wants to act or to have access to selected items,
     * it must implement its selection model (which holds wanted functionality) and register here.
     * To have access to selected items selectino model must call also
     * @see fillCatLocs(List<CatLocDetail> selectedCatLocs)
     */
    public void onRegisterCatLocTreeSelectionHandler(SelectionChangeEvent.Handler selectionHandler) {
        view.getTreeSelectionModel().addSelectionChangeHandler(selectionHandler);
    }

    /**************************************************************************/
    /* Getter methods                                                         */
    /**************************************************************************/
    @Override
    public int getInstanceId() {
        return instanceId;
    }

    @Override
    public CatLocSelectorBuilder getBuilder() {
        return builder;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Init category selector TreeBrowser.
     * Build TreeBrowser according to given CatLocSelectorBuilder and
     * set TreeBrowser widget to given holder.
     *
     * @param embedWidget - TellBrowser's holder
     * @param builder - defines TellBrowser attributes
     * @param instanceId - distinguish individual instances
     */
    private void initCatLocSelectorTreeBrowser(SimplePanel embedToWidget) {
        //reset in case of calling second time
        view.createTreeBrowser();
        this.bindTreeBrowserHanlders();
        lastOpened = view.getCellTree().getRootTreeNode();
        closeAllNodes(lastOpened);
        openedHierarchy = new LinkedList<CatLocTreeItem>();
        embedToWidget.setWidget(view.getWidgetView());
    }

    /**
     * Manages openedHierarchy attributes due to new user selection.
     * If user selected category of different or same level, update CellTree's open nodes hierarchy
     * actual state to match new selection.
     *
     * @param selectedCatLoc
     * @param openedNode
     * @return define if update/correction was made
     */
    private void manageOpenedHierarchy(ICatLocDetail selectedCatLoc, int index) {
        //remove all levels which levels are more or even to selected category
        Iterator<CatLocTreeItem> iterator = openedHierarchy.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getCatLoc().getLevel() >= selectedCatLoc.getLevel()) {
                iterator.remove();
            }
        }
        //replace last level index with actual one - selected by user
        openedHierarchy.add(new CatLocTreeItem(selectedCatLoc, index));
    }

    /**
     * Updates last opened node according to given level.
     * If last opened node's level is is not referring to given level,
     * find parent that refers the same level and make it last opened node.
     *
     * @param toLevel - level to which we want to find parent of lastOpened node
     */
    private void manageLastOpenedNode(int toLevel) {
        int lastOpenedLevel = ((ICatLocDetail) lastOpened.getValue()).getLevel();
        for (; lastOpenedLevel > 0; lastOpenedLevel--) {
            if (toLevel <= lastOpenedLevel) {
                lastOpened = lastOpened.getParent();
            }
        }
    }

    /**
     * If new selection was made, we want to close old opened node at the same level as the new one.
     * Therefore according to given actual opened hierarchy return item of given level.
     *
     * @param hierarchy opened hierarchy
     * @param level of which item is returned
     * @return item of given level
     */
    private CatLocTreeItem getSameLevelOldItem(LinkedList<CatLocTreeItem> hierarchy, int level) {
        for (CatLocTreeItem item : hierarchy) {
            if (item.getCatLoc().getLevel() == level) {
                return item;
            }
        }
        return null;
    }

    /**
     * Open node hierarchy according to given node hierarchy.
     * However child nodes are requested asynchronously, method only opens first node.
     * The rest is opened by LoadingStateChangehandler implementation when child nodes are available.
     *
     * @param result
     */
    private void openNodes(LinkedList<CatLocTreeItem> result) {
        if (result != null && !result.isEmpty()) {
            todoOpen = new LinkedList<CatLocTreeItem>(result);
            cancelNextOpenEvent = true;
            lastOpened = lastOpened.setChildOpen(todoOpen.removeFirst().getIndex(), true);
        }
    }

    /**
     * Close all child nodes of given node.
     * @param node, which children will be closed.
     */
    private void closeAllNodes(TreeNode node) {
        for (int i = 0; i < node.getChildCount(); i++) {
            cancelNextCloseEvent = true;
            node.setChildOpen(i, false);
        }
    }
}
