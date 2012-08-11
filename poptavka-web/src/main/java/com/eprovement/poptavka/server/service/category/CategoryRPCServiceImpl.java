package com.eprovement.poptavka.server.service.category;

import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import com.eprovement.poptavka.client.service.demand.CategoryRPCService;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.server.converter.Converter;
import com.eprovement.poptavka.server.service.AutoinjectingRemoteService;
import com.eprovement.poptavka.service.demand.CategoryService;
import com.eprovement.poptavka.service.user.SupplierService;
import com.eprovement.poptavka.shared.domain.CategoryDetail;
import com.eprovement.poptavka.shared.exceptions.RPCException;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Qualifier;

@Configurable
public class CategoryRPCServiceImpl extends AutoinjectingRemoteService
        implements CategoryRPCService {

    private CategoryService categoryService;
    private SupplierService supplierService;
    private Converter<Category, CategoryDetail> categoryConverter;
    private static final Logger LOGGER = Logger.getLogger("CategoryRPCServiceImpl");

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setClientDemandMessageConverter(@Qualifier("categoryConverter")
            Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Override
    public CategoryDetail getCategory(long id) throws RPCException {
        return categoryConverter.convertToTarget(categoryService.getById(id));
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) throws RPCException {
        this.supplierService = supplierService;
    }

    @Override
    public List<CategoryDetail> getAllRootCategories() throws RPCException {
        return categoryConverter.convertToTargetList(categoryService.getRootCategories());
    }

    @Override
    public List<CategoryDetail> getCategories() throws RPCException {
        return categoryConverter.convertToTargetList(categoryService.getRootCategories());
    }

    /**
     * Return all parents of given category within given category.
     * @param category - given category id
     * @return list of parents and given category
     */
    @Override
    public List<CategoryDetail> getCategoryParents(Long category) throws RPCException {
        System.out.println("Getting parent categories");
        Category cat = categoryService.getById(category);
        List<Category> parents = new ArrayList<Category>();
        //add cat itself
        parents.add(cat);
        while (cat.getParent() != null) {
            parents.add(cat.getParent());
            cat = cat.getParent();
        }

        return categoryConverter.convertToTargetList(parents);
    }

    @Override
    public List<CategoryDetail> getCategoryChildren(Long category) throws RPCException {
        System.out.println("Getting children categories");
        try {
            if (category != null) {
                final Category cat = categoryService.getById(category);
                if (cat != null) {
                    return categoryConverter.convertToTargetList(cat.getChildren());
                }
            }
        } catch (NullPointerException ex) {
            LOGGER.info("NullPointerException while executing getCategoryChildren");
        }
        return new ArrayList<CategoryDetail>();
    }

    /** Inner method for transforming domain Entity to front-end representation. **/
//    private List<CategoryDetail> createCategoryDetailList(List<Category> categories) {
//        final List<CategoryDetail> categoryDetails = new ArrayList<CategoryDetail>();
//
//        for (Category cat : categories) {
//            categoryDetails.add(createCategoryDetail(cat));
//        }
//
//        return categoryDetails;
//    }

//    private CategoryDetail createCategoryDetail(Category category) {
//        long suppliersCount = supplierService.getSuppliersCountQuick(category);
//        CategoryDetail detail = new CategoryDetail(category.getId(), category.getName(), 0, suppliersCount);
        // TODO uncomment, when implemented
//        CategoryDetail detail = new CategoryDetail(cat.getId(), cat.getName(),
//              cat.getAdditionalInfo().getDemandsCount(), cat.getAdditionalInfo().getSuppliersCount());
//        if (category.getChildren().size() != 0) {
//            detail.setParent(true);
//        } else {
//            detail.setParent(false);
//        }
//        return detail;
//    }
}
