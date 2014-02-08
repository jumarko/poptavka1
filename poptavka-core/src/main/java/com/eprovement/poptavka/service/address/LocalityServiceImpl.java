package com.eprovement.poptavka.service.address;

import static org.apache.commons.lang.Validate.isTrue;
import static org.apache.commons.lang.Validate.notEmpty;

import com.eprovement.poptavka.dao.address.LocalityDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.exception.TreeItemModificationException;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.googlecode.ehcache.annotations.Cacheable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provides methods for handling addresses and localities.
 *
 * @see com.eprovement.poptavka.domain.address.Address
 * @see com.eprovement.poptavka.domain.address.Locality
 *
 * @author Juraj Martinka
 *         Date: 5.2.11
 */
@Transactional(readOnly = true)
public class LocalityServiceImpl extends GenericServiceImpl<Locality, LocalityDao> implements LocalityService {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalityServiceImpl.class);


    public LocalityServiceImpl(LocalityDao localityDao) {
        setDao(localityDao);
    }

    @Override
    @Cacheable(cacheName = "localityCache")
    public Locality getLocality(Long id) {
        return getDao().getLocality(id);
    }


    @Override
    @Cacheable(cacheName = "localityCache")
    public Locality findCityByZipCode(String cityName, String zipCode) {
        notEmpty(cityName, "cityName should not be empty!");
        notEmpty(zipCode, "zipCode should not be empty!");

        final List<Locality> localitiesForZipCode = getDao().findLocalitiesForZipCode(zipCode);
        for (Locality locality : localitiesForZipCode) {
            if (cityName.equals(locality.getName())) {
                // found matching city - this should be the only one (we rely on DB data consistency)
                return locality;
            }
        }

        LOGGER.info("action=find_city_by_zipcode status=not_found city={}, zipcode={}", cityName, zipCode);
        return null;
    }

    @Override
    @Cacheable(cacheName = "localityCache")
    public Locality findCityByName(String region, String district, String city) {
        notEmpty(region, "region cannot be empty!");
        notEmpty(district, "district cannot be empty!");
        notEmpty(city, "city cannot be empty!");
        final Locality d = findDistrictByName(region, district);
        if (d == null) {
            return null;
        }
        final Locality c = getDao().findCityByName(d, city);
        if (c == null) {
            LOGGER.info("No city has been found for region_name={} district_name={} city_name={}",
                    region, district, city);
            return null;
        }
        return c;
    }

    @Override
    @Cacheable(cacheName = "localityCache")
    public Locality findDistrictByName(String region, String district) {
        notEmpty(region, "region cannot be empty!");
        notEmpty(district, "district cannot be empty!");
        final Locality r = findRegion(region);
        if (r == null) {
            return null;
        }
        final Locality d = getDao().findDistrictByName(r, district);
        if (d == null) {
            LOGGER.info("No district has been found for region_name={} district_name={}", region, district);
            return null;
        }
        return d;
    }

    @Override
    @Cacheable(cacheName = "regionCache")
    public Locality findRegion(String region) {
        notEmpty(region, "region cannot be empty!");
        final Locality regionByAbbr = getDao().findRegionByAbbreviation(region);
        if (regionByAbbr != null) {
            return regionByAbbr;
        }
        final Locality r = getDao().findRegionByName(region);
        if (r == null) {
            LOGGER.info("No region has been found for region_name={}", region);
            return null;
        }
        return r;
    }

    @Override
    @Cacheable(cacheName = "localityCache")
    public List<Locality> getLocalities(LocalityType localityType) {
        return getDao().getLocalities(localityType, ResultCriteria.EMPTY_CRITERIA);
    }

    @Override
    @Cacheable(cacheName = "localityCache")
    public List<Locality> getLocalities(LocalityType localityType, ResultCriteria resultCriteria) {
        return getDao().getLocalities(localityType, resultCriteria);
    }


    @Override
    @Cacheable(cacheName = "localityCache")
    public List<Locality> getSubLocalities(long localityId) {
        final Locality locality = getLocality(localityId);
        isTrue(locality != null, "No locality with id=" + localityId + " has been found!");
        return locality.getChildren();
    }


    //----------------------------------  DO NOT MODIFY LOCALITIES USING THIS SERVICE ----------------------------------


    @Override
    public Locality create(Locality entity) {
        throw new TreeItemModificationException();
    }

    @Override
    public Locality update(Locality entity) {
        throw new TreeItemModificationException();
    }

    @Override
    public void remove(Locality entity) {
        throw new TreeItemModificationException();
    }

    @Override
    public void removeById(long id) {
        throw new TreeItemModificationException();
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String nameSubstring) {
        return getDao().getLocalitiesByMaxLengthExcl(maxLengthExcl, nameSubstring);
    }

    /**
     *  {@inheritDoc}
     */
    @Override
    public List<Locality> getLocalitiesByMinLength(int minLength, String nameSubstring) {
        return getDao().getLocalitiesByMinLength(minLength, nameSubstring);
    }

    @Override
    public List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String nameSubstring,
            LocalityType type) {
        return getDao().getLocalitiesByMaxLengthExcl(maxLengthExcl, nameSubstring, type);
    }

    @Override
    public List<Locality> getLocalitiesByMinLength(int minLength, String nameSubstring,
            LocalityType type) {
        return getDao().getLocalitiesByMinLength(minLength, nameSubstring, type);
    }
}
