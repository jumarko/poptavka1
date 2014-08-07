package com.eprovement.poptavka.dao.user;

import com.eprovement.poptavka.dao.common.TreeItemDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.util.collection.CollectionsHelper;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.SQLQuery;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class SupplierDaoImpl extends BusinessUserRoleDaoImpl<Supplier> implements SupplierDao {

    private TreeItemDao treeItemDao;

    @Override
    public long getSuppliersCount(Category... categories) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoriesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
            Category.class));
        return (Long) runNamedQueryForSingleResult("getSuppliersCountForCategories", params);
    }

    @Override
    public List<Map<String, Object>> getSuppliersCountForAllCategories() {
        return runNamedQuery("getSuppliersCountForAllCategories", Collections.EMPTY_MAP);
    }

    @Override
    public long getSuppliersCountQuick(Category category) {
        String sql = "select count(distinct sc.supplier_id) from Category c LEFT JOIN SUPPLIER_CATEGORY sc ON "
                + "c.id=sc.category_id where c.leftBound between :leftBound and :rightBound "
                + "and (sc.enabled=1 or sc.enabled is null)";
        SQLQuery query = getHibernateSession().createSQLQuery(sql);
        query.setParameter("leftBound", category.getLeftBound());
        query.setParameter("rightBound", category.getRightBound());
        return ((BigInteger) query.uniqueResult()).longValue();
    }

    /** {@inheritDoc} */
    @Override
    public long getSuppliersCountWithoutChildren(Category category) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("category", category);
        return (Long) runNamedQueryForSingleResult("getSuppliersCountForCategoryWithoutChildren", params);
    }

    @Override
    public Set<Supplier> getSuppliers(Category[] categories, ResultCriteria resultCriteria) {
        if (categories == null || categories.length == 0 || CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoriesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
            Category.class));
        return toSet(runNamedQuery("getSuppliersForCategories", params, resultCriteria));
    }

    @Override
    public Set<Supplier> getSuppliers(List<Category> categories, List<Locality> localities,
        ResultCriteria resultCriteria) {
        if (CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }
        if (CollectionsHelper.containsOnlyNulls(localities)) {
            return Collections.emptySet();
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryIds", this.treeItemDao.getAllChildItemsIdsRecursively(categories, Category.class));
        params.put("localityIds", this.treeItemDao.getAllChildItemsIdsRecursively(localities, Locality.class));

        return toSet(runNamedQuery("getSuppliersForCategoriesAndLocalities", params, resultCriteria));

    }

    @Override
    public Set<Supplier> getSuppliersIncludingParentsAndChildren(List<Category> categories, List<Locality> localities,
        ResultCriteria resultCriteria) {
        if (CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }
        if (CollectionsHelper.containsOnlyNulls(localities)) {
            return Collections.emptySet();
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryIds", CollectionsHelper.getCollectionOfIds(categories));
        params.put("localityIds", CollectionsHelper.getCollectionOfIds(localities));

        return toSet(runNamedQuery("getSuppliersForCategoriesAndLocalitiesIncludingParentsAndChildren", params,
            resultCriteria));
    }

    /** {@inheritDoc} */
    public List<Map<String, Object>> getSuppliersCountForAllLocalities() {
        return runNamedQuery("getSuppliersCountForAllLocalities", Collections.EMPTY_MAP);
    }

    @Override
    public long getSuppliersCount(Locality... localities) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localitiesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities),
            Locality.class));
        return (Long) runNamedQueryForSingleResult("getSuppliersCountForLocalities", params);
    }

    @Override
    public long getSuppliersCount(List<Category> categories, List<Locality> localities, ResultCriteria resultCriteria) {
        return getSuppliersCountForCategoriesAndLocalities(categories, localities, resultCriteria,
            "getSuppliersCountForCategoriesAndLocalities");
    }

    private long getSuppliersCountForCategoriesAndLocalities(List<Category> categories, List<Locality> localities,
        ResultCriteria resultCriteria, String namedQueryName) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localityIds", this.treeItemDao.getAllChildItemsIdsRecursively(localities, Locality.class));
        params.put("categoryIds", this.treeItemDao.getAllChildItemsIdsRecursively(categories, Category.class));

        return (Long) runNamedQueryForSingleResult(namedQueryName, params, resultCriteria);
    }

    @Override
    public long getSuppliersCountQuick(Locality locality) {
        String sql = "select count(distinct sl.supplier_id) from Locality loc LEFT JOIN SUPPLIER_LOCALITY sl ON "
                + "loc.id=sl.locality_id where loc.leftBound between :leftBound and :rightBound "
                + "and (sl.enabled=1 or sl.enabled is null)";
        SQLQuery query = getHibernateSession().createSQLQuery(sql);
        query.setParameter("leftBound", locality.getLeftBound());
        query.setParameter("rightBound", locality.getRightBound());
        return ((BigInteger) query.uniqueResult()).longValue();

    }

    /** {@inheritDoc} */
    @Override
    public long getSuppliersCountWithoutChildren(Locality locality) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("locality", locality);
        return (Long) runNamedQueryForSingleResult("getSuppliersCountForLocalityWithoutChildren", params);
    }

    @Override
    public Set<Supplier> getSuppliers(Locality[] localities, ResultCriteria resultCriteria) {
        if (localities == null || localities.length == 0 || CollectionsHelper.containsOnlyNulls(localities)) {
            return Collections.emptySet();
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localitiesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities),
            Locality.class));
        return toSet(runNamedQuery("getSuppliersForLocalities", params, resultCriteria));
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void incrementCategorySupplierCount(Collection<Long> categoryIds) {
        getEntityManager().createNamedQuery(Category.INCREMENT_SUPPLIER_COUNT)
            .setParameter("ids", categoryIds)
            .executeUpdate();
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void decrementCategorySupplierCount(Collection<Long> categoryIds) {
        getEntityManager().createNamedQuery(Category.DECREMENT_SUPPLIER_COUNT)
            .setParameter("ids", categoryIds)
            .executeUpdate();
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void incrementLocalitySupplierCount(Collection<Long> localityIds) {
        getEntityManager().createNamedQuery(Locality.INCREMENT_SUPPLIER_COUNT)
            .setParameter("ids", localityIds)
            .executeUpdate();
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public void decrementLocalitySupplierCount(Collection<Long> localityIds) {
        getEntityManager().createNamedQuery(Locality.DECREMENT_SUPPLIER_COUNT)
            .setParameter("ids", localityIds)
            .executeUpdate();
    }

    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }

}
