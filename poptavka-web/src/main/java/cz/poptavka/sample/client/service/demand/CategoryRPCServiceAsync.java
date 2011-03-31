package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.shared.domain.CategoryDetail;

public interface CategoryRPCServiceAsync {
    void getCategory(String code, AsyncCallback<Category> callback);

    void getCategories(AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryChildren(String category,
            AsyncCallback<ArrayList<CategoryDetail>> callback);
}
