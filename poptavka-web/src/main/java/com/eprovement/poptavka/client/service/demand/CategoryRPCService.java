package com.eprovement.poptavka.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@RemoteServiceRelativePath(CategoryRPCService.URL)
public interface CategoryRPCService extends RemoteService {

    String URL = "service/category";

    CategoryDetail getCategory(long id) throws RPCException;

    List<CategoryDetail> getCategories() throws RPCException;

    List<CategoryDetail> getCategoryParents(Long category) throws RPCException;

    List<CategoryDetail> getCategoryChildren(Long category) throws RPCException;

    List<CategoryDetail> getAllRootCategories() throws RPCException;
}
