/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.cellBrowser;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorEventBus;
import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.common.session.Storage;
import com.eprovement.poptavka.client.service.demand.CatLocSelectorRPCServiceAsync;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocTreeItem;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.cellview.client.CellBrowser;
import com.google.gwt.user.cellview.client.CellList;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.TreeNode;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.view.client.ListDataProvider;
import com.google.gwt.view.client.MultiSelectionModel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.google.gwt.view.client.SingleSelectionModel;
import com.google.inject.Inject;
import com.mvp4g.client.annotation.Presenter;
import com.mvp4g.client.presenter.LazyPresenter;
import com.mvp4g.client.view.LazyView;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Cell browser presenter. Use GWT cell browser to browse and pick up categories.
 * @author Martin Slavkovsky
 */
@Presenter(view = CellBrowserView.class, multiple = true)
public class CellBrowserPresenter
        extends LazyPresenter<CellBrowserPresenter.CellBrowserInterface, CatLocSelectorEventBus>
        implements PresentersInterface {

    /**************************************************************************/
    /* View interface                                                         */
    /**************************************************************************/
    public interface CellBrowserInterface extends LazyView {

        void createCellBrowser();

        void setSelectedCountLabel(int count, int countRestriction);

        CellBrowser getCellBrowser();

        CellList getCellList();

        ListDataProvider<ICatLocDetail> getCellListDataProvider();

        MultiSelectionModel<ICatLocDetail> getCellBrowserSelectionModel();

        SingleSelectionModel<ICatLocDetail> getCellListSelectionModel();

        boolean isValid();

        Widget getWidgetView();
    }

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private int instanceId;
    private int registerRestriction;
    private LinkedList<CatLocTreeItem> todoOpen;
    private TreeNode lastOpened;
    private CatLocSelectorBuilder builder;
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
    /* Bind                                                                   */
    /**************************************************************************/
    /**
     * Binds selection handler for cell list.
     */
    @Override
    public void bindView() {
        view.getCellListSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                view.getCellBrowserSelectionModel().setSelected(
                        view.getCellListSelectionModel().getSelectedObject(),
                        false);
                view.getCellListDataProvider().getList().remove(
                        view.getCellListSelectionModel().getSelectedObject());
            }
        });
    }

    /**
     * Binds cell browser selection handler.
     * Cannot be defined in <i>bindView</i> because cell browser is created later.
     * Therefore register handler after cell browser is created.
     */
    private void bindCellBrowserHanlders() {
        view.getCellBrowserSelectionModel().addSelectionChangeHandler(new SelectionChangeEvent.Handler() {
            @Override
            public void onSelectionChange(SelectionChangeEvent event) {
                List<ICatLocDetail> selectedList = new ArrayList<ICatLocDetail>(
                        view.getCellBrowserSelectionModel().getSelectedSet());
                if (builder.getSelectionRestriction() != -1
                        && (view.getCellBrowserSelectionModel().getSelectedSet().size()
                        > registerRestriction)) {
                    Window.alert(Storage.MSGS.commonCategorySelectionRestriction());
                    view.getCellBrowserSelectionModel().setSelected(getSelectedObjectOverAllowedMax(), false);
                } else {
                    view.getCellListDataProvider().setList(selectedList);
                    view.getCellList().setVisible(true);
                    if (builder.getSelectionRestriction() != -1) {
                        view.setSelectedCountLabel(selectedList.size(), registerRestriction);
                    }
                }
            }
        });
    }

    /**************************************************************************/
    /* Initialization                                                         */
    /**************************************************************************/
    /**
     * Call when new intance of CellBrowser is requested.
     */
    public void onInitNewCatLocSelectorCellBrowser(
            SimplePanel embedWidget, CatLocSelectorBuilder builder, int instanceId) {
        this.builder = builder;
        this.instanceId = instanceId;
        this.registerRestriction = builder.getSelectionRestriction();
        initCatLocSelectorCellBrowser(embedWidget);
    }

    /**
     * Call when same intance of CellBrowser is requested.
     */
    public void onInitSameCatLocSelectorCellBrowser(
            SimplePanel embedWidget, CatLocSelectorBuilder builder, int instanceId) {
        if (this.instanceId == instanceId) {
            initCatLocSelectorCellBrowser(embedWidget);
        }
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * If editing category, reopen selected category's hierarchy in cell browser
     * for better user experience.
     * @param categoryHierarchy category's hierarchy to be opened
     */
    public void onResponseHierarchyForCellBrowser(LinkedList<CatLocTreeItem> categoryHierarchy, int instanceId) {
        if (this.instanceId == instanceId) {
            view.getCellBrowserSelectionModel().setSelected(categoryHierarchy.getLast().getCatLoc(), true);
            todoOpen = new LinkedList<CatLocTreeItem>(categoryHierarchy);
        }
    }

    /**
     * Append given list with selected items.
     * If given list is null, initialize it first.
     * Only active presenter response to this call.
     */
    public void onFillCatLocsFromCellBrowser(List<ICatLocDetail> selectedCatLocs, int instanceId) {
        if (this.instanceId == instanceId) {
            if (selectedCatLocs == null) {
                selectedCatLocs = new ArrayList<ICatLocDetail>();
            }
            selectedCatLocs.addAll(view.getCellListDataProvider().getList());
        }
    }

    /**************************************************************************/
    /* Helper methods                                                         */
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
                if (todoOpen != null && !todoOpen.isEmpty()) {
                    lastOpened = lastOpened.setChildOpen(todoOpen.removeFirst().getIndex(), true);
                }
            }
        };
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
     * @return the CatLocSelectionBuilder
     */
    @Override
    public CatLocSelectorBuilder getBuilder() {
        return builder;
    }

    /**************************************************************************/
    /* Helper methods                                                         */
    /**************************************************************************/
    /**
     * Init category selector CellBrowser.
     * Build CellBrowser according to given CatLocSelectorBuilder and
     * set CellBrowser widget to given holder.
     *
     * @param embedWidget - CellBrowser's holder
     * @param builder - defines CellBrowser attributes
     * @param instanceId - distinguish individual instances
     */
    private void initCatLocSelectorCellBrowser(SimplePanel embedWidget) {
        //reset in case of calling second time
        view.createCellBrowser();
        this.bindCellBrowserHanlders();
        view.getCellListDataProvider().getList().clear();
        view.setSelectedCountLabel(0, registerRestriction);
        lastOpened = view.getCellBrowser().getRootTreeNode();
        todoOpen = null;
        embedWidget.setWidget(view.getWidgetView());
    }

    /**
     * Get selected object exceeding allowed registration items ccount.
     * The object must be unselect from cell browser.
     * @return the object
     */
    private ICatLocDetail getSelectedObjectOverAllowedMax() {
        for (ICatLocDetail selectedCatLoc : view.getCellBrowserSelectionModel().getSelectedSet()) {
            if (!view.getCellListDataProvider().getList().contains(selectedCatLoc)) {
                return selectedCatLoc;
            }
        }
        return null;
    }
}
