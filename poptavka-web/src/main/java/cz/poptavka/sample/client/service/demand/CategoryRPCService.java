package cz.poptavka.sample.client.service.demand;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.shared.domain.CategoryDetail;

@RemoteServiceRelativePath("service/category")
public interface CategoryRPCService extends RemoteService {

    Category getCategory(String code);

    ArrayList<CategoryDetail> getCategories();

    ArrayList<CategoryDetail> getCategoryChildren(String category);

    Category getCategory(long id);

    List<Category> getAllRootCategories();
}
