package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

@RemoteServiceRelativePath("service/category")
public interface CategoryRPCService extends RemoteService {

    CategoryDetail getCategory(long id) throws CommonException;

    ArrayList<CategoryDetail> getCategories() throws CommonException;

    ArrayList<CategoryDetail> getCategoryParents(Long category) throws CommonException;

    ArrayList<CategoryDetail> getCategoryChildren(Long category) throws CommonException;

    List<CategoryDetail> getAllRootCategories() throws CommonException;
}
