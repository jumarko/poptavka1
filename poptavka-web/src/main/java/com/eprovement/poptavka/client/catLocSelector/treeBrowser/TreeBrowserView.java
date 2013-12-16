package com.eprovement.poptavka.client.catLocSelector.treeBrowser;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocTreeViewModel;
import com.eprovement.poptavka.client.catLocSelector.treeBrowser.TreeBrowserPresenter.TreeBrowserInterface;
import com.eprovement.poptavka.client.common.validation.ProvidesValidate;
import com.eprovement.poptavka.client.root.ReverseCompositeView;
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
    @Override
    public void createView() {
        initWidget(uiBinder.createAndBindUi(this));
    }

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
    /* SETTERS                                                                */
    /**************************************************************************/
    /**************************************************************************/
    /* GETTERS                                                                */
    /**************************************************************************/
    /** CellTree. **/
    @Override
    public CellTree getCellTree() {
        return cellTree;
    }

    @Override
    public SingleSelectionModel<ICatLocDetail> getTreeSelectionModel() {
        return treeSelectionModel;
    }

    @Override
    public boolean isValid() {
        return !treeSelectionModel.getSelectedSet().isEmpty();
    }

    @Override
    public Widget getWidgetView() {
        return this;
    }
}
