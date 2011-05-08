package cz.poptavka.sample.dao.address;

import cz.poptavka.sample.domain.common.ResultCriteria;
import cz.poptavka.sample.dao.GenericDao;
import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.address.LocalityType;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 5.2.11
 */
public interface LocalityDao extends GenericDao<Locality> {

    /**
     * Get locality by given code. Code must be a unique identifier!
     *
     * @param code unique code for identifying locality
     * @return
     */
    Locality getLocality(String code);

    /**
     * Get all localities of given type.
     *
     * @param localityType
     * @param resultCriteria optional additional criteria which can be applied to the localities
     * @return
     */
    List<Locality> getLocalities(LocalityType localityType, ResultCriteria resultCriteria);

}
