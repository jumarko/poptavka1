package com.eprovement.poptavka.dao.demand;

import com.eprovement.poptavka.domain.common.ExternalSource;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.ExternalCategory;

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

    /**
     * Gets a list of categories whose name is shorter than <code>maxlengthExcl</code> and
     * contains <code>nameSubstring</code>
     * @param maxLengthExcl all <code>Category</code>-ies' names returned must be shorter than the given length
     * @param nameSubstring a <code>String</code> that all the categories' names must contain
     * @return a <code>List<code> of categories satisfying criteria
     */
    List<Category> getCategoriesByMaxLengthExcl(int maxLengthExcl, String nameSubstring);

    /**
     * Gets a list of categories whose name is the same length or longer than <code>minLength</code> and
     * contains <code>nameSubstring</code>.
     * @param minLength all <code>Category</code>-ies' names returned must be at least of the given length
     * @param nameSubstring a <code>String</code> that all the categories' names must contain
     * @return a <code>List<code> of categories satisfying criteria
     */
    List<Category> getCategoriesByMinLength(int minLength, String nameSubstring);

     /**
     * Finds category by given sic code.
     * SicCode must match the exactly with the entry in underlying data source.
     */
    Category getCategoryBySicCode(String sicCode);

    /**
     * Loads mapping between external and internal categories
     * @param externalSource external source such as FBOGOV (fbo.gov)
     * @return list of mappings, each representing mapping [external_category, internal_categories]
     */
    List<ExternalCategory> getCategoryMapping(ExternalSource externalSource);
}
