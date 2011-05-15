package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public interface SupplierDao extends BusinessUserRoleDao<Supplier> {

    /**
     * Load all suppliers associated to the given locality (-ies) while applying additional criteria
     * <code>resultCriteria</code> if they are specified.
     *
     * @see cz.poptavka.sample.dao.demand.DemandDao#getDemands(cz.poptavka.sample.domain.address.Locality[],
     * cz.poptavka.sample.domain.common.ResultCriteria)
     *
     * @param localities
     * @param resultCriteria additional restirction that will be set to result, can be null
     * @return collection of suppliers that are related to the given localities and adher to <code>resultCriteria</code>
     * @throws IllegalStateException if <code>resultCriteria</code> specifies order by columns
     */
    Set<Supplier> getSuppliers(Locality[] localities, ResultCriteria resultCriteria);

     /**
     * Optmized method for loading suppliers count for all localities in one query!
     *
     * @return list of maps, each map containing only 2 items
      *         ("locality" => locality, "suppliersCount" => suppliersCount)
     */
    List<Map<String, Object>> getSuppliersCountForAllLocalities();

    /**
     * Evaluate the number of suppliers associated to the given <code>locality</code>(-ies).
     * <p>
     * Use this method instead of {@link #getSuppliers(cz.poptavka.sample.domain.address.Locality[],
     * cz.poptavka.sample.domain.common.ResultCriteria)} if you want
     * to retrieve only number of suppliers - this method is far more lightweight than usage of
     * {@link #getSuppliers(cz.poptavka.sample.domain.address.Locality[],
     * cz.poptavka.sample.domain.common.ResultCriteria)}
     *      .size().
     *
     * @param localities
     * @return number of suppliers related to the <code>locality</code>(-ies).
     */
    long getSuppliersCount(Locality... localities);


    long getSuppliersCountQuick(Locality locality);

    /**
     * Get count of all suppliers that belongs directly to the specified locality. No suppliers belonging to
     * any sublocality are included!
     */
    long getSuppliersCountWithoutChildren(Locality locality);


    /**
     * Load all suppliers associated to the given category (-ies) while applying additional criteria
     * <code>resultCriteria</code> if they are specified.
     *
     * @param categories
     * @param resultCriteria
     * @return collection of suppliers that are related to the given localities and adher to <code>resultCriteria</code>
     * @throws IllegalStateException if <code>resultCriteria</code> specifies order by columns
     */
    Set<Supplier> getSuppliers(Category[] categories, ResultCriteria resultCriteria);


    /**
     * Optmized method for loading suppliers count for all categories in one query!
     *
     * @return list of maps, each map containing only 2 items
     *              ("category" => category, "suppliersCount" => suppliersCount)
     */
    List<Map<String, Object>> getSuppliersCountForAllCategories();

    /**
     * Evaluate the number of suppliers associated to the given <code>category</code>(-ies).
     * <p>
     * Use this method instead of {@link #getSuppliers(cz.poptavka.sample.domain.demand.Category[],
     * cz.poptavka.sample.domain.common.ResultCriteria)} if you want
     * to retrieve only number of suppliers - this method is far more lightweight than usage of
     * {@link #getSuppliers(cz.poptavka.sample.domain.demand.Category[],
     * cz.poptavka.sample.domain.common.ResultCriteria)}
     *      .size().
     * </p>
     *
     * @param categories
     * @return number of suppliers related to the <code>category</code>(-ies).
     */
    long getSuppliersCount(Category... categories);


    long getSuppliersCountQuick(Category category);

    /**
     * Get count of all suppliers that belongs directly to the specified category. No suppliers belonging to
     * any subcategory are included!
     */
    long getSuppliersCountWithoutChildren(Category category);
}
