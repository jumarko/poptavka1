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
import com.eprovement.poptavka.domain.demand.DemandType;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.service.GenericService;
import com.googlecode.genericdao.search.Search;

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
     * Creates new demand associated to the {@code creator}.
     * <p>
     *     Some default values can be filled if they are not specified in <code>demand</code> object.
     *     <ul>
     *        <li>Demand#type -- set to "normal" if it is not specified (null)</li>
     *     </ul>
     * @param demand demand to be created
     * @return created demand with NEW status
     */
    Demand create(Demand demand);

    /**
     * Load all types available for demands.
     *
     * @return all demand's types
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
     *  @see DemandDao#getDemandsIncludingParents(java.util.List, java.util.List,
     *                                            com.eprovement.poptavka.domain.common.ResultCriteria)
     */
    Set<Demand> getDemandsIncludingParents(List<Category> categories, List<Locality> localities,
                                           ResultCriteria resultCriteria);

    /**
     * Get number of all client demands that have at least one offer and are not assigned yet i.e. demands are in status
     * {@link com.eprovement.poptavka.domain.enums.DemandStatus#OFFERED} only.
     *
     * @param client - client's demands
     * @return number of client's demands with offers
     */
    long getClientDemandsWithOfferCount(Client client);

    /**
     * Get all client demands that have at least one offer and are not assigned yet i.e. demands are in status
     * {@link com.eprovement.poptavka.domain.enums.DemandStatus#OFFERED} only.
     *
     * @param client - client's demands
     * @return client's demands with offers
     */
    List<Demand> getClientDemandsWithOffer(Client client);

    /**
     * Activates given demand.
     * This means that {@link Demand#status} will be set to
     * {@link com.eprovement.poptavka.domain.enums.DemandStatus#ACTIVE}.
     * @param demand demand which will be activated. must not be in active state.
     * @throws IllegalArgumentException if given demand is null or it has already been activated
     */
    void activateDemand(Demand demand) throws IllegalArgumentException;


    /**
     * Gets all the demands of the given user along with the number of
     * UNREAD messages that span from the demand message (including the demand
     * message itself)
     *
     * @param businessUser
     * @return a map keyed by the user's demands containing the number
     * of UNREAD messages spanning from the demand message (including the
     * demand message itself)
     */
    Map<Demand, Integer> getClientDemandsWithUnreadSubMsgs(BusinessUser businessUser);

    /**
     * Gets all the demands of the given user along with the number of
     * UNREAD messages that span from the demand message (including the demand
     * message itself)
     *
     * @param businessUser
     * @param search to be performed on the result
     * @return a map keyed by the user's demands containing the number
     * of UNREAD messages spanning from the demand message (including the
     * demand message itself)
     */
    Map<Demand, Integer> getClientDemandsWithUnreadSubMsgs(BusinessUser businessUser,
            Search search);

    /**
     * Gets the count of all ACTIVE, OFFERED, NEW, INACTIVE or INVALID demands of the given business user
     *
     * @param businessUser
     * @return count of user's demands
     */
    long getClientDemandsCount(BusinessUser businessUser);

    /**
     * Gets all the demands with offer of the given user along with the number of
     * UNREAD messages that span from the demand message (including the demand
     * message itself)
     *
     * @param businessUser
     * @param search search to be performed on the result
     * @return a map keyed by the user's demands containing the number
     * of UNREAD messages spanning from the demand message (including the
     * demand message itself)
     */
    Map<Demand, Integer> getClientOfferedDemandsWithUnreadOfferSubMsgs(BusinessUser businessUser);
    /**
     * Gets all the demands with offer of the given user along with the number of
     * UNREAD messages that span from the demand message (including the demand
     * message itself)
     *
     * @param businessUser
     * @param search to be performed on the result
     * @return a map keyed by the user's demands containing the number
     * of UNREAD messages spanning from the demand message (including the
     * demand message itself)
     */
    Map<Demand, Integer> getClientOfferedDemandsWithUnreadOfferSubMsgs(BusinessUser businessUser,
            Search search);


    /**
     * Gets the count of all OFFERED demands of the given business user
     *
     * @param businessUser
     * @return count of user's demands
     */
    long getClientOfferedDemandsCount(BusinessUser businessUser);
}
