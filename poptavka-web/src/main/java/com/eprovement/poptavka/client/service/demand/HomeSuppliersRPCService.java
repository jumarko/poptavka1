/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.selectors.catLocSelector.ICatLocDetail;
import com.eprovement.poptavka.shared.domain.supplier.LesserSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.List;

/**
 *
 * @author Ivan Vlcek
 */
@RemoteServiceRelativePath(HomeSuppliersRPCService.URL)
public interface HomeSuppliersRPCService extends RemoteService {

    String URL = "service/homesuppliers";

    ICatLocDetail getCategory(long categoryID) throws RPCException;

    LesserSupplierDetail getSupplier(long supplierID) throws RPCException;

    List<LesserSupplierDetail> getSuppliers(SearchDefinition searchDefinition) throws RPCException;

    Integer getSuppliersCount(SearchDefinition searchDefinition) throws RPCException;

}
