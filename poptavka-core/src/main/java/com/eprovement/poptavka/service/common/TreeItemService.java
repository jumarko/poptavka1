package com.eprovement.poptavka.service.common;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.common.TreeItem;

import java.util.List;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 8.2.11
 */
public interface TreeItemService {

    /**
     * @see com.eprovement.poptavka.dao.common.TreeItemDao#getAllDescendants(com.eprovement.poptavka.domain.common.TreeItem,
     *      Class, com.eprovement.poptavka.domain.common.ResultCriteria)
     */
    <T extends TreeItem> List<T> getAllDescendants(TreeItem parentNode, Class<T> treeItemClass);

    /**
     * The same as {@link #getAllDescendants(com.eprovement.poptavka.domain.common.TreeItem, Class)},
     * but additional criteria are applied on the result.
     *
     * @param parentNode
     * @param treeItemClass
     * @param resultCriteria additional criteria that are applied and only that result is returned, can be null
     * @param <T>
     * @return
     */
    <T extends TreeItem> List<T> getAllDescendants(TreeItem parentNode, Class<T> treeItemClass,
                                                ResultCriteria resultCriteria);


    /**
     * @see com.eprovement.poptavka.dao.common.TreeItemDao#getAllChildItemsIdsRecursively(java.util.List, Class)
     */
    <T extends TreeItem> Set<Long> getAllChildItemsIdsRecursively(List<TreeItem> treeItems, Class<T> treeItemClass);

    /**
     * @see com.eprovement.poptavka.dao.common.TreeItemDao#getAllChildren(com.eprovement.poptavka.domain.common.TreeItem,
     *      Class, com.eprovement.poptavka.domain.common.ResultCriteria)
     */
    <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass);

    /**
     * The same as {@link #getAllChildren(com.eprovement.poptavka.domain.common.TreeItem, Class)},
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
     * @see com.eprovement.poptavka.dao.common.TreeItemDao#getAllLeavesIds(Class)
     */
    List<Long> getAllLeavesIds(Class<? extends TreeItem> treeItemClazz);
}
