package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.shared.domain.CategoryDetail;

public interface CategoryRPCServiceAsync {
    void getCategories(AsyncCallback<ArrayList<CategoryDetail>> callback);
}
