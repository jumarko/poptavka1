/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.demand;

import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.demand.DemandDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.service.GenericServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Excalibur
 * @author Juraj Martinka
 */
@Transactional(readOnly = true)
public class DemandServiceImpl extends GenericServiceImpl<Demand, DemandDao> implements DemandService {


    private DemandDao demandDao;


    @Override
    @Cacheable(cacheName = "cache5h")
    public List<DemandType> getDemandTypes() {
        return demandDao.getDemandTypes();
    }

    @Override
    @Cacheable(cacheName = "cache5h")
    public DemandType getDemandType(String code) {
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "Code for demand type is empty!");
        return demandDao.getDemandType(code);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(Locality... localities) {
        return this.demandDao.getDemands(localities);
    }


    /** {@inheritDoc} */
    public Map<Locality, Long> getDemandsCountForAllLocalities() {
        final List<Map<String, Object>> demandsCountForAllLocalities = this.demandDao.getDemandsCountForAllLocalities();

        // convert to suitable Map: <locality, demandsCountForLocality>
        final Map<Locality, Long> demandsCountForLocalitiesMap =
                new HashMap<Locality, Long>(ESTIMATED_NUMBER_OF_LOCALITIES);
        for (Map<String, Object> demandsCountForLocality : demandsCountForAllLocalities) {
            demandsCountForLocalitiesMap.put((Locality) demandsCountForLocality.get("locality"),
                    (Long) demandsCountForLocality.get("demandsCount"));
        }

        return demandsCountForLocalitiesMap;
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
    public long getDemandsCountQuick(Locality locality) {
        return this.demandDao.getDemandsCountQuick(locality);
    }

    /** {@inheritDoc} */
    @Override
    public long getDemandsCountWithoutChildren(Locality locality) {
        return this.demandDao.getDemandsCountWithoutChildren(locality);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(Category... categories) {
        return this.demandDao.getDemands(categories);
    }


    /** {@inheritDoc} */
    public Map<Category, Long> getDemandsCountForAllCategories() {
        final List<Map<String, Object>> demandsCountForAllCategories = this.demandDao.getDemandsCountForAllCategories();

        // convert to suitable Map: <locality, demandsCountForLocality>
        final Map<Category, Long> demandsCountForCategoriesMap =
                new HashMap<Category, Long>(ESTIMATED_NUMBER_OF_CATEGORIES);
        for (Map<String, Object> demandsCountForCategory : demandsCountForAllCategories) {
            demandsCountForCategoriesMap.put((Category) demandsCountForCategory.get("category"),
                    (Long) demandsCountForCategory.get("demandsCount"));
        }

        return demandsCountForCategoriesMap;
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

    /** {@inheritDoc} */
    @Override
    public long getDemandsCountQuick(Category category) {
        return this.demandDao.getDemandsCountQuick(category);
    }

    /** {@inheritDoc} */
    @Override
    public long getDemandsCountWithoutChildren(Category category) {
        return this.demandDao.getDemandsCountWithoutChildren(category);
    }

    //--------------------- GETTERS AND SETTERS ------------------------------------------------------------------------
    public void setDemandDao(DemandDao demandDao) {
        this.demandDao = demandDao;
    }

}
