package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import com.eprovement.poptavka.shared.domain.CategoryDetail;

public interface CategoryRPCServiceAsync {

    void getCategory(long id, AsyncCallback<CategoryDetail> callback);

    void getAllRootCategories(AsyncCallback<List<CategoryDetail>> callback);

    void getCategories(AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryParents(Long category, AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryChildren(Long category, AsyncCallback<ArrayList<CategoryDetail>> callback);
}
