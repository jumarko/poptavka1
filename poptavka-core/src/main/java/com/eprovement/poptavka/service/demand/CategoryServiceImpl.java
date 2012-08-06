package com.eprovement.poptavka.service.demand;

import com.googlecode.ehcache.annotations.Cacheable;
import com.eprovement.poptavka.dao.demand.CategoryDao;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.exception.TreeItemModificationException;
import com.eprovement.poptavka.service.GenericServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
@Transactional(readOnly = true)
public class CategoryServiceImpl extends GenericServiceImpl<Category, CategoryDao> implements  CategoryService {

    private static final Logger LOGGER = LoggerFactory.getLogger(CategoryServiceImpl.class);

    public CategoryServiceImpl(CategoryDao categoryDao) {
        setDao(categoryDao);
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "cache5h")
    public List<Category> getRootCategories() {
        LOGGER.debug("action=get_root_categories status=start");
        final List<Category> rootCategories = getRootCategories(null);
        LOGGER.debug("action=get_root_categories status=finish root_categories_number=", rootCategories.size());
        return rootCategories;
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "cache5h")
    public List<Category> getRootCategories(ResultCriteria resultCriteria) {
        LOGGER.debug("action=get_root_categories_critiera status=start result_criteria={}", resultCriteria);
        final List<Category> rootCategories = getDao().getRootCategories(resultCriteria);
        LOGGER.debug("action=get_root_categories_critiera status=start result_criteria={} "
                + "root_categories_number=", resultCriteria, rootCategories.size());
        return rootCategories;
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
