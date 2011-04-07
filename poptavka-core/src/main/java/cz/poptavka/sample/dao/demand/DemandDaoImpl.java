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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Excalibur
 */
public class DemandDaoImpl extends GenericHibernateDao<Demand> implements DemandDao {

    private TreeItemDao treeItemDao;


    /** {@inheritDoc} */
    public List<Map<String, Object>> getDemandsCountForAllLocalities() {
        return  runNamedQuery("getDemandsCountForAllLocalities", Collections.EMPTY_MAP);
    }


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

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localitiesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities),
                Locality.class));
        return toSet(runNamedQuery("getDemandsForLocalities", params));
    }


    /** {@inheritDoc} */
    @Override
    public long getDemandsCount(Locality... localities) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localitiesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities),
                Locality.class));
        final long allDemandsCount = (Long) runNamedQueryForSingleResult("getDemandsCountForLocalities", params);
        return allDemandsCount;
    }

     /** {@inheritDoc} */
    @Override
    public long getDemandsCountQuick(Locality locality) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("leftBound", locality.getLeftBound());
        params.put("rightBound", locality.getRightBound());
        final long allDemandsCount = (Long) runNamedQueryForSingleResult("getDemandsCountForLocality", params);
        return allDemandsCount;
    }

    /** {@inheritDoc} */
    @Override
    public long getDemandsCountWithoutChildren(Locality locality) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("locality", locality);
        return (Long) runNamedQueryForSingleResult("getDemandsCountForLocalityWithoutChildren", params);
    }


    //------------------------------------- Categories -----------------------------------------------------------------


    @Override
    public List<Map<String, Object>> getDemandsCountForAllCategories() {
        return  runNamedQuery("getDemandsCountForAllCategories", Collections.EMPTY_MAP);
    }

    /** {@inheritDoc} */
    @Override
    public Set<Demand> getDemands(Category... categories) {
        if (categories == null || categories.length == 0 || CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoriesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
                Category.class));
        return toSet(runNamedQuery("getDemandsForCategories", params));
    }

     /** {@inheritDoc} */
    @Override
    public long getDemandsCount(Category... categories) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoriesIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
                Category.class));
        final long allDemandsCount = (Long) runNamedQueryForSingleResult("getDemandsCountForCategories", params);
        return allDemandsCount;
    }

     /** {@inheritDoc} */
    @Override
    public long getDemandsCountQuick(Category category) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("leftBound", category.getLeftBound());
        params.put("rightBound", category.getRightBound());
        final long allDemandsCount = (Long) runNamedQueryForSingleResult("getDemandsCountForCategory", params);
        return allDemandsCount;
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
