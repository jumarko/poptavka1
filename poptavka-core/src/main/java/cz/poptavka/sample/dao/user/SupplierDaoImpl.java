package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.dao.common.TreeItemDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.util.collection.CollectionsHelper;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class SupplierDaoImpl extends GenericHibernateDao<Supplier> implements SupplierDao {

    private TreeItemDao treeItemDao;


    @Override
    public long getSuppliersCount(Category... categories) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoriesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
                Category.class));
        final long allSuppliersCount = (Long) runNamedQueryForSingleResult("getSuppliersCountForCategories", params);
        return allSuppliersCount;
    }

    @Override
    public Set<Supplier> getSuppliers(Category... categories) {
        if (categories == null || categories.length == 0 || CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoriesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
                Category.class));
        return toSet(runNamedQuery("getSuppliersForCategories", params));
    }

    @Override
    public long getSuppliersCount(Locality... localities) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localitiesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities),
                Locality.class));
        final long allSuppliersCount = (Long) runNamedQueryForSingleResult("getSuppliersCountForLocalities", params);
        return allSuppliersCount;
    }


    @Override
    public Set<Supplier> getSuppliers(Locality... localities) {
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
