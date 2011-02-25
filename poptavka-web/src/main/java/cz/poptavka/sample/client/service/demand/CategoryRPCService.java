package cz.poptavka.sample.client.service.demand;

import java.util.List;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

import cz.poptavka.sample.domain.demand.Category;

@RemoteServiceRelativePath("service/category")
public interface CategoryRPCService extends RemoteService {

    List<Category> getCategories();
}
