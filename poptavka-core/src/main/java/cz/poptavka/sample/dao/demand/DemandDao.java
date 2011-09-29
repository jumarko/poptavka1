/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;

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
     * See {@link #getDemands(cz.poptavka.sample.domain.address.Locality[],
     * cz.poptavka.sample.domain.common.ResultCriteria)}
     * for further explanation.
     *
     * @param localities
     * @return
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
     * @see #getDemands(cz.poptavka.sample.domain.address.Locality[], cz.poptavka.sample.domain.common.ResultCriteria)
     * @see ResultCriteria#orderByColumns
     */
    Set<Demand> getDemands(Category[] categories, ResultCriteria resultCriteria);

    /**
     * Get count of ALL demands associated to the some category from given <code>categories</code>.
     *
     * @param categories
     * @return
     *
     * @see #getDemands(cz.poptavka.sample.domain.address.Locality[], cz.poptavka.sample.domain.common.ResultCriteria)
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
     * Load all demands that are new, i.e. have status {@link cz.poptavka.sample.domain.demand.DemandStatus#NEW}
     * @return
     * @param resultCriteria
     */
    List<Demand> getAllNewDemands(ResultCriteria resultCriteria);
}
