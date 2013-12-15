/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector;

import com.eprovement.poptavka.client.catLocSelector.cellBrowser.CellBrowserPresenter;
import com.eprovement.poptavka.client.catLocSelector.manager.ManagerPresenter;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.catLocSelector.treeBrowser.TreeBrowserPresenter;
import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.shared.selectors.catLocSelector.CatLocTreeItem;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.view.client.SelectionChangeEvent;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.event.EventBusWithLookup;
import com.mvp4g.client.presenter.NoStartPresenter;
import java.util.LinkedList;
import java.util.List;

/**
 * EventBus for <b>CatLoc selector module</b>.
 * Module can be initialized in tree ways:
 * <ul>
 *   <li>CellBrowser - uses cellBrowser to browse and select items.</li>
 *   <li>TreeBrowser - uses cellTree to browse and select items in a tree.</li>
 *   <li>Manager - manages selected items in a table</li>
 * </ul>
 *
 * @author Martin Slavkovsky
 */
@Events(startPresenter = NoStartPresenter.class, module = CatLocSelectorModule.class)
public interface CatLocSelectorEventBus extends EventBusWithLookup, InfoWidgetsGateway {

    /**************************************************************************/
    /* Business events handled by CatLocSelectorPresenter                   */
    /**************************************************************************/
    /**
     * Init catLoc selector module.
     * According to given CatLocSelectorBuilder init either
     * CatLocManager, CatLocCellBrowser or CatLocTreeBrowser.
     */
    @Event(handlers = CatLocSelectorInstanceManager.class)
    void initCatLocSelector(SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId);

    /**
     * Append given list with selected catLocs from CatLoc Selector widget.
     * If given list is null, initialize it first.
     * Only active presenter response to this call.
     */
    @Event(handlers = CatLocSelectorInstanceManager.class)
    void fillCatLocs(List<ICatLocDetail> selectedCatLocs, int instanceId);

    @Event(handlers = CatLocSelectorHandler.class)
    void requestHierarchy(int selectorType, ICatLocDetail catLoc, int instanceId);

    @Event(handlers = CatLocSelectorInstanceManager.class)
    void responseHierarchy(LinkedList<CatLocTreeItem> result, int instanceId);

    /**************************************************************************/
    /* Business events handled by CatLocManagerPresenter                    */
    /**************************************************************************/
    @Event(generate = ManagerPresenter.class)
    void initNewCatLocSelectorManager(SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId);

    @Event(handlers = ManagerPresenter.class)
    void initSameCatLocSelectorManager(SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId);

    @Event(handlers = ManagerPresenter.class)
    void fillCatLocsFromManager(List<ICatLocDetail> selectedCatLocs, int instanceId);

    @Event(handlers = ManagerPresenter.class)
    void responseHierarchyForManager(LinkedList<CatLocTreeItem> result, int instanceId);

    @Event(handlers = ManagerPresenter.class)
    void setCatLocs(List<ICatLocDetail> catLocs);

    @Event(handlers = ManagerPresenter.class)
    void addCatLocs(List<ICatLocDetail> catLocs);

    /**************************************************************************/
    /* Business events handled by TreeBrowserPresenter                        */
    /**************************************************************************/
    @Event(generate = TreeBrowserPresenter.class)
    void initNewCatLocSelectorTreeBrowser(SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId);

    @Event(handlers = TreeBrowserPresenter.class)
    void initSameCatLocSelectorTreeBrowser(SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId);

    @Event(handlers = TreeBrowserPresenter.class)
    void fillCatLocsFromTreeBrowser(List<ICatLocDetail> selectedCatLocs, int instanceId);

    @Event(handlers = TreeBrowserPresenter.class)
    void responseHierarchyForTreeBrowser(LinkedList<CatLocTreeItem> result, int instanceId);

    /**
     * Register selection model to cellTree in TreeBrowser widget.
     * Since TreeBrowser holds functionality to get data, open, close, select items over cellTree,
     * therefor if another "outside" widget wants to act or to have access to selected items,
     * it must implement its selection model (which holds wanted functionality) and register here.
     * To have access to selected items selectino model must call also
     * @see fillCatLocs(List<CatLocDetail> selectedCatLocs)
     */
    @Event(handlers = TreeBrowserPresenter.class)
    void registerCatLocTreeSelectionHandler(SelectionChangeEvent.Handler selectionHandler);

    /**************************************************************************/
    /* Business events handled by CellBrowserPresenter                        */
    /**************************************************************************/
    @Event(generate = CellBrowserPresenter.class)
    void initNewCatLocSelectorCellBrowser(SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId);

    @Event(handlers = CellBrowserPresenter.class)
    void initSameCatLocSelectorCellBrowser(SimplePanel embedToWidget, CatLocSelectorBuilder builder, int instanceId);

    @Event(handlers = CellBrowserPresenter.class)
    void fillCatLocsFromCellBrowser(List<ICatLocDetail> selectedCatLocs, int instanceId);

    @Event(handlers = CellBrowserPresenter.class)
    void responseHierarchyForCellBrowser(LinkedList<CatLocTreeItem> result, int instanceId);
}
