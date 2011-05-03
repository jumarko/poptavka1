package cz.poptavka.sample.dao;

import com.googlecode.genericdao.dao.jpa.GeneralDAO;
import com.googlecode.genericdao.search.jpa.JPASearchProcessor;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Handy subclass of {@link com.googlecode.genericdao.dao.jpa.GeneralDAOImpl} that is used
 * for simple configuration of {@link EntityManager} (primary reason)
 * and {@link JPASearchProcessor} (secondary reason|.
 *
 * <p>
 *     This class simplifies Spring configuration.
 *
 * @author Juraj Martinka
 *         Date: 4.5.11
 */
public class DecoratedGeneralDAOImpl extends com.googlecode.genericdao.dao.jpa.GeneralDAOImpl implements GeneralDAO {


    public DecoratedGeneralDAOImpl(JPASearchProcessor searchProcessor) {
        super.setSearchProcessor(searchProcessor);
    }

    @Override
    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        super.setEntityManager(entityManager);
    }

}
