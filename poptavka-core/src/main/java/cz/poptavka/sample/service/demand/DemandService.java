/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.dao.demand.DemandDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.service.GenericService;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Juraj Martinka
 */
public interface DemandService extends GenericService<Demand, DemandDao> {

    int ESTIMATED_NUMBER_OF_LOCALITIES = 10000;
    int ESTIMATED_NUMBER_OF_CATEGORIES = 10000;


    /**
     * Load all types available for demands.
     *
     * @retun all demand's types
     */
    List<DemandType> getDemandTypes();

    /**
     * Load specific demand type that is uniquely identified by its code.
     *
     * @param code unique code of Demand type
     * @return demand type with given code or null if no such demand type exists
     * @throws IllegalArgumentException if given code is empty
     */
    DemandType getDemandType(String code);

    /**
     * Load all demands associated to the given locality (-ies).
     *
     * @see cz.poptavka.sample.dao.demand.DemandDao#getDemands(cz.poptavka.sample.domain.address.Locality[],
     * cz.poptavka.sample.common.ResultCriteria)
     *
     * @param localities
     * @return
     */
    Set<Demand> getDemands(Locality... localities);

    /**
     * The same as {@link #getDemands(cz.poptavka.sample.domain.address.Locality...)},
     * but apply additional criteria on demands.
     * <p>
     *     ResultCriteria are applied
     *
     * @see ResultCriteria
     *
     * @param resultCriteria
     * @param localities
     * @return
     */
    Set<Demand> getDemands(ResultCriteria resultCriteria, Locality... localities);


    /**
     * Highly optimized method for getting number of demands for all localities.
     *
     * @return map which contains pairs <locality, demandsCountForLocality>
     */
    Map<Locality, Long> getDemandsCountForAllLocalities();


    /**
     * Evaluate the number of demands associated to the given <code>locality</code>(-ies).
     * <p>
     * Use this method instead of {@link #getDemands(cz.poptavka.sample.domain.address.Locality...)} if you want
     * to retrieve only number of demands - this method is far more lightweight than usage of
     * {@link #getDemands(cz.poptavka.sample.domain.address.Locality...)}.size().
     *
     * @param localities
     * @return number of demands related to the <code>locality</code>(-ies).
     */
    long getDemandsCount(Locality... localities);


    /**
     * Similar to the {@link #getDemandsCount(cz.poptavka.sample.domain.address.Locality...)}
     * but with better performance.
     * This includes restriction to the one locality.
     *
     * @param locality
     * @return
     */
    long getDemandsCountQuick(Locality locality);

     /** @see DemandDao#getDemandsCountWithoutChildren(cz.poptavka.sample.domain.address.Locality)  */
    long getDemandsCountWithoutChildren(Locality locality);


    /**
     * Load all demands associated to the given category (-ies).
     *
     * @param categories
     * @return
     */
    Set<Demand> getDemands(Category... categories);

    /**
     * The same as {@link #getDemands(cz.poptavka.sample.domain.demand.Category...)},
     * but apply additional criteria on demands.
     *
     * @see ResultCriteria
     *
     * @param resultCriteria additional criteria, can be null.
     * @param  categories
     * @return
     */
    Set<Demand> getDemands(ResultCriteria resultCriteria, Category... categories);


    /**
     * Highly optimized method for getting number of demands for all categories.
     *
     * @return map which contains pairs <category, demandsCountForCategory>
     */
    Map<Category, Long> getDemandsCountForAllCategories();

    /**
     * Evaluate the number of demands associated to the given <code>category</code>(-ies).
     * <p>
     * Use this method instead of {@link #getDemands(cz.poptavka.sample.domain.demand.Category...)} if you want
     * to retrieve only number of demands - this method is far more lightweight than usage of
     * {@link #getDemands(cz.poptavka.sample.domain.demand.Category...)}.size().
     *
     * @param categories
     * @return number of demands related to the <code>category</code>(-ies).
     */
    long getDemandsCount(Category... categories);

     /**
     * Similar to the {@link #getDemandsCount(cz.poptavka.sample.domain.demand.Category...)}
     * but with better performance.
     * This includes restriction to the one category.
     *
     * @param category
     * @return
     */
    long getDemandsCountQuick(Category category);


    /** @see DemandDao#getDemandsCountWithoutChildren(cz.poptavka.sample.domain.demand.Category) */
    long getDemandsCountWithoutChildren(Category category);
}
