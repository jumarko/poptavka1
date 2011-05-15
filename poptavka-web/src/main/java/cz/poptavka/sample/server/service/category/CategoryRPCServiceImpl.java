package cz.poptavka.sample.server.service.category;

import cz.poptavka.sample.client.service.demand.CategoryRPCService;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class CategoryRPCServiceImpl extends AutoinjectingRemoteService
        implements CategoryRPCService {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -5635188205093095585L;
    private CategoryService categoryService;
    private static final Logger LOGGER = Logger.getLogger("CategoryRPCServiceImpl");

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public Category getCategory(String code) {
        return categoryService.getCategory(code);
    }

    @Override
    public Category getCategory(long id) {
        return categoryService.getById(id);
    }

    @Override
    public List<Category> getAllRootCategories() {
        return categoryService.getRootCategories();
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
            CategoryDetail detail = new CategoryDetail(cat.getId(), cat.getName(), 0, 0);
//            CategoryDetail detail = new CategoryDetail(cat.getId(), cat.getName(),
//                  cat.getAdditionalInfo().getDemandsCount(), cat.getAdditionalInfo().getSuppliersCount());
            if (cat.getChildren().size() != 0) {
                detail.setParent(true);
            } else {
                detail.setParent(false);
            }
            categoryDetails.add(detail);
        }

        return categoryDetails;
    }
}
