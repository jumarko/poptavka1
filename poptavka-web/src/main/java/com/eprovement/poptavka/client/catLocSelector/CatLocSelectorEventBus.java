/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector;

import com.eprovement.poptavka.client.catLocSelector.cellBrowser.CellBrowserPresenter;
import com.eprovement.poptavka.client.catLocSelector.manager.ManagerPresenter;
import com.eprovement.poptavka.client.catLocSelector.others.CatLocSelectorBuilder;
import com.eprovement.poptavka.client.catLocSelector.treeBrowser.TreeBrowserPresenter;
import com.eprovement.poptavka.client.root.gateways.InfoWidgetsGateway;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.user.client.ui.SimplePanel;
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
    void initCatLocSelector(SimplePanel embedToWidget, CatLocSelectorBuilder builder);

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
    void responseHierarchy(LinkedList<ICatLocDetail> result, int instanceId);

    /**************************************************************************/
    /* Business events handled by CatLocManagerPresenter                    */
    /**************************************************************************/
    @Event(generate = ManagerPresenter.class)
    void initNewCatLocSelectorManager(SimplePanel embedToWidget, CatLocSelectorBuilder builder);

    @Event(handlers = ManagerPresenter.class)
    void initSameCatLocSelectorManager(SimplePanel embedToWidget, CatLocSelectorBuilder builder);

    @Event(handlers = ManagerPresenter.class)
    void fillCatLocsFromManager(List<ICatLocDetail> selectedCatLocs, int instanceId);

    @Event(handlers = ManagerPresenter.class)
    void responseHierarchyForManager(LinkedList<ICatLocDetail> result, int instanceId);

    @Event(handlers = ManagerPresenter.class)
    void setCatLocs(List<ICatLocDetail> catLocs, int instanceId);

    @Event(handlers = ManagerPresenter.class)
    void addCatLocs(List<ICatLocDetail> catLocs, int instanceId);

    @Event(handlers = ManagerPresenter.class)
    void redrawCatLocSelectorGrid(int instanceId);

    /**************************************************************************/
    /* Business events handled by TreeBrowserPresenter                        */
    /**************************************************************************/
    @Event(generate = TreeBrowserPresenter.class)
    void initNewCatLocSelectorTreeBrowser(SimplePanel embedToWidget, CatLocSelectorBuilder builder);

    @Event(handlers = TreeBrowserPresenter.class)
    void initSameCatLocSelectorTreeBrowser(SimplePanel embedToWidget, CatLocSelectorBuilder builder);

    @Event(handlers = TreeBrowserPresenter.class)
    void fillCatLocsFromTreeBrowser(List<ICatLocDetail> selectedCatLocs, int instanceId);

    @Event(handlers = TreeBrowserPresenter.class)
    void responseHierarchyForTreeBrowser(LinkedList<ICatLocDetail> result, int instanceId);

    /**************************************************************************/
    /* Business events handled by CellBrowserPresenter                        */
    /**************************************************************************/
    @Event(generate = CellBrowserPresenter.class)
    void initNewCatLocSelectorCellBrowser(SimplePanel embedToWidget, CatLocSelectorBuilder builder);

    @Event(handlers = CellBrowserPresenter.class)
    void initSameCatLocSelectorCellBrowser(SimplePanel embedToWidget, CatLocSelectorBuilder builder);

    @Event(handlers = CellBrowserPresenter.class)
    void fillCatLocsFromCellBrowser(List<ICatLocDetail> selectedCatLocs, int instanceId);

    @Event(handlers = CellBrowserPresenter.class)
    void responseHierarchyForCellBrowser(LinkedList<ICatLocDetail> result, int instanceId);
}
