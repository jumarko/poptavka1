package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.demand.Category;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 6.2.11
 */
public interface CategoryDao extends GenericDao<Category> {

    /**
     * Load all root categories, ie. categories that have no parent.
     * This should correspond with categories at level 1.
     *
     * @param resultCriteria additional criteria for filtering root categories, can be null.
     */
    List<Category> getRootCategories(ResultCriteria resultCriteria);

    /**
     * Load category object by unique code.
     *
     * @param code unique code representing category
     * @return category with given code or null if no such category exists.
     */
    Category getCategory(String code);
}
