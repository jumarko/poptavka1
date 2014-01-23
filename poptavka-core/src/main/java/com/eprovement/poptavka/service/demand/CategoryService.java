package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.demand.CategoryDao;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.ExternalCategory;
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


    /** @see com.eprovement.poptavka.dao.demand.CategoryDao#getCategory(Long)  */
    Category getCategory(Long id);

    /**
     * Finds external category for given {@code externalId}.
     * @return external category for given {@code externalId} or null if no such category exist
     * @see com.eprovement.poptavka.domain.demand.ExternalCategory
     */
    ExternalCategory getExternalCategory(String externalId);

    /**
     * Loads mapping "external categories to internal categories" for given external source.
     * @param externalSourceName name of external source such as FBOGOV (fbo.gov)
     * @return list of mappings, each representing mapping [external_category, internal_categories]
     * @see com.eprovement.poptavka.domain.demand.ExternalCategory#categories
     */
    List<ExternalCategory> getCategoryMapping(String externalSourceName);

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
     * contains <code>nameSubstring</code> and whose type is <code>type</code>
     * @param minLength all <code>Category</code>-ies' names returned must be at least of the given length
     * @param nameSubstring a <code>String</code> that all the categories' names must contain
     * @return a <code>List<code> of categories satisfying criteria
     */
    List<Category> getCategoriesByMinLength(int minLength, String nameSubstring);


}
