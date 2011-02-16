/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.dao.common.TreeItemDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.util.collection.CollectionsHelper;
import org.hibernate.Query;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Excalibur
 */
public class DemandDaoImpl extends GenericHibernateDao<Demand> implements DemandDao {

    private TreeItemDao treeItemDao;


    /**
     *  {@inheritDoc}
     * <p>
     * If some demand occurs in more than one locality it is still included ONLY ONCE in result.
     */
    @Override
    public Set<Demand> getDemands(Locality... localities) {
        if (localities == null || localities.length == 0 || CollectionsHelper.containsOnlyNulls(localities)) {
            return Collections.emptySet();
        }

        final Query getDemandsQuery = getHibernateSession().getNamedQuery("getDemandsForLocalities");
        final List<Demand> demandsList = getDemandsQuery.setParameterList("localitiesIds",
                this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities), Locality.class))
                .list();

        return CollectionsHelper.converToSet(demandsList);
    }


    /** {@inheritDoc} */
    @Override
    public long getDemandsCount(Locality... localities) {
        final Query getDemandsQuery = getHibernateSession().getNamedQuery("getDemandsCountForLocalities");
        final long allDemandsCount = (Long) getDemandsQuery.setParameterList("localitiesIds",
                this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities), Locality.class))
                .uniqueResult();

        return allDemandsCount;
    }


     /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(Category... categories) {
        if (categories == null || categories.length == 0 || CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }

        final Query getDemandsQuery = getHibernateSession().getNamedQuery("getDemandsForCategories");
        final List<Demand> demandsList = getDemandsQuery.setParameterList("categoriesIds",
                this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories), Category.class))
                .list();

        return CollectionsHelper.converToSet(demandsList);
    }

     /** {@inheritDoc} */
    @Override
    public long getDemandsCount(Category... categories) {
        final Query getDemandsQuery = getHibernateSession().getNamedQuery("getDemandsCountForCategories");
        final long allDemandsCount = (Long) getDemandsQuery.setParameterList("categoriesIds",
                this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories), Category.class))
                .uniqueResult();

        return allDemandsCount;
    }


    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }
}
