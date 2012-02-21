package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.CategoryDetail;

@RemoteServiceRelativePath("service/category")
public interface CategoryRPCService extends RemoteService {

    CategoryDetail getCategory(long id);

    ArrayList<CategoryDetail> getCategories();

    ArrayList<CategoryDetail> getCategoryParents(Long category);

    ArrayList<CategoryDetail> getCategoryChildren(Long category);

    List<CategoryDetail> getAllRootCategories();
}
