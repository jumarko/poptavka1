/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;

import java.util.Set;

/**
 *
 * @author Excalibur
 */
public interface DemandDao extends GenericDao<Demand> {

    /**
     * Load all demands for given localities. This methods works recursively, that means that really ALL demands
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
     * @param localities
     * @return all demands that (directly OR indirectly) belongs to the some from given <code>localities</code>.
     */
    Set<Demand> getDemands(Locality... localities);

    /**
     * Get count of ALL demands associated to the some locality from given <code>localities</code>
     *
     * <p>
     * See {@link #getDemands(cz.poptavka.sample.domain.address.Locality...)} for further explanation.
     *
     * @param localities
     * @return
     */
    long getDemandsCount(Locality... localities);


    /**
     * Load all demands for given categories. This methods works recursively, that means that really ALL demands
     * for given categories are returned - NOT ONLY demands directly assigned to those categories!
     *
     * @param categories
     * @return all demands that (directly OR indirectly) belongs to the some from given <code>localities</code>.
     *
     * @see #getDemands(cz.poptavka.sample.domain.address.Locality...)
     */
    Set<Demand> getDemands(Category... categories);

    /**
     * Get count of ALL demands associated to the some category from given <code>categories</code>.
     *
     * @param categories
     * @return
     *
     * @see #getDemands(cz.poptavka.sample.domain.address.Locality...)
     */
    long getDemandsCount(Category... categories);
}
