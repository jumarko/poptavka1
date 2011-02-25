package cz.poptavka.sample.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.AsyncCallback;

import cz.poptavka.sample.domain.demand.Category;

public interface CategoryRPCServiceAsync {
    void getCategories(AsyncCallback<List<Category>> callback);
}
