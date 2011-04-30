package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.dao.demand.CategoryDao;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.service.GenericService;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
public interface CategoryService extends GenericService<Category, CategoryDao> {

    /** @see cz.poptavka.sample.dao.demand.CategoryDao#getRootCategories()  */
    List<Category> getRootCategories();


    /**
     * The same as {@link #getRootCategories()},
     * but additional criteria can be applied to the result
     *
     * @return all root categories that (eventually) satisfy given (optional) criteria.
     */
    List<Category> getRootCategories(ResultCriteria resultCriteria);


    /** @see cz.poptavka.sample.dao.demand.CategoryDao#getCategory(String) */
    Category getCategory(String code);

}
