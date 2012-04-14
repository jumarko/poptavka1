package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.exceptions.RPCException;

@RemoteServiceRelativePath("service/category")
public interface CategoryRPCService extends RemoteService {

    CategoryDetail getCategory(long id) throws RPCException;

    ArrayList<CategoryDetail> getCategories() throws RPCException;

    ArrayList<CategoryDetail> getCategoryParents(Long category) throws RPCException;

    ArrayList<CategoryDetail> getCategoryChildren(Long category) throws RPCException;

    List<CategoryDetail> getAllRootCategories() throws RPCException;
}
