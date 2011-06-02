/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.dao.common.TreeItemDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.util.collection.CollectionsHelper;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Excalibur
 * @author Juraj Martinka
 */
public class DemandDaoImpl extends GenericHibernateDao<Demand> implements DemandDao {

    private TreeItemDao treeItemDao;


    /** {@inheritDoc} */
    public List<Map<String, Object>> getDemandsCountForAllLocalities() {
        return  runNamedQuery("getDemandsCountForAllLocalities", Collections.EMPTY_MAP);
    }


    /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(Locality[] localities, ResultCriteria resultCriteria) {
        if (localities == null || localities.length == 0 || CollectionsHelper.containsOnlyNulls(localities)) {
            return Collections.emptySet();
        }

        Query query = getEntityManager().createNamedQuery("getDemandsForLocalities");
        query.setParameter("localitiesIds",
                this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities), Locality.class));

        return toSet(applyResultCriteria(query, resultCriteria).getResultList());
    }

    /** {@inheritDoc} */
    @Override
    public long getDemandsCount(Locality... localities) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localitiesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities),
                Locality.class));
        return (Long) runNamedQueryForSingleResult("getDemandsCountForLocalities", params);
    }

     /** {@inheritDoc} */
    @Override
    public long getDemandsCountQuick(Locality locality) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("leftBound", locality.getLeftBound());
        params.put("rightBound", locality.getRightBound());
        return (Long) runNamedQueryForSingleResult("getDemandsCountForLocality", params);
    }

    /** {@inheritDoc} */
    @Override
    public long getDemandsCountWithoutChildren(Locality locality) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("locality", locality);
        return (Long) runNamedQueryForSingleResult("getDemandsCountForLocalityWithoutChildren", params);
    }

    @Override
    public long getAllDemandsCount() {
        return (Long) runNamedQueryForSingleResult("getAllDemandsCount", Collections.EMPTY_MAP);
    }

    //------------------------------------- Categories -----------------------------------------------------------------


    @Override
    public List<Map<String, Object>> getDemandsCountForAllCategories() {
        return  runNamedQuery("getDemandsCountForAllCategories", Collections.EMPTY_MAP);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(Category[] categories, ResultCriteria resultCriteria) {
        if (categories == null || categories.length == 0 || CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }

        Query query = getEntityManager().createNamedQuery("getDemandsForCategories");
        query.setParameter("categoriesIds",
                this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories), Category.class));

        return toSet(applyResultCriteria(query, resultCriteria).getResultList());
    }

     /** {@inheritDoc} */
    @Override
    public long getDemandsCount(Category... categories) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoriesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
                Category.class));
        return (Long) runNamedQueryForSingleResult("getDemandsCountForCategories", params);
    }

     /** {@inheritDoc} */
    @Override
    public long getDemandsCountQuick(Category category) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("leftBound", category.getLeftBound());
        params.put("rightBound", category.getRightBound());
        return (Long) runNamedQueryForSingleResult("getDemandsCountForCategory", params);
    }


    /** {@inheritDoc} */
    @Override
    public long getDemandsCountWithoutChildren(Category category) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("category", category);
        return (Long) runNamedQueryForSingleResult("getDemandsCountForCategoryWithoutChildren", params);
    }

    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }
}
