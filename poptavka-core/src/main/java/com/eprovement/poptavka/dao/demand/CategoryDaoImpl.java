package com.eprovement.poptavka.dao.demand;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.demand.Category;
import org.apache.commons.lang.Validate;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import java.util.Collections;
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
    public Category getCategory(Long id) {
        final Map<String, Object> params = new HashMap<>();
        params.put("id", id);
        return (Category) runNamedQueryForSingleResult("getCategoryById", params);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Category> getCategoriesByMaxLengthExcl(int maxLengthExcl, String nameSubstring) {
        final HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("length", maxLengthExcl);
        queryParams.put("name", "%" + nameSubstring + "%");
        return runNamedQuery(
                "getCategoriesByMaxLength",
                queryParams);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Category> getCategoriesByMinLength(int minLength, String nameSubstring) {
        final HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("length", minLength);
        queryParams.put("name", "%" + nameSubstring + "%");
        return runNamedQuery(
                "getCategoriesByMinLength",
                queryParams);
    }

    @Override
    public Category getCategoryBySicCode(String sicCode) {
        Validate.notEmpty(sicCode, "sicCode should not be empty!");
        return (Category) runNamedQueryForSingleResult("getCategoryBySicCode",
                Collections.singletonMap("sicCode", sicCode));
    }

    //---------------------------------------------- HELPER METHODS ---------------------------------------------------

    private boolean orderByNotSpecified(ResultCriteria resultCriteria) {
        return resultCriteria == null || resultCriteria.getOrderByColumns() == null
                || resultCriteria.getOrderByColumns().isEmpty();
    }
}
