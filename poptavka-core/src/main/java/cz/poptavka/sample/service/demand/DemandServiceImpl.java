/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.demand;

import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.demand.DemandDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.service.GenericServiceImpl;

import java.util.Set;

/**
 *
 * @author Excalibur
 */
public class DemandServiceImpl extends GenericServiceImpl<Demand, DemandDao> implements DemandService {


    private DemandDao demandDao;



    /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(Locality... localities) {
        return this.demandDao.getDemands(localities);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result of this operation is cached therefore client cannot rely on the freshness of result.
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    public long getDemandsCount(Locality... localities) {
        return this.demandDao.getDemandsCount(localities);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(Category... categories) {
        return this.demandDao.getDemands(categories);
    }

    /**
     * {@inheritDoc}
     * <p>
     * The result of this operation is cached therefore client cannot rely on the freshness of result.
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    public long getDemandsCount(Category... categories) {
        return this.demandDao.getDemandsCount(categories);
    }

    //--------------------- GETTERS AND SETTERS ------------------------------------------------------------------------
    public void setDemandDao(DemandDao demandDao) {
        this.demandDao = demandDao;
    }

}
