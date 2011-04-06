package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;

import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public interface SupplierDao extends GenericDao<Supplier> {

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


    long getSuppliersCountQuick(Locality locality);

    /**
     * Get count of all suppliers that belongs directly to the specified locality. No suppliers belonging to
     * any sublocality are included!
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


    long getSuppliersCountQuick(Category category);

    /**
     * Get count of all suppliers that belongs directly to the specified category. No suppliers belonging to
     * any subcategory are included!
     */
    long getSuppliersCountWithoutChildren(Category category);
}
