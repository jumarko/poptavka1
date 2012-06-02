package com.eprovement.poptavka.dao.address;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.address.LocalityType;

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
