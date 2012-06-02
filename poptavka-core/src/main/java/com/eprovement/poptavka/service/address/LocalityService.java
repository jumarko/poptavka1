package com.eprovement.poptavka.service.address;

import com.eprovement.poptavka.domain.common.ResultCriteria;
import com.eprovement.poptavka.dao.address.LocalityDao;
import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.address.LocalityType;
import com.eprovement.poptavka.service.GenericService;

import java.util.List;

/**
 * Basic Service for localities.
 * Should provide only read methods because localities storing is complex problem that must be handled
 * by stored procedure (filling of attributes level, leftBound and rightBound).
 * <p>
 *     If any method "non-read" method is called then TreeItemModificationException should be thrown.
 *
 * @see com.eprovement.poptavka.domain.common.TreeItem
 *
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
     * Same as {@link #getLocalities(com.eprovement.poptavka.domain.address.LocalityType)}
     * but additional criteria can be applied.
     * @param localityType
     * @param resultCriteria optional additional criteria which can be applied to the localities
     * @return
     */
    List<Locality> getLocalities(LocalityType localityType, ResultCriteria resultCriteria);

    /**
     * Get locality by given code. Code must be a unique identifier!
     *
     * @param code unique code for identifying locality
     * @return
     */
    Locality getLocality(String code);
}
