package cz.poptavka.sample.service.common;

import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.dao.common.TreeItemDao;
import cz.poptavka.sample.domain.common.TreeItem;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * @author Juraj Martinka
 *         Date: 8.2.11
 */
@Transactional(readOnly = true)
public class TreeItemServiceImpl implements TreeItemService {

    private TreeItemDao treeItemDao;

    public void setTreeItemDao(TreeItemDao treeItemDao) {
        this.treeItemDao = treeItemDao;
    }

    /** {@inheritDoc} */
    @Override
    public <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass) {
        return getAllChildren(parentNode, treeItemClass, null);
    }

    /** {@inheritDoc} */
    @Override
    public <T extends TreeItem> List<T> getAllChildren(TreeItem parentNode, Class<T> treeItemClass,
                                                       ResultCriteria resultCriteria) {
        return this.treeItemDao.getAllChildren(parentNode, treeItemClass, resultCriteria);
    }


    /** {@inheritDoc} */
    @Override
    public <T extends TreeItem> Set<Long> getAllChildItemsIdsRecursively(List<TreeItem> treeItems,
                                                                         Class<T> treeItemClass) {
        return this.treeItemDao.getAllChildItemsIdsRecursively(treeItems, treeItemClass);
    }


    /** {@inheritDoc} */
    @Override
    public List<Long> getAllLeavesIds(Class<? extends TreeItem> treeItemClazz) {
        return treeItemDao.getAllLeavesIds(treeItemClazz);
    }
}
