package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.exceptions.ExceptionUtils;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;
import java.util.List;

public class CategoryDataProvider extends AsyncDataProvider<CategoryDetail> {

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
                }

                @Override
                public void onFailure(Throwable caught) {
                    // TODO ivlcek - change the alert message to user
                    new com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes.AlertBox(
                        ExceptionUtils.getFullErrorMessage(caught)).show();
                }
            });
        } else {
            categoryService.getCategoryChildren(categoryDetail.getId(),
                    new AsyncCallback<List<CategoryDetail>>() {
                        @Override
                        public void onSuccess(List<CategoryDetail> result) {
                            updateRowCount(result.size(), true);
                            updateRowData(0, result);
                        }

                        @Override
                        public void onFailure(Throwable caught) {
                            // TODO ivlcek - change the alert message to user
                            new com.eprovement.poptavka.shared.exceptions.SecurityDialogBoxes.AlertBox(
                                ExceptionUtils.getFullErrorMessage(caught)).show();
                        }
                    });
        }
    }
}