package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.CategoryDetail;

@RemoteServiceRelativePath("service/category")
public interface CategoryRPCService extends RemoteService {

    CategoryDetail getCategory(long id);

//    CategoryDetail getCategory(String code);

    ArrayList<CategoryDetail> getCategories();

    ArrayList<CategoryDetail> getCategoryChildren(Long category);

    List<CategoryDetail> getAllRootCategories();
}
