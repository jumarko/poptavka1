/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.client.service.demand;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import cz.poptavka.sample.client.main.common.search.SearchModuleDataHolder;
import cz.poptavka.sample.domain.common.OrderType;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.domain.supplier.FullSupplierDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Praso
 */
@RemoteServiceRelativePath("service/homesuppliers")
public interface HomeSuppliersRPCService extends RemoteService {

    long filterSuppliersCount(SearchModuleDataHolder supplier) throws RPCException;

    List<FullSupplierDetail> filterSuppliers(int start, int count,
            SearchModuleDataHolder supplier, Map<String, OrderType> orderColumns) throws RPCException;

    ArrayList<CategoryDetail> getCategoryParents(Long category) throws RPCException;

    ArrayList<CategoryDetail> getCategories() throws RPCException;

    ArrayList<CategoryDetail> getCategoryChildren(Long category) throws RPCException;
}
