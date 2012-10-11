package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.client.common.session.Storage;
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
import java.util.List;
import java.util.logging.Logger;

public class CategoryDataProvider extends AsyncDataProvider<CategoryDetail> {

    private static final Logger LOGGER = Logger.getLogger(CategoryDataProvider.class.getName());
    private static final LocalizableMessages MSGS = GWT.create(LocalizableMessages.class);
    private CategoryRPCServiceAsync categoryService;
    private CategoryDetail categoryDetail;

    public CategoryDataProvider(CategoryDetail locCode, CategoryRPCServiceAsync categoryService) {
        this.categoryDetail = locCode;
        this.categoryService = categoryService;
    }

    @Override
    protected void onRangeChanged(HasData<CategoryDetail> display) {
        if (categoryDetail == null) {
            categoryService.getCategories(new AsyncCallback<List<CategoryDetail>>() {
                @Override
                public void onSuccess(List<CategoryDetail> result) {
                    updateRowCount(result.size(), true);
                    updateRowData(0, result);
                    if (Storage.getTree() != null) {
                        Storage.getTree().fireEvent(new LoadingStateChangeEvent(LoadingState.LOADED));
                    }
                }

                @Override
                public void onFailure(Throwable caught) {
                    LOGGER.severe("CategoryDataProvider not working, caught=" + caught.getMessage());
                    new SecurityDialogBoxes.AlertBox(MSGS.tryWaiting()).show();
                }
            });
        } else {
            categoryService.getCategoryChildren(categoryDetail.getId(),
                    new AsyncCallback<List<CategoryDetail>>() {
                        @Override
                        public void onSuccess(List<CategoryDetail> result) {
                            updateRowCount(result.size(), true);
                            updateRowData(0, result);
                            if (Storage.getTree() != null) {
                                Storage.getTree().fireEvent(new LoadingStateChangeEvent(LoadingState.LOADED));
                            }
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            LOGGER.severe("CategoryDataProvider not working, caught=" + caught.getMessage());
                            new SecurityDialogBoxes.AlertBox(MSGS.tryWaiting()).show();
                        }
                    });
        }
    }
}