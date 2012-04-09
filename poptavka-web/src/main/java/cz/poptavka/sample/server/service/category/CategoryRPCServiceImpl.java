package cz.poptavka.sample.server.service.category;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;

import cz.poptavka.sample.client.service.demand.CategoryRPCService;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.server.service.AutoinjectingRemoteService;
import cz.poptavka.sample.service.demand.CategoryService;
import cz.poptavka.sample.service.user.SupplierService;
import cz.poptavka.sample.shared.domain.CategoryDetail;
import cz.poptavka.sample.shared.exceptions.CommonException;

public class CategoryRPCServiceImpl extends AutoinjectingRemoteService
        implements CategoryRPCService {

    private CategoryService categoryService;
    private SupplierService supplierService;
    private static final Logger LOGGER = Logger.getLogger("CategoryRPCServiceImpl");

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Override
    public CategoryDetail getCategory(long id) throws CommonException {
        return createCategoryDetail(categoryService.getById(id));
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) throws CommonException {
        this.supplierService = supplierService;
    }

    @Override
    public ArrayList<CategoryDetail> getAllRootCategories() throws CommonException {
        return createCategoryDetailList(categoryService.getRootCategories());
    }

    @Override
    public ArrayList<CategoryDetail> getCategories() throws CommonException {
        final List<Category> categories = categoryService.getRootCategories();
        return createCategoryDetailList(categories);
    }

    /**
     * Return all parents of given category within given category.
     * @param category - given category id
     * @return list of parents and given category
     */
    @Override
    public ArrayList<CategoryDetail> getCategoryParents(Long category) throws CommonException {
        System.out.println("Getting parent categories");
        Category cat = categoryService.getById(category);
        List<Category> parents = new ArrayList<Category>();
        //add cat itself
        parents.add(cat);
        while (cat.getParent() != null) {
            parents.add(cat.getParent());
            cat = cat.getParent();
        }

        return createCategoryDetailList(parents);
    }

    @Override
    public ArrayList<CategoryDetail> getCategoryChildren(Long category) throws CommonException {
        System.out.println("Getting children categories");
        try {
            if (category != null) {
                final Category cat = categoryService.getById(category);
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
        long suppliersCount = supplierService.getSuppliersCountQuick(category);
        CategoryDetail detail = new CategoryDetail(category.getId(), category.getName(), 0, suppliersCount);
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
