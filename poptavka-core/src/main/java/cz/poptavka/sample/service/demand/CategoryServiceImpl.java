package cz.poptavka.sample.service.demand;

import cz.poptavka.sample.dao.demand.CategoryDao;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.service.GenericServiceImpl;
import org.apache.commons.lang.StringUtils;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
public class CategoryServiceImpl extends GenericServiceImpl<Category, CategoryDao> implements  CategoryService {

    private CategoryDao categoryDao;

    /** {@inheritDoc} */
    @Override
    public Category getCategory(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        return this.categoryDao.getCategory(code);
    }


    //------------------------------------ GETTERS AND SETTERS ---------------------------------------------------------
    public void setCategoryDao(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

}
