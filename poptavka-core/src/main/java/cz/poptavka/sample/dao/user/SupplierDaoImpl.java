package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.common.TreeItemDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.util.collection.CollectionsHelper;

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
        return toSet(runNamedQuery("getSuppliersForCategories", params));
    }

    @Override
    public Set<Supplier> getSuppliers(Category[] categories, Locality[] localities, ResultCriteria resultCriteria) {
        if (categories == null || categories.length == 0 || CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }
        if (localities == null || localities.length == 0 || CollectionsHelper.containsOnlyNulls(localities)) {
            return Collections.emptySet();
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
                Category.class));
        params.put("localityIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities),
                Category.class));
        return toSet(runNamedQuery("getSuppliersForCategoriesAndLocalities", params));
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
        return toSet(runNamedQuery("getSuppliersForLocalities", params));
    }





    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }

}
