package com.eprovement.poptavka.service.user;

import com.eprovement.poptavka.dao.user.SupplierDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.Supplier;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Basic service class that provides methods for handling suppliers.
 *
 * @see com.eprovement.poptavka.domain.user.Supplier
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public interface SupplierService extends BusinessUserRoleService<Supplier, SupplierDao> {

    /**
     * Load all suppliers associated to the given locality (-ies).
     *
     * @see com.eprovement.poptavka.dao.demand.DemandDao#getDemands(com.eprovement.poptavka.domain.address.Locality...)
     *
     * @param localities
     * @return
     */
    Set<Supplier> getSuppliers(Locality... localities);


    /**
     * The same as {@link #getSuppliers(com.eprovement.poptavka.domain.address.Locality...)}
     * but apply additional criteria to the result.
     *
     * @param resultCriteria additional criteria, can be null
     * @param localities
     * @return
     */
    Set<Supplier> getSuppliers(ResultCriteria resultCriteria, Locality... localities);

    /**
     * Highly optimized method for getting number of suppliers for all localities.
     *
     * @return map which contains pairs <locality, suppliersCountForLocality>
     */
    Map<Locality, Long> getSuppliersCountForAllLocalities();

    /**
     * Evaluate the number of suppliers associated to the given <code>locality</code>(-ies).
     * <p>
     * Use this method instead of {@link #getSuppliers(com.eprovement.poptavka.domain.address.Locality...)} if you want
     * to retrieve only number of suppliers - this method is far more lightweight than usage of
     * {@link #getSuppliers(com.eprovement.poptavka.domain.address.Locality...)}.size().
     *
     * @param localities
     * @return number of suppliers related to the <code>locality</code>(-ies).
     */
    long getSuppliersCount(Locality... localities);


    /**
     * Similar to the {@link #getSuppliersCount(com.eprovement.poptavka.domain.address.Locality...)}
     * but restricted to the one locality and with better performance.
     *
     * @param locality
     * @return
     */
    long getSuppliersCountQuick(Locality locality);


    /**
     *  @see com.eprovement.poptavka.dao.user.SupplierDao#
     *  getSuppliersCountWithoutChildren(com.eprovement.poptavka.domain.address.Locality)
     */
    long getSuppliersCountWithoutChildren(Locality locality);



    /**
     * Load all suppliers associated to the given category (-ies).
     *
     * @param categories
     * @return
     */
    Set<Supplier> getSuppliers(Category... categories);

    /**
     *                                                            /**
     * The same as {@link #getSuppliers(com.eprovement.poptavka.domain.demand.Category...)}
     * but apply additional criteria to the result.
     *
     * @param resultCriteria additional criteria, can be null
     * @param categories
     * @return
     */
    Set<Supplier> getSuppliers(ResultCriteria resultCriteria, Category... categories);
    /**
     * Gets all the supplier from given categories (including their
     * subcategories) which are also associated to the given localities
     * (all their sub-localities) which also meet the supplied criteria
     *
     * @param resultCriteria
     * @param categories
     * @param localities
     * @return
     */
    Set<Supplier> getSuppliers(ResultCriteria resultCriteria,
                               List<Category> categories, List<Locality> localities);

    /**
     * @see SupplierDao#getSuppliersIncludingParents(java.util.List, java.util.List,
     * com.eprovement.poptavka.domain.common.ResultCriteria).
     */
    Set<Supplier> getSuppliersIncludingParents(List<Category> categories, List<Locality> localities,
                                               ResultCriteria resultCriteria);
    /**
     * Highly optimized method for getting number of suppliers for all categories.
     *
     * @return map which contains pairs <category, suppliersCountForCategory>
     */
    Map<Category, Long> getSuppliersCountForAllCategories();

    /**
     * Evaluate the number of suppliers associated to the given <code>category</code>(-ies).
     * <p>
     * Use this method instead of {@link #getSuppliers(com.eprovement.poptavka.domain.demand.Category...)} if you want
     * to retrieve only number of suppliers - this method is far more lightweight than usage of
     * {@link #getSuppliers(com.eprovement.poptavka.domain.demand.Category...)}.size().
     *
     * @param categories
     * @return number of suppliers related to the <code>category</code>(-ies).
     */
    long getSuppliersCount(Category... categories);

    /**
     * Evaluate the number of suppliers associated to the given
     * <code>category</code>(-ies) and <code>locality</code>(-ies)
     * at the same time.
     *
     * @param categories
     * @return number of suppliers related to the <code>category</code>(-ies).
     */
    long getSuppliersCount(List<Category> categories, List<Locality> localities);

   /**
    * Evaluate the number of suppliers associated to the given
    * <code>category</code>(-ies) and <code>locality</code>(-ies)
    * at the same time.
    *
    * @param categories
    * @param localities
    * @param resultCriteria
    * @return
    */
    long getSuppliersCount(List<Category> categories, List<Locality> localities, ResultCriteria resultCriteria);

    /**
     * Similar to the {@link #getSuppliersCount(com.eprovement.poptavka.domain.demand.Category...)}
     * but restricted to the one category and with better performance.
     *
     * @param category
     * @return
     */
    long getSuppliersCountQuick(Category category);


    /**
     *  @see com.eprovement.poptavka.dao.user.SupplierDao#
     *  getSuppliersCountWithoutChildren(com.eprovement.poptavka.domain.demand.Category)
     */
    long getSuppliersCountWithoutChildren(Category category);
}
