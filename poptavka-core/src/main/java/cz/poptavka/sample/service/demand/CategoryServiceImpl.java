package cz.poptavka.sample.service.demand;

import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.common.ResultCriteria;
import cz.poptavka.sample.dao.demand.CategoryDao;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.service.GenericServiceImpl;
import org.apache.commons.lang.StringUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
@Transactional(readOnly = true)
public class CategoryServiceImpl extends GenericServiceImpl<Category, CategoryDao> implements  CategoryService {


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


}
