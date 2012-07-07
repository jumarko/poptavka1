/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.eprovement.poptavka.shared.search.SearchModuleDataHolder;
import com.eprovement.poptavka.domain.enums.OrderType;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.domain.supplier.FullSupplierDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath(HomeSuppliersRPCService.URL)
public interface HomeSuppliersRPCService extends RemoteService {

    String URL = "service/homesuppliers";

    long getSuppliersCount(SearchModuleDataHolder supplier) throws RPCException;

    List<FullSupplierDetail> getSuppliers(int start, int count,
            SearchModuleDataHolder supplier, Map<String, OrderType> orderColumns) throws RPCException;

    ArrayList<CategoryDetail> getCategoryParents(Long category) throws RPCException;

    ArrayList<CategoryDetail> getCategories() throws RPCException;

    ArrayList<CategoryDetail> getCategoryChildren(Long category) throws RPCException;
}
