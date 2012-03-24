/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Praso
 */
public interface HomeSuppliersRPCServiceAsync {

    void filterSuppliers(int start, int count, SearchModuleDataHolder supplier, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullSupplierDetail>> callback);

    void filterSuppliersCount(SearchModuleDataHolder supplier, AsyncCallback<Long> callback);

    void getCategories(AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryParents(Long category, AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryChildren(Long category, AsyncCallback<ArrayList<CategoryDetail>> callback);
}
