package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes;
import com.google.gwt.core.client.GWT;
import com.google.gwt.i18n.client.LocalizableMessages;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent;
import com.google.gwt.user.cellview.client.LoadingStateChangeEvent.LoadingState;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import com.mvp4g.client.event.EventBusWithLookup;
import java.util.List;
import java.util.logging.Logger;

public class CategoryDataProvider extends AsyncDataProvider<CategoryDetail> {

    private static final Logger LOGGER = Logger.getLogger(CategoryDataProvider.class.getName());
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private CategoryRPCServiceAsync categoryService;
    private EventBusWithLookup eventBus;
    private CategoryDetail categoryDetail;
    private LoadingStateChangeEvent.Handler categoryLoadingHandler;

    public CategoryDataProvider(CategoryDetail locCode, CategoryRPCServiceAsync categoryService,
            EventBusWithLookup eventBus, LoadingStateChangeEvent.Handler rootHandler,
            LoadingStateChangeEvent.Handler categoryLoadingHandler) {
        this.categoryDetail = locCode;
        this.categoryService = categoryService;
        this.eventBus = eventBus;
        this.categoryLoadingHandler = categoryLoadingHandler;
    }

    @Override
    protected void onRangeChanged(HasData<CategoryDetail> display) {
        if (categoryDetail == null) {
            categoryService.getCategories(new AsyncCallback<List<CategoryDetail>>() {
                @Override
                public void onSuccess(List<CategoryDetail> result) {
                    updateRowCount(result.size(), true);
                    updateRowData(0, result);
                    //Not every widget want to register listener
                    if (categoryLoadingHandler != null) {
                        categoryLoadingHandler.onLoadingStateChanged(new LoadingStateChangeEvent(LoadingState.LOADED));
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    LOGGER.severe("CategoryDataProvider not working, caught=" + caught.getMessage());
                    SecurityDialogBoxes.showAlertBox(eventBus, MSGS.errorTipTryWaiting());
                }
            });
        } else {
            categoryService.getCategoryChildren(categoryDetail.getId(),
                    new AsyncCallback<List<CategoryDetail>>() {
                        @Override
                        public void onSuccess(List<CategoryDetail> result) {
                            updateRowCount(result.size(), true);
                            updateRowData(0, result);
                            //Not every widget want to register listener
                            if (categoryLoadingHandler != null) {
                                categoryLoadingHandler.onLoadingStateChanged(
                                        new LoadingStateChangeEvent(LoadingState.LOADED));
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            LOGGER.severe("CategoryDataProvider not working, caught=" + caught.getMessage());
                            SecurityDialogBoxes.showAlertBox(eventBus, MSGS.errorTipTryWaiting());
                        }
                    });
        }
    }
}