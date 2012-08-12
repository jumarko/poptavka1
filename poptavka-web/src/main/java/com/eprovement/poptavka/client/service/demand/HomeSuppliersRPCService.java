/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import com.eprovement.poptavka.shared.search.SearchDefinition;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath(HomeSuppliersRPCService.URL)
public interface HomeSuppliersRPCService extends RemoteService {

    String URL = "service/homesuppliers";

    long getSuppliersCount(SearchDefinition searchDefinition) throws RPCException;

    List<FullSupplierDetail> getSuppliers(SearchDefinition searchDefinition) throws RPCException;

    ArrayList<CategoryDetail> getCategoryParents(Long category) throws RPCException;

    ArrayList<CategoryDetail> getCategories() throws RPCException;

    ArrayList<CategoryDetail> getCategoryChildren(Long category) throws RPCException;
}
