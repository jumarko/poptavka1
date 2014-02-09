package com.eprovement.poptavka.dao.address;

import static java.util.Collections.singletonMap;
import static org.apache.commons.lang.Validate.notEmpty;
import static org.apache.commons.lang.Validate.notNull;

import com.eprovement.poptavka.dao.GenericHibernateDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.enums.LocalityType;
import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import java.util.HashMap;
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

    @Override
    public List<Locality> findLocalitiesForZipCode(String zipCode) {
        notEmpty(zipCode, "zipCode should not be empty");
        return runNamedQuery("getLocalitiesByZipCode", singletonMap("zipCode", zipCode));
    }

    @Override
    public Locality findCityByName(Locality district, String cityName) {
        notNull(district, "district should not be null");
        notEmpty(cityName, "city name should not be empty");
        return (Locality) getHibernateSession().createCriteria(Locality.class)
                .add(Restrictions.eq("type", LocalityType.CITY))
                .add(Restrictions.eq("name", cityName))
                .add(Restrictions.eq("parent", district))
                .uniqueResult();
    }

    @Override
    public Locality findDistrictByName(Locality region, String districtName) {
        notNull(region, "region should not be null");
        notEmpty(districtName, "district name should not be empty");
        return (Locality) getHibernateSession().createCriteria(Locality.class)
                .add(Restrictions.eq("type", LocalityType.DISTRICT))
                .add(Restrictions.eq("name", districtName))
                .add(Restrictions.eq("parent", region))
                .uniqueResult();
    }

    @Override
    public Locality findRegionByAbbreviation(String abbreviation) {
        notEmpty(abbreviation, "abbreviation should not be empty!");
        return (Locality) runNamedQueryForSingleResult("getRegionByAbbreviation",
                singletonMap("abbreviation", abbreviation));
    }

    @Override
    public Locality findRegionByName(String name) {
        notEmpty(name, "region name should not be empty");
        return (Locality) getHibernateSession().createCriteria(Locality.class)
                .add(Restrictions.eq("type", LocalityType.REGION))
                .add(Restrictions.eq("name", name))
                .uniqueResult();
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String nameSubstring) {
        final HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("length", maxLengthExcl);
        queryParams.put("name", "%" + nameSubstring + "%");
        return runNamedQuery(
                "getLocalitiesByMaxLength",
                queryParams);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Locality> getLocalitiesByMinLength(int minLength, String nameSubstring) {
        final HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("length", minLength);
        queryParams.put("name", "%" + nameSubstring + "%");
        return runNamedQuery(
                "getLocalitiesByMinLength",
                queryParams);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String nameSubstring,
            LocalityType type) {
        final HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("length", maxLengthExcl);
        queryParams.put("name", "%" + nameSubstring + "%");
        queryParams.put("type", type);
        return runNamedQuery(
                "getLocalitiesByMaxLengthAndType",
                queryParams);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Locality> getLocalitiesByMinLength(int minLength, String nameSubstring,
            LocalityType type) {
        final HashMap<String, Object> queryParams = new HashMap<>();
        queryParams.put("length", minLength);
        queryParams.put("name", "%" + nameSubstring + "%");
        queryParams.put("type", type);
        return runNamedQuery(
                "getLocalitiesByMinLengthAndType",
                queryParams);
    }
}
