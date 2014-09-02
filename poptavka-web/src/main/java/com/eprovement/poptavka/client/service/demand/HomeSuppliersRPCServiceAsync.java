/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.LesserSupplierDetail;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.AsyncCallback;
import java.util.List;

/**
 *
 * @author Praso
 */
public interface HomeSuppliersRPCServiceAsync {

    void getCategory(long categoryID, AsyncCallback<ICatLocDetail> callback);

    void getSupplier(long supplierID, AsyncCallback<LesserSupplierDetail> callback);

    void getSuppliers(SearchDefinition searchDefinition, AsyncCallback<List<LesserSupplierDetail>> callback);

    void getSuppliersCount(SearchDefinition searchDefinition, AsyncCallback<Integer> callback);

}
