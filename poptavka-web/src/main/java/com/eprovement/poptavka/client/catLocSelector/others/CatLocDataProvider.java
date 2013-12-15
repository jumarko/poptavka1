/*
 * Copyright (C), eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.client.catLocSelector.others;

import com.eprovement.poptavka.client.catLocSelector.CatLocSelectorInstanceManager.PresentersInterface;
import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import java.util.List;
import java.util.logging.Logger;

/**
 * CatLocData provider retrieves items asynchoronously.
 * In order to have asynchronous data retrieving for CellBrowser and CellTree,
 * asynchronous data provider must be created.
 *
 * @author Martin Slavkovsky
 */
public class CatLocDataProvider extends AsyncDataProvider<ICatLocDetail> {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private static final Logger LOGGER = Logger.getLogger(CatLocDataProvider.class.getName());
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private PresentersInterface presenter;
    private ICatLocDetail detail;

    /**************************************************************************/
    /* Initialiation                                                          */
    /**************************************************************************/
    /**
     * Creates CatLocDataProvider
     * @param catLocDetail - parent object
     * @param categoryPresenter the PresentersInterface
     */
    public CatLocDataProvider(ICatLocDetail catLocDetail, PresentersInterface categoryPresenter) {
        this.detail = catLocDetail;
        this.presenter = categoryPresenter;
    }

    /**************************************************************************/
    /* Business events                                                        */
    /**************************************************************************/
    /**
     * When range changes, new data are recquired.
     * Retrieves data asynchronously.
     * If parent item provided, retrieves its child items.
     * If no parent item provided, retrieves root items.
     */
    @Override
    protected void onRangeChanged(HasData<ICatLocDetail> display) {
        if (detail == null) {
            presenter.getService().getRootItems(presenter.getBuilder().getSelectorType(),
                    new AsyncCallback<List<ICatLocDetail>>() {
                    @Override
                    public void onSuccess(List<ICatLocDetail> result) {
                        updateRowCount(result.size(), true);
                        updateRowData(0, result);
                        //Not every widget want to register listener
                        if (presenter.getLoadingHandler() != null) {
                            presenter.getLoadingHandler().onLoadingStateChanged(
                                    new LoadingStateChangeEvent(LoadingState.LOADED));
                        }
                    }

                    @Override
                    public void onFailure(Throwable caught) {
                        LOGGER.severe("CategoryDataProvider not working, caught=" + caught.getMessage());
                        presenter.getEventBus().showAlertPopup(MSGS.errorTipTryWaiting());
                    }
                });
        } else {
            presenter.getService().getItemChildren(presenter.getBuilder().getSelectorType(), detail.getId(),
                    new AsyncCallback<List<ICatLocDetail>>() {
                        @Override
                        public void onSuccess(List<ICatLocDetail> result) {
                            updateRowCount(result.size(), true);
                            updateRowData(0, result);
                            //Not every widget want to register listener
                            if (presenter.getLoadingHandler() != null) {
                                presenter.getLoadingHandler().onLoadingStateChanged(
                                        new LoadingStateChangeEvent(LoadingState.LOADED));
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            LOGGER.severe("CategoryDataProvider not working, caught=" + caught.getMessage());
                            presenter.getEventBus().showAlertPopup(MSGS.errorTipTryWaiting());
                        }
                    });
        }
    }
}