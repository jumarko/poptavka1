package cz.poptavka.sample.dao.user;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.dao.common.TreeItemDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.util.collection.CollectionsHelper;
import org.hibernate.Query;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class SupplierDaoImpl extends GenericHibernateDao<Supplier> implements SupplierDao {

    private TreeItemDao treeItemDao;


    @Override
    public long getSuppliersCount(Category... categories) {
        final Query getSuppliersCountQuery = getHibernateSession().getNamedQuery("getSuppliersCountForCategories");
        return (Long) getSuppliersCountQuery.setParameterList("categoriesIds",
                this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories), Category.class))
                .uniqueResult();
    }

    @Override
    public Set<Supplier> getSuppliers(Category... categories) {
        if (categories == null || categories.length == 0 || CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }

        final List<Supplier> supplierList = getHibernateSession().getNamedQuery("getSuppliersForCategories")
                .setParameterList("categoriesIds",
                        this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories), Category.class))
                .list();
        return CollectionsHelper.converToSet(supplierList);
    }

    @Override
    public long getSuppliersCount(Locality... localities) {
        final Query getSuppliersCountQuery = getHibernateSession().getNamedQuery("getSuppliersCountForLocalities");
        return (Long) getSuppliersCountQuery.setParameterList("localitiesIds",
                this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities), Locality.class))
                .uniqueResult();
    }


    @Override
    public Set<Supplier> getSuppliers(Locality... localities) {
        if (localities == null || localities.length == 0 || CollectionsHelper.containsOnlyNulls(localities)) {
            return Collections.emptySet();
        }

        final List<Supplier> suppliersList = getHibernateSession().getNamedQuery("getSuppliersForLocalities")
                .setParameterList("localitiesIds",
                        this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities), Locality.class))
                .list();
        return CollectionsHelper.converToSet(suppliersList);
    }


    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }

}
