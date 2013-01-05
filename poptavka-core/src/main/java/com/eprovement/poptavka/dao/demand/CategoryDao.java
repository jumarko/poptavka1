package com.eprovement.poptavka.dao.demand;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.demand.Category;

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
     * @param id unique code representing category
     * @return category with given code or null if no such category exists.
     */
    Category getCategory(Long id);
}
