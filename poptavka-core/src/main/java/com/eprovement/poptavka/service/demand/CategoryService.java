package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.demand.CategoryDao;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.service.GenericService;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
public interface CategoryService extends GenericService<Category, CategoryDao> {

    /** @see com.eprovement.poptavka.dao.demand.CategoryDao#getRootCategories()  */
    List<Category> getRootCategories();


    /**
     * The same as {@link #getRootCategories()},
     * but additional criteria can be applied to the result
     *
     * @return all root categories that (eventually) satisfy given (optional) criteria.
     */
    List<Category> getRootCategories(ResultCriteria resultCriteria);


    /** @see com.eprovement.poptavka.dao.demand.CategoryDao#getCategory(String) */
    Category getCategory(Long id);

}
