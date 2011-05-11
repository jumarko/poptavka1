package cz.poptavka.sample.service.address;

import com.googlecode.ehcache.annotations.Cacheable;
import cz.poptavka.sample.dao.address.LocalityDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.service.GenericServiceImpl;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Provides methods for handling addresses and localities.
 *
 * @see cz.poptavka.sample.domain.address.Address
 * @see cz.poptavka.sample.domain.address.Locality
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
    public Locality getLocality(String code) {
        return getDao().getLocality(code);
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
}
