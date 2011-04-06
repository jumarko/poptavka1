package cz.poptavka.sample.service.common;

import cz.poptavka.sample.domain.common.TreeItem;

import java.util.List;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 8.2.11
 */
public interface TreeItemService {

    /**
     * @see cz.poptavka.sample.dao.common.TreeItemDao#getAllChildren(cz.poptavka.sample.domain.common.TreeItem, Class)
     */
    <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass);


    /**
     * @see cz.poptavka.sample.dao.common.TreeItemDao#getAllChildItemsIdsRecursively(java.util.List, Class)
     */
    <T extends TreeItem> Set<Long> getAllChildItemsIdsRecursively(List<TreeItem> treeItems, Class<T> treeItemClass);


    /** @see cz.poptavka.sample.dao.common.TreeItemDao#getAllLeavesIds() */
    List<Long> getAllLeavesIds(Class<? extends TreeItem> treeItemClazz);
}
