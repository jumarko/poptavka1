package cz.poptavka.sample.server.service.category;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.CategoryRPCService;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.shared.domain.CategoryDetail;

public class CategoryRPCServiceImpl extends AutoinjectingRemoteService
        implements CategoryRPCService {

    private CategoryService categoryService;
    private static final Logger LOGGER = Logger.getLogger("CategoryRPCServiceImpl");

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDetail getCategory(long id) {
        return createCategoryDetail(categoryService.getById(id));
    }

//    @Override
//    public CategoryDetail getCategory(String code) {
//        return createCategoryDetail(categoryService.getCategory(code));
//    }

    @Override
    public ArrayList<CategoryDetail> getAllRootCategories() {
        return createCategoryDetailList(categoryService.getRootCategories());
    }

    @Override
    public ArrayList<CategoryDetail> getCategories() {
        final List<Category> categories = categoryService.getRootCategories();
        return createCategoryDetailList(categories);
    }

    @Override
    public ArrayList<CategoryDetail> getCategoryChildren(String category) {
        System.out.println("Getting children categories");
        try {
            if (category != null) {
                final Category cat = categoryService.getById(Long.valueOf(category));
                if (cat != null) {
                    return createCategoryDetailList(cat.getChildren());
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.info("NullPointerException while executing getCategoryChildren");
        }
        return new ArrayList<CategoryDetail>();
    }

    /** Inner method for transforming domain Entity to front-end representation. **/
    private ArrayList<CategoryDetail> createCategoryDetailList(List<Category> categories) {
        final ArrayList<CategoryDetail> categoryDetails = new ArrayList<CategoryDetail>();

        for (Category cat : categories) {
            categoryDetails.add(createCategoryDetail(cat));
        }

        return categoryDetails;
    }

    private CategoryDetail createCategoryDetail(Category category) {
        CategoryDetail detail = new CategoryDetail(category.getId(), category.getName(), 0, 0);
        // TODO uncomment, when implemented
//        CategoryDetail detail = new CategoryDetail(cat.getId(), cat.getName(),
//              cat.getAdditionalInfo().getDemandsCount(), cat.getAdditionalInfo().getSuppliersCount());
        if (category.getChildren().size() != 0) {
            detail.setParent(true);
        } else {
            detail.setParent(false);
        }
        return detail;
    }

}
