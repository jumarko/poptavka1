package cz.poptavka.sample.dao.common;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.common.TreeItem;
import cz.poptavka.sample.util.collection.CollectionsHelper;
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
    public <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass) {
        final Criteria allChildrenCriteria = getHibernateSession().createCriteria(treeItemClass);
        if (parentNode != null) {
            // only descendants of specified parent
            allChildrenCriteria.add(Restrictions.between("leftBound",
                parentNode.getLeftBound() + 1,
                parentNode.getRightBound() - 1));
        }
        return allChildrenCriteria.list();
    }


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

        // for all tree items we want to get only their IDs for using them in native SQL query or somewhere else
        return getTreeItemsIds(allTreeItemsCriteria.list());
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
