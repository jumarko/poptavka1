package com.eprovement.poptavka.dao.address;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.address.LocalityType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 5.2.11
 */
public class LocalityDaoImpl extends GenericHibernateDao<Locality> implements LocalityDao {

    @Override
    @SuppressWarnings("unchecked")
    public List<Locality> getLocalities(LocalityType localityType, ResultCriteria resultCriteria) {
        final Criteria entityCriteria = getHibernateSession().createCriteria(Locality.class)
                .add(Restrictions.eq("type", localityType));
        return buildResultCriteria(entityCriteria, resultCriteria).list();
    }

    @Override
    public Locality getLocality(String code) {
        return (Locality) getHibernateSession().createCriteria(Locality.class)
                .add(Restrictions.eq("code", code))
                .uniqueResult();
    }
}
