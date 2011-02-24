package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.demand.Category;

import java.util.HashMap;
import java.util.Map;


/**
 * @author Juraj Martinka
 *         Date: 6.2.11
 */
public class CategoryDaoImpl extends GenericHibernateDao<Category> implements CategoryDao {

    /** {@inheritDoc} */
    @Override
    public Category getCategory(String code) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        return (Category) runNamedQueryForSingleResult("getCategoryByCode", params);
    }

}
