/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.dao.demand.DemandDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.service.GenericService;

import java.util.Set;

/**
 * @author Juraj Martinka
 */
public interface DemandService extends GenericService<Demand, DemandDao> {

    /**
     * Load all demands associated to the given locality (-ies).
     *
     * @see cz.poptavka.sample.dao.demand.DemandDao#getDemands(cz.poptavka.sample.domain.address.Locality...)
     *
     * @param localities
     * @return
     */
    Set<Demand> getDemands(Locality... localities);

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
     * Load all demands associated to the given category (-ies).
     *
     * @param categories
     * @return
     */
    Set<Demand> getDemands(Category... categories);

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


}
