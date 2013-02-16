/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.dao.demand;

import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.user.Client;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Excalibur
 */
public interface DemandDao extends GenericDao<Demand> {


    /**
     * Load all demands for given localities while applying additional criteria if they are specified.
     *
     * <p>This methods works recursively, that means that really ALL demands
     * for given localities are returned - NOT ONLY demands directly assigned to those localities!
     * <p>
     * E.g.
     * Let be
     *
     * <div>
     * <pre>
     * + loc1
     *   + loc11
     *   + loc12
     *      + 121
     * + loc2
     * </pre>
     * </div>
     *
     * the hierarchy of localities. Then <code>getDemands(loc1)</code> must return all demands related to the following
     * localities: loc1, loc11, loc12 and loc121.
     *
     * @param localities collection of all localities for which we want return (posisibly all) demands that are related
     * to those localities
     * @param resultCriteria additional criteria that will be applied to the found demands, can be null
     * @return all demands that (directly OR indirectly) belongs to the some from given <code>localities</code>.
     * @throws IllegalStateException if <code>resultCriteria</code> specifies order by columns,
     *     see {@link ResultCriteria#orderByColumns}
     */
    Set<Demand> getDemands(Locality[] localities, ResultCriteria resultCriteria);


    /**
     * Optmized method for loading demands count for all localities in one query!
     *
     * @return list of maps, each map containing only 2 items ("locality" => locality, "demandsCount" => demandsCount)
     */
    List<Map<String, Object>> getDemandsCountForAllLocalities();


    /**
     * Get count of ALL demands associated to the some locality from given <code>localities</code>
     *
     * <p>
     * See {@link #getDemands(com.eprovement.poptavka.domain.address.Locality[],
     * com.eprovement.poptavka.domain.common.ResultCriteria)}
     * for further explanation.
     *
     * @param localities
     * @return
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

    /**
     * Get count of all demands that belongs directly to the specified locality. No demands belonging to
     * any sublocality are included!
     */
    long getDemandsCountWithoutChildren(Locality locality);


    /**
     * Optmized method for loading demands count for all categories in one query!
     *
     * @return list of maps, each map containing only 2 items ("category" => category, "demandsCount" => demandsCount)
     */
    List<Map<String, Object>> getDemandsCountForAllCategories();

    /**
     * Load all demands for given categories while applying additional criteria <code>resultCriteria</code>
     * if they are specified.
     *
     * <p>
     *     This methods works recursively, that means that really ALL demands
     * for given categories are returned - NOT ONLY demands directly assigned to those categories!
     *
     *
     * @param resultCriteria
     * @param categories
     * @return all demands that (directly OR indirectly) belongs to the some from given <code>localities</code>.
     * @throws IllegalStateException if <code>resultCriteria</code> specifies order by columns
     *
     * @see #getDemands(com.eprovement.poptavka.domain.address.Locality[],
     * com.eprovement.poptavka.domain.common.ResultCriteria)
     * @see ResultCriteria#orderByColumns
     */
    Set<Demand> getDemands(Category[] categories, ResultCriteria resultCriteria);

    /**
     * Get count of ALL demands associated to the some category from given <code>categories</code>.
     *
     * @param categories
     * @return
     *
     * @see #getDemands(com.eprovement.poptavka.domain.address.Locality[],
     * com.eprovement.poptavka.domain.common.ResultCriteria)
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

    /**
     * Get count of all demands that belongs directly to the specified category. No demands belonging to
     * any subcategory are included!
     */
    long getDemandsCountWithoutChildren(Category category);



    /**
     * Returns the count of all demands in DB.
     * @return
     */
    long getAllDemandsCount();



    /**
     * Load all demands associated to the given category (-ies) and
     * category (-ies) - each must be associated to both - while applying
     * additional criteria <code>resultCriteria</code> if they are specified.
     *
     * @param categories
     * @param localities
     * @param resultCriteria
     * @return collection of demands that are related to the given localities and adher to <code>resultCriteria</code>
     * @throws IllegalStateException if <code>resultCriteria</code> specifies order by columns
     */
    Set<Demand> getDemands(List<Category> categories, List<Locality> localities, ResultCriteria resultCriteria);


     /**
     * Evaluates the number of demands associated to the given
     * <code>locality</code>(-ies) and <code>category</code>(-ies).
     * <p>
     * Use this method instead of {@link #getDemands(com.eprovement.poptavka.domain.address.Locality[],
     * com.eprovement.poptavka.domain.common.ResultCriteria)} if you want
     * to retrieve only number of suppliers - this method is far more lightweight than usage of
     * {@link #getDemands(com.eprovement.poptavka.domain.address.Locality[],
     * com.eprovement.poptavka.domain.common.ResultCriteria)}
     *      .size().
     *
     * @param localities
     * @return number of suppliers related to the <code>locality</code>(-ies).
     */

    long getDemandsCount(List<Category> categories, List<Locality> localities, ResultCriteria resultCriteria);


    /**
     * Loads all the demands associated to the any category (or its parent) of given {@code categories}
     * AND to any locality (or its parent) from given localities.
     *
     * <div>
     *     Example:<br />
     *     <p>
     *     Let's have following localities:
     *     <ul>
     *         <li>Locality1</li>
     *         <li>Locality11</li>
     *         <li>Locality2</li>
     *         <li>Locality21</li>
     *     </ul>
     *     Locality1 is parent of Locality11, Locality2 is parent of Locality21. Locality1 and Locality2 are unrelated.
     *     </p>
     *     <p>
     *     Furthermore, Let's have following categories:
     *     <ul>
     *         <li>Category1</li>
     *         <li>Category11</li>
     *         <li>Category2</li>
     *         <li>Category21</li>
     *     </ul>
     *     Category1 is parent of Category11, Category2 is parent of Category21. Category1 and Category2 are unrelated.
     *     </p>
     *     <p>
     *         There are demands associated to the localities and categories:
     *         <ul>
     *             <li>Demand1-1 -> Locality1, Category1</li>
     *             <li>Demand1-11 -> Locality1, Category11</li>
     *             <li>Demand1-2 -> Locality1, Category2</li>
     *             <li>Demand1-21 -> Locality1, Category21</li>
     *             <li>Demand11-1 -> Locality11, Category1</li>
     *             <li>Demand11-11 -> Locality11, Category11</li>
     *             <li>Demand11-2 -> Locality11, Category2</li>
     *             <li>Demand11-21 -> Locality11, Category21</li>
     *             <li>Demand2-1 -> Locality2, Category1</li>
     *             <li>Demand2-11 -> Locality2, Category11</li>
     *             <li>Demand2-2 -> Locality2, Category2</li>
     *             <li>Demand2-21 -> Locality2, Category21</li>
     *             <li>Demand21-1 -> Locality1, Category1</li>
     *             <li>Demand21-11 -> Locality1, Category11</li>
     *             <li>Demand21-2 -> Locality1, Category2</li>
     *             <li>Demand21-21 -> Locality1, Category21</li>
     *         </ul>
     *     </p>
     *
     *     <p>
     *         If we issue following method:
     *         <pre>
     *             getDemandsIncludingParents(
     *                 Arrays.asList(Locality11, Locality2),
     *                 Arrays.asList(Category21),
     *                 EMPTY_CRITERIA)
     *         </pre>
     *         then we should get collection {Demand1-2, Demand1-21, Demand11-2, Demand11-21, Demand2-2, Demand2-21}
     *     </p>
     * </div>
     *
     * @param resultCriteria further search criteria
     * @param categories list of categories
     * @param localities list of all localities
     * @return list of demands associated to proper locality and category
     */
    Set<Demand> getDemandsIncludingParents(List<Category> categories, List<Locality> localities,
                                           ResultCriteria resultCriteria);

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
