package com.eprovement.poptavka.dao.user;

import com.eprovement.poptavka.dao.common.TreeItemDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.util.collection.CollectionsHelper;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
        return  runNamedQuery("getSuppliersCountForAllCategories", Collections.EMPTY_MAP);
    }

    @Override
    public long getSuppliersCountQuick(Category category) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("leftBound", category.getLeftBound());
        params.put("rightBound", category.getRightBound());
        return (Long) runNamedQueryForSingleResult("getSuppliersCountForCategory", params);
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
        return  runNamedQuery("getSuppliersCountForAllLocalities", Collections.EMPTY_MAP);
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
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("leftBound", locality.getLeftBound());
        params.put("rightBound", locality.getRightBound());
        return (Long) runNamedQueryForSingleResult("getSuppliersCountForLocality", params);
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





    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }

}
