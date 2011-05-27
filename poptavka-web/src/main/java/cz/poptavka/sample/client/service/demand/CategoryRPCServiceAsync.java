package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.CategoryDetail;

public interface CategoryRPCServiceAsync {

//    void getCategory(String code, AsyncCallback<CategoryDetail> callback);

    void getCategory(long id, AsyncCallback<CategoryDetail> callback);

    void getAllRootCategories(AsyncCallback<List<CategoryDetail>> callback);

    void getCategories(AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryChildren(String category,
            AsyncCallback<ArrayList<CategoryDetail>> callback);
}
