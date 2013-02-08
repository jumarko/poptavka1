/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.service.demand;

import com.eprovement.poptavka.dao.demand.DemandDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.demand.DemandOrigin;
import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.GenericService;

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
     * Load all demand origins available for demands.
     *
     * @retun all possible demand's origins
     * @see com.eprovement.poptavka.domain.demand.DemandOrigin
     */
    List<DemandOrigin> getDemandOrigins();

    /**
     * Load specific demand origin that is uniquely identified by its code.
     *
     * @param code unique code of Demand origin
     * @return demand origin with given code or null if no such demand origin exists
     * @throws IllegalArgumentException if given code is empty
     *
     * @see DemandOrigin
     */
    DemandOrigin getDemandOrigin(String code);

    /**
     * Load all demands associated to the given locality (-ies).
     *
     * @see com.eprovement.poptavka.dao.demand.DemandDao#getDemands(com.eprovement.poptavka.domain.address.Locality[],
     * com.eprovement.poptavka.domain.common.ResultCriteria)
     *
     * @param localities
     * @return
     */
    Set<Demand> getDemands(Locality... localities);

    /**
     * The same as {@link #getDemands(com.eprovement.poptavka.domain.address.Locality...)},
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
     * Use this method instead of {@link #getDemands(com.eprovement.poptavka.domain.address.Locality...)} if you want
     * to retrieve only number of demands - this method is far more lightweight than usage of
     * {@link #getDemands(com.eprovement.poptavka.domain.address.Locality...)}.size().
     *
     * @param localities
     * @return number of demands related to the <code>locality</code>(-ies).
     */
    long getDemandsCount(Locality... localities);


    /**
     * Similar to the {@link #getDemandsCount(com.eprovement.poptavka.domain.address.Locality...)}
     * but with better performance.
     * This includes restriction to the one locality.
     *
     * @param locality
     * @return
     */
    long getDemandsCountQuick(Locality locality);

    /** @see DemandDao#getDemandsCountWithoutChildren(com.eprovement.poptavka.domain.address.Locality)  */
    long getDemandsCountWithoutChildren(Locality locality);


    /**
     * Load all demands associated to the given category (-ies).
     *
     * @param categories
     * @return
     */
    Set<Demand> getDemands(Category... categories);

    /**
     * The same as {@link #getDemands(com.eprovement.poptavka.domain.demand.Category...)},
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
     * Use this method instead of {@link #getDemands(com.eprovement.poptavka.domain.demand.Category...)} if you want
     * to retrieve only number of demands - this method is far more lightweight than usage of
     * {@link #getDemands(com.eprovement.poptavka.domain.demand.Category...)}.size().
     *
     * @param categories
     * @return number of demands related to the <code>category</code>(-ies).
     */
    long getDemandsCount(Category... categories);

    /**
     * Similar to the {@link #getDemandsCount(com.eprovement.poptavka.domain.demand.Category...)}
     * but with better performance.
     * This includes restriction to the one category.
     *
     * @param category
     * @return
     */
    long getDemandsCountQuick(Category category);


    /** @see DemandDao#getDemandsCountWithoutChildren(com.eprovement.poptavka.domain.demand.Category) */
    long getDemandsCountWithoutChildren(Category category);

    /**
     * Returns the count of all demands in DB.
     * @return
     */
    long getAllDemandsCount();

    /**
     * Gets the count of offers associated with the supplied demand.
     * @param demand
     * @return
     */
    long getOfferCount(Demand demand);


    /**
     * Gets all the demands from given categories (including their
     * subcategories) which are also associated to the given localities
     * (all their sub-localities) which also meet the supplied criteria
     *
     * @param resultCriteria
     * @param categories
     * @param localities
     * @return
     */
    Set<Demand> getDemands(ResultCriteria resultCriteria,
                           List<Category> categories, List<Locality> localities);

    /**
     * Evaluate the number of demands associated to the given
     * <code>category</code>(-ies) and <code>locality</code>(-ies)
     * at the same time.
     *
     * @param categories
     * @return number of demands related to the <code>category</code>(-ies).
     */
    long getDemandsCount(List<Category> categories, List<Locality> localities);

    /**
     * Get number of all client demands that have at least one offer.
     * @param client - client's demands
     * @return number of client's demands with offers
     */
    long getClientDemandsWithOfferCount(Client client);

    /**
     * Get all client demands that have at least one offer.
     * @param client - client's demands
     * @return client's demands with offers
     */
    List<Demand> getClientDemandsWithOffer(Client client);

}
