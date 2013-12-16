package com.eprovement.poptavka.service.address;

import com.eprovement.poptavka.dao.address.LocalityDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.exception.TreeItemModificationException;
import com.eprovement.poptavka.service.GenericServiceImpl;
import com.googlecode.ehcache.annotations.Cacheable;
import java.util.List;
import org.apache.commons.lang.Validate;
import org.springframework.transaction.annotation.Transactional;

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
    public Locality findCityByName(String region, String cityName) {
        Validate.notEmpty(region, "region cannot be empty!");
        Validate.notEmpty(cityName, "cityName cannot be empty!");
        return getDao().findCityByName(findRegion(region), cityName);
    }

    @Override
    @Cacheable(cacheName = "localityCache")
    public Locality findDistrictByName(String region, String districtName) {
        Validate.notEmpty(region, "region cannot be empty!");
        Validate.notEmpty(districtName, "districtName cannot be empty!");
        return getDao().findDistrictByName(findRegion(region), districtName);
    }

    @Override
    @Cacheable(cacheName = "regionCache")
    public Locality findRegion(String region) {
        Validate.notEmpty(region, "region cannot be empty!");
        final Locality regionByAbbr = getDao().findRegionByAbbreviation(region);
        if (regionByAbbr != null) {
            return regionByAbbr;
        }
        return getDao().findRegionByName(region);
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
        Validate.isTrue(locality != null, "No locality with id=" + localityId + " has been found!");
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
