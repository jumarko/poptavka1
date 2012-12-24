/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.dao.demand;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.dao.common.TreeItemDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.util.collection.CollectionsHelper;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

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


    @Override
    public Set<Demand> getDemands(Category[] categories, Locality[] localities, ResultCriteria resultCriteria) {
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
                Locality.class));
        return toSet(runNamedQuery("getDemandsForCategoriesAndLocalities", params, resultCriteria));
    }


    @Override
    public long getDemandsCount(Category[] categories, Locality[] localities, ResultCriteria resultCriteria) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localityIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(localities),
                Locality.class));
        params.put("categoryIds", this.treeItemDao.getAllChildItemsIdsRecursively(Arrays.asList(categories),
                Category.class));
        return (Long) runNamedQueryForSingleResult(
                "getDemandsCountForCategoriesAndLocalities", params,
                resultCriteria);
    }


    @Override
    public List<Demand> getAllNewDemands(ResultCriteria resultCriteria) {
        final Demand newDemandExample = new Demand();

        final Criteria newDemandCriteria = getHibernateSession().createCriteria(Demand.class);
        newDemandCriteria.add(Restrictions.eq("status", DemandStatus.NEW));
        return buildResultCriteria(newDemandCriteria, resultCriteria).list();
    }

    @Override
    public long getClientDemandsWithOfferCount(Client client) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("client", client);
        return (Long) runNamedQueryForSingleResult("getClientDemandsWithOfferCount", params);
    }

    @Override
    public List<Demand> getClientDemandsWithOffer(Client client) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("client", client);
        return  runNamedQuery("getClientDemandsWithOffer", params);
    }

    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }
}
