package com.eprovement.poptavka.dao.common;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.common.TreeItem;
import com.eprovement.poptavka.util.collection.CollectionsHelper;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 8.2.11
 */
public class TreeItemDaoImpl extends GenericHibernateDao<TreeItem> implements TreeItemDao {

    /** {@inheritDoc} */
    @Override
    public <T extends TreeItem> List<T> getAllDescendants(TreeItem parentNode, Class<T> treeItemClass,
                                                       ResultCriteria resultCriteria) {
        final Criteria allDescendantsCriteria = getHibernateSession().createCriteria(treeItemClass);
        if (parentNode != null) {
            // only descendants of specified parent
            allDescendantsCriteria.add(Restrictions.between("leftBound",
                parentNode.getLeftBound() + 1,
                parentNode.getRightBound() - 1));
        }

        return buildResultCriteria(allDescendantsCriteria, resultCriteria).list();
    }


    @Override
    /** {@inheritDoc} */
    public <T extends TreeItem> Set<Long> getAllChildItemsIdsRecursively(List<? extends TreeItem> treeItems,
                                                                         Class<T> treeItemClass) {
        if (treeItems == null || treeItems.isEmpty() || CollectionsHelper.containsOnlyNulls(treeItems)) {
            return Collections.emptySet();
        }

        // get all child tree items
        final Disjunction fromSomeTreeItemDisjunction = Restrictions.disjunction();
        for (TreeItem treeItem : treeItems) {
            if (treeItem != null) {
                fromSomeTreeItemDisjunction.add(Restrictions.between("leftBound",
                        treeItem.getLeftBound(), treeItem.getRightBound()));
            }
        }
        final Criteria allTreeItemsCriteria = getHibernateSession().createCriteria(treeItemClass);
        allTreeItemsCriteria.add(fromSomeTreeItemDisjunction);

        // For all tree items we want to get only their IDs for using them in native SQL query or somewhere else.
        // Apply also additional criteria if required.
        return getTreeItemsIds(allTreeItemsCriteria.list());
    }

    /** {@inheritDoc} */
    @Override
    public <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass,
                                                       ResultCriteria resultCriteria) {
        final Criteria allChildrenCriteria = getHibernateSession().createCriteria(treeItemClass);
        if (parentNode != null) {
            // only descendants of specified parent
            allChildrenCriteria.add(Restrictions.between("leftBound",
                parentNode.getLeftBound() + 1,
                parentNode.getRightBound() - 1));
            allChildrenCriteria.add(Restrictions.eq("level",
                    parentNode.getLevel() + 1));
        } else {
            allChildrenCriteria.add(Restrictions.eq("level", 0));
        }

        return buildResultCriteria(allChildrenCriteria, resultCriteria).list();
    }

    /** {@inheritDoc} */
    @Override
    public List<Long> getAllLeavesIds(Class<? extends TreeItem> treeItemClazz) {
        final String hql = "select t.id from " + treeItemClazz.getSimpleName() + " t where "
                         + "   not exists (from " + treeItemClazz.getSimpleName() + " t2 where t2.parent = t)";
        return createQuery(hql).getResultList();
    }

    //------------------------------------------- HELPER METHODS -------------------------------------------------------
    private Set<Long> getTreeItemsIds(List<TreeItem> allTreeItems) {
        final Set<Long> allTreeItemsIds = new HashSet<Long>();
        for (TreeItem treeItem : allTreeItems) {
            if (treeItem != null && treeItem.getId() != null) {
                allTreeItemsIds.add(treeItem.getId());
            }
        }
        return allTreeItemsIds;
    }

}
