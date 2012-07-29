package com.eprovement.poptavka.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.shared.domain.CategoryDetail;

public interface CategoryRPCServiceAsync {

    void getCategory(long id, AsyncCallback<CategoryDetail> callback);

    void getAllRootCategories(AsyncCallback<List<CategoryDetail>> callback);

    void getCategories(AsyncCallback<List<CategoryDetail>> callback);

    void getCategoryParents(Long category, AsyncCallback<List<CategoryDetail>> callback);

    void getCategoryChildren(Long category, AsyncCallback<List<CategoryDetail>> callback);
}
