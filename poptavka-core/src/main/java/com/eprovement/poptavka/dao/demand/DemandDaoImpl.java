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
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.util.collection.CollectionsHelper;
import java.math.BigInteger;

import javax.persistence.Query;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.hibernate.SQLQuery;

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
        String sql = "select count(distinct dl.demand_id) from Locality loc LEFT JOIN DEMAND_LOCALITY dl ON "
                + "loc.id=dl.locality_id LEFT JOIN Demand d on dl.demand_id=d.id where "
                + "loc.leftBound between :leftBound and :rightBound "
                + "and (dl.enabled=1 or dl.enabled is null) "
                + "and dl.demand_id=d.id and (d.status LIKE 'ACTIVE' OR d.status LIKE 'OFFERED')";
        SQLQuery query = getHibernateSession().createSQLQuery(sql);
        query.setParameter("leftBound", locality.getLeftBound());
        query.setParameter("rightBound", locality.getRightBound());
        return ((BigInteger) query.uniqueResult()).longValue();
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
        String sql = "select count(distinct dc.demand_id) from Demand d, Category c LEFT JOIN DEMAND_CATEGORY dc ON "
                + "c.id=dc.category_id where "
                + "c.leftBound between :leftBound and :rightBound "
                + "and (dc.enabled=1 or dc.enabled is null) "
                + "and dc.demand_id=d.id and (d.status LIKE 'ACTIVE' OR d.status LIKE 'OFFERED')";
        SQLQuery query = getHibernateSession().createSQLQuery(sql);
        query.setParameter("leftBound", category.getLeftBound());
        query.setParameter("rightBound", category.getRightBound());
        return ((BigInteger) query.uniqueResult()).longValue();
    }


    /** {@inheritDoc} */
    @Override
    public long getDemandsCountWithoutChildren(Category category) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("category", category);
        return (Long) runNamedQueryForSingleResult("getDemandsCountForCategoryWithoutChildren", params);
    }


    @Override
    public Set<Demand> getDemands(List<Category> categories, List<Locality> localities, ResultCriteria resultCriteria) {
        if (CollectionsHelper.containsOnlyNulls(categories)) {
            return Collections.emptySet();
        }
        if (CollectionsHelper.containsOnlyNulls(localities)) {
            return Collections.emptySet();
        }

        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("categoryIds", this.treeItemDao.getAllChildItemsIdsRecursively(categories, Category.class));
        params.put("localityIds", this.treeItemDao.getAllChildItemsIdsRecursively(localities, Locality.class));
        return toSet(runNamedQuery("getDemandsForCategoriesAndLocalities", params, resultCriteria));
    }


    @Override
    public long getDemandsCount(List<Category> categories, List<Locality> localities, ResultCriteria resultCriteria) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("localityIds", this.treeItemDao.getAllChildItemsIdsRecursively(localities, Locality.class));
        params.put("categoryIds", this.treeItemDao.getAllChildItemsIdsRecursively(categories, Category.class));
        return (Long) runNamedQueryForSingleResult(
                "getDemandsCountForCategoriesAndLocalities", params,
                resultCriteria);
    }

    @Override
    public Set<Demand> getDemandsIncludingParents(List<Category> categories, List<Locality> localities,
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

        return toSet(runNamedQuery("getDemandsForCategoriesAndLocalitiesIncludingParents", params, resultCriteria));
    }


    @Override
    public long getClientDemandsWithOfferCount(Client client) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("client", client);
        params.put("status", DemandStatus.OFFERED);
        return (Long) runNamedQueryForSingleResult("getClientDemandsWithOfferCount", params);
    }

    @Override
    public List<Demand> getClientDemandsWithOffer(Client client) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("client", client);
        params.put("status", DemandStatus.OFFERED);
        return  runNamedQuery("getClientDemandsWithOffer", params);
    }

    @Override
    public Map<Demand, Integer> getClientDemandsWithUnreadSubMsgs(BusinessUser businessUser) {
        return getUnreadSubMessagesForDemandMessageHelper(businessUser, "getClientDemandsWithUnreadSubMsgs");

    }

    /** {@inheritDoc} */
    @Override
    public long getClientDemandsCount(BusinessUser businessUser) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", businessUser);

        return ((Long) runNamedQueryForSingleResult(
                "getClientDemandsCount",
                queryParams));
    }

    @Override
    public Map<Demand, Integer> getClientOfferedDemandsWithUnreadOfferSubMsgs(BusinessUser businessUser) {
        return getUnreadSubMessagesForDemandMessageHelper(businessUser,
                "getClientOfferedDemandsWithUnreadOfferSubMsgs");
    }

    /** {@inheritDoc} */
    @Override
    public long getClientOfferedDemandsCount(BusinessUser businessUser) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", businessUser);

        return ((Long) runNamedQueryForSingleResult(
                "getClientOfferedDemands",
                queryParams));
    }

    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }

    //---------------------------------------------- HELPER METHODS ----------------------------------------------------
    /**
     * Retrieves a map of client's demands with number of their associated
     * unread sub-messages.
     *
     * @param user
     * @param queryName
     * @return map of thread demands number of their corresponding unread sub-messages
     */
    private Map<Demand, Integer> getUnreadSubMessagesForDemandMessageHelper(BusinessUser businessUser,
            String queryName) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("user", businessUser);

        List<Object[]> unread = runNamedQuery(
                queryName,
                queryParams);
        Map<Demand, Integer> unreadMap = new HashMap();
        for (Object[] entry : unread) {
            unreadMap.put((Demand) entry[0], ((Long) entry[1]).intValue());
        }
        return unreadMap;
    }

}
