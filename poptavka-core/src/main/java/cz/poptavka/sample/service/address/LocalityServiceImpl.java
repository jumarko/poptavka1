package cz.poptavka.sample.service.address;

import cz.poptavka.sample.dao.address.LocalityDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.service.GenericServiceImpl;

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
public class LocalityServiceImpl extends GenericServiceImpl<Locality, LocalityDao> implements LocalityService {

    private LocalityDao localityDao;

    public void setLocalityDao(LocalityDao localityDao) {
        this.localityDao = localityDao;
    }

    @Override
    public Locality getLocality(String code) {
        return this.localityDao.getLocality(code);
    }

    @Override
    public List<Locality> getLocalities(LocalityType localityType) {
        return this.localityDao.getLocalities(localityType);
    }
}
