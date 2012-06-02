package com.eprovement.poptavka.service.demand;

import com.googlecode.ehcache.annotations.Cacheable;
import com.eprovement.poptavka.dao.demand.CategoryDao;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.exception.TreeItemModificationException;
import com.eprovement.poptavka.service.GenericServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
@Transactional(readOnly = true)
public class CategoryServiceImpl extends GenericServiceImpl<Category, CategoryDao> implements  CategoryService {


    public CategoryServiceImpl(CategoryDao categoryDao) {
        setDao(categoryDao);
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "cache5h")
    public List<Category> getRootCategories() {
        return getRootCategories(null);
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "cache5h")
    public List<Category> getRootCategories(ResultCriteria resultCriteria) {
        return getDao().getRootCategories(resultCriteria);
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "cache5h")
    public Category getCategory(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        return getDao().getCategory(code);
    }

    //----------------------------------  DO NOT MODIFY LOCALITIES USING THIS SERVICE ----------------------------------


    @Override
    public Category create(Category entity) {
        throw new TreeItemModificationException();
    }

    @Override
    public Category update(Category entity) {
        throw new TreeItemModificationException();
    }

    @Override
    public void remove(Category entity) {
        throw new TreeItemModificationException();
    }

    @Override
    public void removeById(long id) {
        throw new TreeItemModificationException();
    }
}
