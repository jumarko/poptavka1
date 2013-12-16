/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.others;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.view.client.DefaultSelectionEventManager;
import com.google.gwt.view.client.SetSelectionModel;
import com.google.gwt.view.client.TreeViewModel;

/**
 * The {@link TreeViewModel} used to organize contacts into a hierarchy.
 * @author Martin Slavkovsky
 */
public class CatLocTreeViewModel implements TreeViewModel {

    /**************************************************************************/
    /* ATTRIBUTES                                                             */
    /**************************************************************************/
    private final CatLocCell categoryCell;
    private final SetSelectionModel<ICatLocDetail> selectionModel;
    private final DefaultSelectionEventManager<ICatLocDetail> selectionManager;
    private final PresentersInterface categoryPresenter;

    /**************************************************************************/
    /* INITIALIZATION                                                         */
    /**************************************************************************/
    /**
     * Creates CatLocTreeModel.
     * @param selectionModel - selection model
     * @param presenter - presenter
     */
    public CatLocTreeViewModel(
        SetSelectionModel<ICatLocDetail> selectionModel,
        PresentersInterface presenter) {

        this.selectionModel = selectionModel;
        this.categoryPresenter = presenter;
        this.selectionManager = DefaultSelectionEventManager.createCheckboxManager();
        this.categoryCell = new CatLocCell(presenter, selectionModel);
    }

    /**************************************************************************/
    /* HODE, LEAF definitions                                                 */
    /**************************************************************************/
    /**
     * Then inside getNodeInfo(T value) of your TreeViewModel just return a new
     * DeafultNodeInfo with a new MyDataProvider. It defines type what cells to use
     * in each level. In this way NodeInfo is returned synchronously, but the data provider
     * updates itself asynchronously.
     * @param <T>
     * @param value
     * @return
     */
    @Override
    public <T> NodeInfo<?> getNodeInfo(T value) {
        CatLocDataProvider dataProvider = new CatLocDataProvider((ICatLocDetail) value, categoryPresenter);

        return new DefaultNodeInfo(dataProvider, categoryCell, selectionModel, selectionManager, null);
    }

    /**
     * Check if item is leaf.
     * @param value - item
     * @return true if leaf, false otherwise (node)
     */
    @Override
    public boolean isLeaf(Object value) {
        if (value == null) {
            return false;
        } else {
            return ((ICatLocDetail) value).isLeaf();
        }
    }
}