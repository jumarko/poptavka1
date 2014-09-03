/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.treeBrowser;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorEventBus;
import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.service.demand.CatLocSelectorRPCServiceAsync;
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
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Use for browsing and selecting items in a tree hirarchy.
 *
 * @author Martin Slavkovsky
 */
@Presenter(view = TreeBrowserView.class, multiple = true)
public class TreeBrowserPresenter
        extends LazyPresenter<TreeBrowserPresenter.TreeBrowserInterface, CatLocSelectorEventBus>
        implements PresentersInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface TreeBrowserInterface extends LazyView {

        void createTreeBrowser();

        CellTree getCellTree();

        SingleSelectionModel<ICatLocDetail> getTreeSelectionModel();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private LinkedList<ICatLocDetail> todoOpen;
    private LinkedList<ICatLocDetail> openedHierarchy;
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
                        && !lastOpened.isChildLeaf(getIndex(todoOpen.getFirst()))) {
                    cancelNextOpenEvent = true;
                    lastOpened = lastOpened.setChildOpen(getIndex(todoOpen.removeFirst()), true);
                }
            }
        };
    }

    /**************************************************************************/
    /* Bind                                                                   */
    /**************************************************************************/
    /**
     * Binds cellTree open & close handlers.
     */
    private void bindTreeBrowserHanlders() {
        view.getCellTree().addOpenHandler(new OpenHandler<TreeNode>() {
            @Override
            public void onOpen(OpenEvent<TreeNode> event) {
                if (!cancelNextOpenEvent) {
                    //Close previous opened node
                    //Find previous opened node of the same level as the new one
                    ICatLocDetail sameLevelOldItem = getSameLevelOldItem(
                            openedHierarchy,
                            ((ICatLocDetail) event.getTarget().getValue()).getLevel());
                    //and close it if needed
                    if (sameLevelOldItem != null) {
                        manageLastOpenedNode(sameLevelOldItem.getLevel());
                        cancelNextCloseEvent = true;
                        lastOpened.setChildOpen(getIndex(sameLevelOldItem), false);
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
    public void onInitNewCatLocSelectorTreeBrowser(SimplePanel embedWidget, CatLocSelectorBuilder builder) {
        this.builder = builder;
        this.instanceId = builder.getInstanceId();
        initCatLocSelectorTreeBrowser(embedWidget);
        if (builder.getHandler() != null) {
            view.getTreeSelectionModel().addSelectionChangeHandler(builder.getHandler());
        }
    }

    /**
     * Call when same intance of TreeBrowser is requested.
     */
    public void onInitSameCatLocSelectorTreeBrowser(SimplePanel embedWidget, CatLocSelectorBuilder builder) {
        if (this.instanceId == builder.getInstanceId()) {
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
    public void onResponseHierarchyForTreeBrowser(LinkedList<ICatLocDetail> result, int instanceId) {
        if (this.instanceId == instanceId) {
            openedHierarchy = result;
            //nothing opened yet
            if (lastOpened.getValue() != null
                    && lastOpened.getValue().equals(result.getLast())) {
                //if lastOpen exuals with selected category, it means that its open
                //and it shoud be closed
                lastOpened = lastOpened.getParent();
                closeAllNodes(lastOpened);
            } else {
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

    /**************************************************************************/
    /* Getter methods                                                         */
    /**************************************************************************/
    /**
     * @return the instance id
     */
    @Override
    public int getInstanceId() {
        return instanceId;
    }

    /**
     * @return the CatLocSelectorBuilder
     */
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
        openedHierarchy = new LinkedList<ICatLocDetail>();
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
        Iterator<ICatLocDetail> iterator = openedHierarchy.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().getLevel() >= selectedCatLoc.getLevel()) {
                iterator.remove();
            }
        }
        //replace last level index with actual one - selected by user
        openedHierarchy.add(selectedCatLoc);
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
    private ICatLocDetail getSameLevelOldItem(LinkedList<ICatLocDetail> hierarchy, int level) {
        for (ICatLocDetail item : hierarchy) {
            if (item.getLevel() == level) {
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
    private void openNodes(LinkedList<ICatLocDetail> result) {
        if (result != null && !result.isEmpty()) {
            todoOpen = new LinkedList<ICatLocDetail>(result);
            cancelNextOpenEvent = true;
            lastOpened = lastOpened.setChildOpen(getIndex(todoOpen.removeFirst()), true);
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

    /**
     * Returns index of given item in list related to lastOpened item.
     * @param item
     * @return index value
     */
    private int getIndex(ICatLocDetail item) {
        for (int i = 0; i < lastOpened.getChildCount(); i++) {
            if (((ICatLocDetail) lastOpened.getChildValue(i)).getId() == item.getId()) {
                return i;
            }
        }
        return -1;
    }

}
