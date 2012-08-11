package com.eprovement.poptavka.client.common.category;

import com.eprovement.poptavka.client.common.security.SecuredAsyncCallback;
import com.eprovement.poptavka.client.service.demand.CategoryRPCServiceAsync;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
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
            categoryService.getCategories(new SecuredAsyncCallback<List<CategoryDetail>>() {
                @Override
                public void onSuccess(List<CategoryDetail> result) {
                    updateRowCount(result.size(), true);
                    updateRowData(0, result);
                }
            });
        } else {
            categoryService.getCategoryChildren(categoryDetail.getId(),
                    new SecuredAsyncCallback<List<CategoryDetail>>() {
                        @Override
                        public void onSuccess(List<CategoryDetail> result) {
                            updateRowCount(result.size(), true);
                            updateRowData(0, result);
                        }
                    });
        }
    }
}