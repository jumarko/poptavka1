/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.eprovement.poptavka.client.main.common.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.common.OrderType;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import java.util.Map;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Praso
 */
public interface HomeSuppliersRPCServiceAsync {

    void getSuppliers(int start, int count, SearchModuleDataHolder supplier, Map<String, OrderType> orderColumns,
            AsyncCallback<List<FullSupplierDetail>> callback);

    void getSuppliersCount(SearchModuleDataHolder supplier, AsyncCallback<Long> callback);

    void getCategories(AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryParents(Long category, AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryChildren(Long category, AsyncCallback<ArrayList<CategoryDetail>> callback);
}
