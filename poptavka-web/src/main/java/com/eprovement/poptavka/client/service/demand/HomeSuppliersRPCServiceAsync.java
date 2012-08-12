/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Praso
 */
public interface HomeSuppliersRPCServiceAsync {

    void getSuppliers(SearchDefinition searchDefinition, AsyncCallback<List<FullSupplierDetail>> callback);

    void getSuppliersCount(SearchDefinition searchDefinition, AsyncCallback<Long> callback);

    void getCategories(AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryParents(Long category, AsyncCallback<ArrayList<CategoryDetail>> callback);

    void getCategoryChildren(Long category, AsyncCallback<ArrayList<CategoryDetail>> callback);
}
