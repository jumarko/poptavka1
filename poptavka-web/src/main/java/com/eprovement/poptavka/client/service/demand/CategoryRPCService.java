package com.eprovement.poptavka.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;

@RemoteServiceRelativePath(CategoryRPCService.URL)
public interface CategoryRPCService extends RemoteService {

    String URL = "service/category";

    CategoryDetail getCategory(long id) throws RPCException;

    ArrayList<CategoryDetail> getCategories() throws RPCException;

    ArrayList<CategoryDetail> getCategoryParents(Long category) throws RPCException;

    ArrayList<CategoryDetail> getCategoryChildren(Long category) throws RPCException;

    List<CategoryDetail> getAllRootCategories() throws RPCException;
}
