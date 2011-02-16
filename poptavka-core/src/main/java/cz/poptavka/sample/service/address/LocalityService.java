package cz.poptavka.sample.service.address;

import cz.poptavka.sample.dao.address.LocalityDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;
import cz.poptavka.sample.service.GenericService;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 5.2.11
 */
public interface LocalityService extends GenericService<Locality, LocalityDao> {

    /**
     * Get all localities of given type.
     *
     * @param localityType
     * @return
     */
    List<Locality> getLocalities(LocalityType localityType);

    /**
     * Get locality by given code. Code must be a unique identifier!
     *
     * @param code unique code for identifying locality
     * @return
     */
    Locality getLocality(String code);
}
