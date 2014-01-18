package com.eprovement.poptavka.service.demand;

import static org.apache.commons.lang.Validate.notEmpty;
import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.domain.common.ExternalSource;
import com.eprovement.poptavka.domain.common.UnknownSourceException;
import com.eprovement.poptavka.domain.demand.ExternalCategory;
import com.eprovement.poptavka.service.register.RegisterService;
import com.googlecode.ehcache.annotations.Cacheable;
import com.eprovement.poptavka.dao.demand.CategoryDao;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.demand.Category;
import com.eprovement.poptavka.exception.TreeItemModificationException;
import com.eprovement.poptavka.service.GenericServiceImpl;
import org.apache.commons.lang.Validate;
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
    private final RegisterService registerService;

    public CategoryServiceImpl(CategoryDao categoryDao, RegisterService registerService) {
        notNull(categoryDao, "categoryDao cannot be null!");
        notNull(registerService, "registerService cannot be null!");

        setDao(categoryDao);
        this.registerService = registerService;
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "categoryCache")
    public List<Category> getRootCategories() {
        LOGGER.debug("action=get_root_categories status=start");
        final List<Category> rootCategories = getRootCategories(null);
        LOGGER.debug("action=get_root_categories status=finish root_categories_number=", rootCategories.size());
        return rootCategories;
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "categoryCache")
    public List<Category> getRootCategories(ResultCriteria resultCriteria) {
        LOGGER.debug("action=get_root_categories_critiera status=start result_criteria={}", resultCriteria);
        final List<Category> rootCategories = getDao().getRootCategories(resultCriteria);
        LOGGER.debug("action=get_root_categories_critiera status=start result_criteria={} "
                + "root_categories_number=", resultCriteria, rootCategories.size());
        return rootCategories;
    }

    /** {@inheritDoc} */
    @Override
    @Cacheable(cacheName = "categoryCache")
    public Category getCategory(Long id) {
        if (id == null) {
            return null;
        }

        return getDao().getCategory(id);
    }

    @Override
    @Cacheable(cacheName = "categoryCache")
    public Category getCategoryBySicCode(String sicCode) {
        notEmpty(sicCode, "sicCode cannot be empty");
        Validate.isTrue(sicCode.length() > 1, "sicCode should have at least two charactes");
        return getDao().getCategoryBySicCode(sicCode.substring(0, 2));
    }


    @Override
    @Cacheable(cacheName = "categoryCache")
    public List<ExternalCategory> getCategoryMapping(String externalSourceName) {
        final ExternalSource source = registerService.getValue(externalSourceName, ExternalSource.class);
        if (source == null) {
            throw new UnknownSourceException(externalSourceName);
        }
        return getDao().getCategoryMapping(source);
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

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Category> getCategoriesByMaxLengthExcl(int maxLengthExcl, String nameSubstring) {
        return getDao().getCategoriesByMaxLengthExcl(maxLengthExcl, nameSubstring);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Category> getCategoriesByMinLength(int minLength, String nameSubstring) {
        return getDao().getCategoriesByMinLength(minLength, nameSubstring);
    }
}
