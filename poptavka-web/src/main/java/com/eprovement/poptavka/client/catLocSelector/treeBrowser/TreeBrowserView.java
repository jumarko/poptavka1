/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.treeBrowser;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocTreeViewModel;
import com.eprovement.poptavka.client.catLocSelector.treeBrowser.TreeBrowserPresenter.TreeBrowserInterface;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.common.ReverseCompositeView;
import com.eprovement.poptavka.resources.celltree.CustomCellTree;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocDetail;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.user.cellview.client.CellTree;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.SingleSelectionModel;

/**
 * TreeBrowser view consists of CellTree for selecting categories in tree hierarchy.
 * @author Martin Slavkovsky
 */
public class TreeBrowserView extends ReverseCompositeView<PresentersInterface>
        implements ProvidesValidate, TreeBrowserInterface {

    /**************************************************************************/
    /* UIBINDER                                                               */
    /**************************************************************************/
    private static TreeBrowserUiBinder uiBinder = GWT.create(TreeBrowserUiBinder.class);

    interface TreeBrowserUiBinder extends UiBinder<Widget, TreeBrowserView> {
    }

    /**************************************************************************/
    /* ATTRIBUTES                                                              */
    /**************************************************************************/
    /** UiBinder attributes. **/
    @UiField ScrollPanel scrollPanel;
    /** Class attributes. **/
    // Using category detail key provider in selection model, allow us to have
    // displayed alwas only one node. The other are automaticaly closed.
    private CellTree cellTree;
    private SingleSelectionModel<ICatLocDetail> treeSelectionModel;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates TreeBrowser view's compontents
     */
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

    /**
     * Creates treeBrowser.
     */
    @Override
    public void createTreeBrowser() {
        // tree nodes
        final CellTree.Resources resource = GWT.create(CustomCellTree.class);
        treeSelectionModel = new SingleSelectionModel<ICatLocDetail>(CatLocDetail.KEY_PROVIDER);
        final CatLocTreeViewModel treeViewModule = new CatLocTreeViewModel(treeSelectionModel, presenter);

        cellTree = new CellTree(treeViewModule, null, resource);
        cellTree.setAnimationEnabled(true);
        scrollPanel.setWidget(cellTree);
    }

    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** CellTree. **/
    /**
     * @return the cellTree
     */
    @Override
    public CellTree getCellTree() {
        return cellTree;
    }

    /**
     * @return the treeBrowser selection model
     */
    @Override
    public SingleSelectionModel<ICatLocDetail> getTreeSelectionModel() {
        return treeSelectionModel;
    }

    /**
     * Validate view's compontents.
     * @return true if valid, false otherwise
     */
    @Override
    public boolean isValid() {
        return !treeSelectionModel.getSelectedSet().isEmpty();
    }

    /**
     * @return the widget view
     */
    @Override
    public Widget getWidgetView() {
        return this;
    }
}
