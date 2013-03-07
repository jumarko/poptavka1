package com.eprovement.poptavka.service.address;

import com.googlecode.ehcache.annotations.Cacheable;
import com.eprovement.poptavka.dao.address.LocalityDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.exception.TreeItemModificationException;
import com.eprovement.poptavka.service.GenericServiceImpl;
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


    public LocalityServiceImpl(LocalityDao localityDao) {
        setDao(localityDao);
    }

    @Override
    @Cacheable(cacheName = "cache5h")
    public Locality getLocality(Long id) {
        return getDao().getLocality(id);
    }


    @Override
//    @Cacheable(cacheName = "cache5h")
    public List<Locality> getLocalities(LocalityType localityType) {
        return getDao().getLocalities(localityType, ResultCriteria.EMPTY_CRITERIA);
    }

    @Override
//    @Cacheable(cacheName = "cache5h")
    public List<Locality> getLocalities(LocalityType localityType, ResultCriteria resultCriteria) {
        return getDao().getLocalities(localityType, resultCriteria);
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

    @Override
    public List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String nameSubString,
            LocalityType type) {
        return getDao().getLocalitiesByMaxLengthExcl(maxLengthExcl, nameSubString, type);
    }

    @Override
    public List<Locality> getLocalitiesByMinLength(int minLength, String nameSubString,
            LocalityType type) {
        return getDao().getLocalitiesByMinLength(minLength, nameSubString, type);
    }
}
