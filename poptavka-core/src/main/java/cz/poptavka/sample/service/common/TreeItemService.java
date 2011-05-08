package cz.poptavka.sample.service.common;

import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.domain.common.TreeItem;

import java.util.List;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 8.2.11
 */
public interface TreeItemService {

    /**
     * @see cz.poptavka.sample.dao.common.TreeItemDao#getAllChildren(cz.poptavka.sample.domain.common.TreeItem,
     *      Class, cz.poptavka.sample.domain.common.ResultCriteria)
     */
    <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass);

    /**
     * The same as {@link #getAllChildren(cz.poptavka.sample.domain.common.TreeItem, Class)},
     * but additional criteria are applied on the result.
     *
     * @param parentNode
     * @param treeItemClass
     * @param resultCriteria additional criteria that are applied and only that result is returned, can be null
     * @param <T>
     * @return
     */
    <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass,
                                                ResultCriteria resultCriteria);


    /**
     * @see cz.poptavka.sample.dao.common.TreeItemDao#getAllChildItemsIdsRecursively(java.util.List, Class)
     */
    <T extends TreeItem> Set<Long> getAllChildItemsIdsRecursively(List<TreeItem> treeItems, Class<T> treeItemClass);


    /**
     * @see cz.poptavka.sample.dao.common.TreeItemDao#getAllLeavesIds(Class)
     */
    List<Long> getAllLeavesIds(Class<? extends TreeItem> treeItemClazz);
}
