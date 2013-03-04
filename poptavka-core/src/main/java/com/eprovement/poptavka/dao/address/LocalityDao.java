package com.eprovement.poptavka.dao.address;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.GenericDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.enums.LocalityType;

import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 5.2.11
 */
public interface LocalityDao extends GenericDao<Locality> {

    /**
     * Get locality by given id. Id must be a unique identifier!
     *
     * @param id unique id for identifying locality
     * @return
     */
    Locality getLocality(Long code);

    /**
     * Get all localities of given type.
     *
     * @param localityType
     * @param resultCriteria optional additional criteria which can be applied to the localities
     * @return
     */
    List<Locality> getLocalities(LocalityType localityType, ResultCriteria resultCriteria);

    /**
     * Gets a list of localities whose name is shorter than <code>maxlengthExcl</code> and
     * contains <code>nameSubString</code> and whose type is <code>type</code>
     * @param maxLengthExcl
     * @param nameLike
     * @param type
     * @return 
     */
    List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String namePrefix,
            LocalityType type);

    /**
     * Gets a list of localities whose name is the same length or longer than <code>minLength</code> and
     * contains <code>nameSubString</code> and whose type is <code>type</code>
     * @param minLength
     * @param nameLike
     * @param type
     * @return 
     */
    List<Locality> getLocalitiesByMinLength(int minLength, String namePrefix,
            LocalityType type);

}
