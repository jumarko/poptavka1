package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.demand.Category;

/**
 * @author Juraj Martinka
 *         Date: 6.2.11
 */
public interface CategoryDao extends GenericDao<Category> {

    /**
     * Load category object by unique code.
     *
     * @param code unique code representing category
     * @return category with given code or null if no such category exists.
     */
    Category getCategory(String code);
}
