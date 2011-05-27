package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.shared.domain.CategoryDetail;

@RemoteServiceRelativePath("service/category")
public interface CategoryRPCService extends RemoteService {

    // TODO is it used?
//    CategoryDetail getCategory(String code);

    ArrayList<CategoryDetail> getCategories();

    ArrayList<CategoryDetail> getCategoryChildren(String category);

    CategoryDetail getCategory(long id);

    List<CategoryDetail> getAllRootCategories();
}
