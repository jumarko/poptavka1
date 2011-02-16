package cz.poptavka.sample.dao.demand;

import cz.poptavka.sample.dao.GenericHibernateDao;
import cz.poptavka.sample.domain.demand.Category;


/**
 * @author Juraj Martinka
 *         Date: 6.2.11
 */
public class CategoryDaoImpl extends GenericHibernateDao<Category> implements CategoryDao {

    /** {@inheritDoc} */
    @Override
    public Category getCategory(String code) {
        return (Category) getHibernateSession().getNamedQuery("getCategoryByCode")
                .setParameter("code", code)
                .uniqueResult();
    }

}
