package cz.poptavka.sample.server.service.category;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
    private static final Logger LOGGER = Logger.getLogger("CategoryRPCServiceImpl");

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

    @Override
    public ArrayList<CategoryDetail> getCategories() {
        List<Category> categories = categoryService.getRootCategories();
        System.out.println("Root category count: " + categories.size());
        return createCategoryDetailList(categories);
    }

    @Override
    public ArrayList<CategoryDetail> getCategoryChildren(String category) {
        System.out.println("Getting children categories");
        try {
            Category cat = categoryService.getCategory(category);

            List<Category> cats = treeItemService.getAllChildren(cat, Category.class);

            return createCategoryDetailList(cats);
        } catch (NullPointerException ex) {
            LOGGER.info("NullPointerException while executing getCategoryChildren");
        }
        return new ArrayList<CategoryDetail>();
    }

    /** Inner method for transforming domain Entity to front-end representation. **/
    private ArrayList<CategoryDetail> createCategoryDetailList(List<Category> categories) {
        ArrayList<CategoryDetail> categoryDetails = new ArrayList<CategoryDetail>();

        int i = 0;
        for (Category cat : categories) {
            categoryDetails.add(new CategoryDetail(cat.getId(), cat.getName(), 0, 0));
            i++;
            if (i > 25) {
                return categoryDetails;
            }
        }

        //returning only sublist of 25 items
        return categoryDetails;
    }

}
