package cz.poptavka.sample.server.service.category;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import cz.poptavka.sample.client.service.demand.CategoryRPCService;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.common.TreeItemService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.shared.domain.CategoryDetail;

public class CategoryRPCServiceImpl extends AutoinjectingRemoteService
        implements CategoryRPCService {

    private CategoryService categoryService;
    private TreeItemService treeItemService;
    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryRPCServiceImpl.class);

    @Override
    public ArrayList<CategoryDetail> getCategories() {
        List<Category> categories = categoryService.getRootCategories();
        return createCategoryDetailList(categories);
    }

    @Autowired
    @Required
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    @Required
    public void setTreeItemService(TreeItemService treeItemService) {
        this.treeItemService = treeItemService;
    }

    private ArrayList<CategoryDetail> createCategoryDetailList(List<Category> categories) {
        ArrayList<CategoryDetail> categoryDetails = new ArrayList<CategoryDetail>();

        for (Category cat : categories) {
            categoryDetails.add(new CategoryDetail(cat.getId(), cat.getName(), 0, 0));
        }

        return categoryDetails;
    }

}
