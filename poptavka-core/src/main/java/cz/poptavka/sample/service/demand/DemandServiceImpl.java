/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.service.demand;

import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.demand.DemandDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.service.GenericServiceImpl;
import cz.poptavka.sample.service.ResultProvider;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Excalibur
 * @author Juraj Martinka
 */
public class DemandServiceImpl extends GenericServiceImpl<Demand, DemandDao> implements DemandService {


    public DemandServiceImpl(DemandDao demandDao) {
        setDao(demandDao);
    }


    @Override
    @Cacheable(cacheName = "cache5h")
    @Transactional(readOnly = true)
    public List<DemandType> getDemandTypes() {
        return getDao().getDemandTypes();
    }

    @Override
    @Cacheable(cacheName = "cache5h")
    @Transactional(readOnly = true)
    public DemandType getDemandType(String code) {
        Preconditions.checkArgument(StringUtils.isNotBlank(code), "Code for demand type is empty!");
        return getDao().getDemandType(code);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemands(Locality... localities) {
        return getDemands(ResultCriteria.EMPTY_CRITERIA, localities);
    }


    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemands(final ResultCriteria resultCriteria, final Locality... localities) {
        final ResultProvider<Demand> demandProvider = new ResultProvider<Demand>(resultCriteria) {
            @Override
            public Collection<Demand> getResult() {
                return DemandServiceImpl.this.getDao().getDemands(localities, getResultCriteria());
            }
        };

        return new LinkedHashSet<Demand>(applyOrderByCriteria(demandProvider, resultCriteria));
    }


    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public Map<Locality, Long> getDemandsCountForAllLocalities() {
        final List<Map<String, Object>> demandsCountForAllLocalities = this.getDao().getDemandsCountForAllLocalities();

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
    @Transactional(readOnly = true)
    public long getDemandsCount(Locality... localities) {
        return this.getDao().getDemandsCount(localities);
    }


    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountQuick(Locality locality) {
        return this.getDao().getDemandsCountQuick(locality);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountWithoutChildren(Locality locality) {
        return this.getDao().getDemandsCountWithoutChildren(locality);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public Set<Demand> getDemands(Category... categories) {
        return getDemands(ResultCriteria.EMPTY_CRITERIA, categories);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(ResultCriteria resultCriteria, Category... categories) {
        return this.getDao().getDemands(categories, resultCriteria);
    }

    /** {@inheritDoc} */
    @Transactional(readOnly = true)
    public Map<Category, Long> getDemandsCountForAllCategories() {
        final List<Map<String, Object>> demandsCountForAllCategories = this.getDao().getDemandsCountForAllCategories();

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
    @Transactional(readOnly = true)
    public long getDemandsCount(Category... categories) {
        return this.getDao().getDemandsCount(categories);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountQuick(Category category) {
        return this.getDao().getDemandsCountQuick(category);
    }

    /** {@inheritDoc} */
    @Override
    @Transactional(readOnly = true)
    public long getDemandsCountWithoutChildren(Category category) {
        return this.getDao().getDemandsCountWithoutChildren(category);
    }

}
