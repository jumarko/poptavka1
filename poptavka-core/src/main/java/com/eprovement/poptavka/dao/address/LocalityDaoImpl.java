package com.eprovement.poptavka.dao.address;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import java.util.ArrayList;
import java.util.HashMap;
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
    public Locality getLocality(Long id) {
        return (Locality) getHibernateSession().createCriteria(Locality.class)
                .add(Restrictions.eq("id", id))
                .uniqueResult();
    }
    
    public List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String nameSubString,
            LocalityType type) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("length", maxLengthExcl);
        queryParams.put("name", nameSubString);
        queryParams.put("type", type);
        List<Object[]> result = runNamedQuery(
                "getLocalitiesByMaxLength",
                queryParams);
        List<Locality> localities = new ArrayList();
        for (Object[] entry : result) {
            localities.add((Locality) entry[0]);
        }
        return localities;
    }

    public List<Locality> getLocalitiesByMinLength(int minLength, String nameSubString,
            LocalityType type) {
        final HashMap<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("length", minLength);
        queryParams.put("name", nameSubString);
        queryParams.put("type", type);
        List<Object[]> result = runNamedQuery(
                "getLocalitiesByMinLength",
                queryParams);
        List<Locality> localities = new ArrayList();
        for (Object[] entry : result) {
            localities.add((Locality) entry[0]);
        }
        return localities;
    }
}
