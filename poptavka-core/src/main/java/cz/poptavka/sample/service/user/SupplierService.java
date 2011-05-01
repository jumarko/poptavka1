package cz.poptavka.sample.service.user;

import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;

import java.util.Map;
import java.util.Set;

/**
 * Basic service class that provides methods for handling suppliers.
 *
 * @see cz.poptavka.sample.domain.user.Supplier
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public interface SupplierService {

    /**
     * Load all suppliers associated to the given locality (-ies).
     *
     * @see cz.poptavka.sample.dao.demand.DemandDao#getDemands(cz.poptavka.sample.domain.address.Locality...)
     *
     * @param localities
     * @return
     */
    Set<Supplier> getSuppliers(Locality... localities);


    /**
     * The same as {@link #getSuppliers(cz.poptavka.sample.domain.address.Locality...)}
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
     * Use this method instead of {@link #getSuppliers(cz.poptavka.sample.domain.address.Locality...)} if you want
     * to retrieve only number of suppliers - this method is far more lightweight than usage of
     * {@link #getSuppliers(cz.poptavka.sample.domain.address.Locality...)}.size().
     *
     * @param localities
     * @return number of suppliers related to the <code>locality</code>(-ies).
     */
    long getSuppliersCount(Locality... localities);


    /**
     * Similar to the {@link #getSuppliersCount(cz.poptavka.sample.domain.address.Locality...)}
     * but restricted to the one locality and with better performance.
     *
     * @param locality
     * @return
     */
    long getSuppliersCountQuick(Locality locality);


    /**
     *  @see cz.poptavka.sample.dao.user.SupplierDao#
     *  getSuppliersCountWithoutChildren(cz.poptavka.sample.domain.address.Locality)
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
     * The same as {@link #getSuppliers(cz.poptavka.sample.domain.demand.Category...)}
     * but apply additional criteria to the result.
     *
     * @param resultCriteria additional criteria, can be null
     * @param categories
     * @return
     */
    Set<Supplier> getSuppliers(ResultCriteria resultCriteria, Category... categories);

    /**
     * Highly optimized method for getting number of suppliers for all categories.
     *
     * @return map which contains pairs <category, suppliersCountForCategory>
     */
    Map<Category, Long> getSuppliersCountForAllCategories();

    /**
     * Evaluate the number of suppliers associated to the given <code>category</code>(-ies).
     * <p>
     * Use this method instead of {@link #getSuppliers(cz.poptavka.sample.domain.demand.Category...)} if you want
     * to retrieve only number of suppliers - this method is far more lightweight than usage of
     * {@link #getSuppliers(cz.poptavka.sample.domain.demand.Category...)}.size().
     *
     * @param categories
     * @return number of suppliers related to the <code>category</code>(-ies).
     */
    long getSuppliersCount(Category... categories);


    /**
     * Similar to the {@link #getSuppliersCount(cz.poptavka.sample.domain.demand.Category...)}
     * but restricted to the one category and with better performance.
     *
     * @param category
     * @return
     */
    long getSuppliersCountQuick(Category category);


    /**
     *  @see cz.poptavka.sample.dao.user.SupplierDao#
     *  getSuppliersCountWithoutChildren(cz.poptavka.sample.domain.demand.Category)
     */
    long getSuppliersCountWithoutChildren(Category category);
}
