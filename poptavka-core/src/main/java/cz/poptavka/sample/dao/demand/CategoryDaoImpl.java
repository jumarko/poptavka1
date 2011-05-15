package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.demand.Category;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author Juraj Martinka
 *         Date: 6.2.11
 */
public class CategoryDaoImpl extends GenericHibernateDao<Category> implements CategoryDao {

    /** {@inheritDoc} */
    @Override
    public List<Category> getRootCategories(ResultCriteria resultCriteria) {
        final Criteria categoryCriteria = getHibernateSession().createCriteria(Category.class);
        // root categories DO NOT have a parent
        categoryCriteria.add(Restrictions.isNull("parent"));

        if (orderByNotSpecified(resultCriteria)) {
            // set implicit ordering by name
            categoryCriteria.addOrder(Order.asc("name"));
        }

        return buildResultCriteria(categoryCriteria, resultCriteria).list();
    }


    /** {@inheritDoc} */
    @Override
    public Category getCategory(String code) {
        final Map<String, Object> params = new HashMap<String, Object>();
        params.put("code", code);
        return (Category) runNamedQueryForSingleResult("getCategoryByCode", params);
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------

    private boolean orderByNotSpecified(ResultCriteria resultCriteria) {
        return resultCriteria == null || resultCriteria.getOrderByColumns() == null
                || resultCriteria.getOrderByColumns().isEmpty();
    }
}
