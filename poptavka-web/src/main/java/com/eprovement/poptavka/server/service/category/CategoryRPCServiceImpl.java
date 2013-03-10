package com.eprovement.poptavka.server.service.category;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOGGER = LoggerFactory.getLogger("CategoryRPCServiceImpl");

    @Autowired
    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @Autowired
    public void setClientDemandMessageConverter(@Qualifier("categoryConverter")
            Converter<Category, CategoryDetail> categoryConverter) {
        this.categoryConverter = categoryConverter;
    }

    @Autowired
    public void setSupplierService(SupplierService supplierService) throws RPCException {
        this.supplierService = supplierService;
    }

    @Override
    public CategoryDetail getCategory(long id) throws RPCException {
        return categoryConverter.convertToTarget(categoryService.getById(id));
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
        if (category != null) {
            final Category cat = categoryService.getById(category);
            if (cat != null) {
                return categoryConverter.convertToTargetList(cat.getChildren());
            }
        }
        return new ArrayList<CategoryDetail>();
    }

}
