package cz.poptavka.sample.service.user;

import com.google.common.base.Preconditions;
import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.user.SupplierDao;
import cz.poptavka.sample.dao.user.SupplierFilter;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.service.GeneralService;
import cz.poptavka.sample.service.demand.DemandService;
import org.apache.commons.collections.CollectionUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class SupplierServiceImpl extends BusinessUserRoleServiceImpl<Supplier, SupplierDao> implements SupplierService {


    public SupplierServiceImpl(GeneralService generalService, SupplierDao supplierDao) {
        super(generalService);
        setDao(supplierDao);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Supplier> getSuppliers(Locality... localities) {
        return getSuppliers(ResultCriteria.EMPTY_CRITERIA, localities);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Supplier> getSuppliers(ResultCriteria resultCriteria, Locality... localities) {
        return this.getDao().getSuppliers(localities, resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Supplier> getSuppliers(ResultCriteria resultCriteria,
                                      Category[] categories, Locality[] localities) {
        return this.getDao().getSuppliers(categories, localities,
                resultCriteria);
    }


    /**
     * {@inheritDoc}
     */
    public Map<Locality, Long> getSuppliersCountForAllLocalities() {
        final List<Map<String, Object>> suppliersCountForAllLocalities =
                this.getDao().getSuppliersCountForAllLocalities();

        // convert to suitable Map: <locality, suppliersCountForLocality>
        final Map<Locality, Long> suppliersCountForLocalitiesMap =
                new HashMap<Locality, Long>(DemandService.ESTIMATED_NUMBER_OF_LOCALITIES);
        for (Map<String, Object> suppliersCountForLocality : suppliersCountForAllLocalities) {
            suppliersCountForLocalitiesMap.put((Locality) suppliersCountForLocality.get("locality"),
                    (Long) suppliersCountForLocality.get("suppliersCount"));
        }

        return suppliersCountForLocalitiesMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSuppliersCount(Locality... localities) {
        return this.getDao().getSuppliersCount(localities);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSuppliersCountQuick(Locality locality) {
        return this.getDao().getSuppliersCountQuick(locality);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSuppliersCountWithoutChildren(Locality locality) {
        return this.getDao().getSuppliersCountWithoutChildren(locality);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Supplier> getSuppliers(Category... categories) {
        return getSuppliers(ResultCriteria.EMPTY_CRITERIA, categories);
    }

    @Override
    public Set<Supplier> getSuppliers(ResultCriteria resultCriteria, Category... categories) {
        return this.getDao().getSuppliers(categories, resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    public Map<Category, Long> getSuppliersCountForAllCategories() {
        final List<Map<String, Object>> suppliersCountForAllCategories =
                this.getDao().getSuppliersCountForAllCategories();

        // convert to suitable Map: <locality, suppliersCountForLocality>
        final Map<Category, Long> suppliersCountForCategoriesMap =
                new HashMap<Category, Long>(DemandService.ESTIMATED_NUMBER_OF_CATEGORIES);
        for (Map<String, Object> suppliersCountForCategory : suppliersCountForAllCategories) {
            suppliersCountForCategoriesMap.put((Category) suppliersCountForCategory.get("category"),
                    (Long) suppliersCountForCategory.get("suppliersCount"));
        }

        return suppliersCountForCategoriesMap;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Cacheable(cacheName = "cache5min")
    public long getSuppliersCount(Category... categories) {
        return this.getDao().getSuppliersCount(categories);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSuppliersCount(Category[] categories, Locality[] localities) {
        return this.getDao().getSuppliersCount(categories, localities, null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSuppliersCount(Category[] categories, Locality[] localities,
                                  ResultCriteria resultCriteria) {
        return this.getDao().getSuppliersCount(categories, localities,
                resultCriteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSuppliersCountQuick(Category category) {
        return this.getDao().getSuppliersCountQuick(category);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public long getSuppliersCountWithoutChildren(Category category) {
        return this.getDao().getSuppliersCountWithoutChildren(category);
    }


    @Override
    public Collection<Supplier> getSuppliers(SupplierFilter supplierFilter, ResultCriteria resultCriteria) {

        Preconditions.checkNotNull(supplierFilter);
        final Set<Supplier> demandsForCategories = getSuppliers(resultCriteria, supplierFilter.getDemandCategories()
                .toArray(new Category[supplierFilter.getDemandCategories().size()]));
        final Set<Supplier> demandsForLocalities = getSuppliers(resultCriteria, supplierFilter.getDemandLocalities()
                .toArray(new Locality[supplierFilter.getDemandLocalities().size()]));

        switch (supplierFilter.getFilterOperator()) {
            case AND:
                return CollectionUtils.intersection(demandsForCategories, demandsForLocalities);
            case OR:
                return CollectionUtils.union(demandsForCategories, demandsForLocalities);
            default:
                return Collections.emptySet();
        }

        // TODO check existing implementaion and if necessary implement new method in dao to get better performance
    }

}
