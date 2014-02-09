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
     * @return locality with given id or null if no such locality exists
     */
    Locality getLocality(Long id);

    /**
     * Find all localities matching given {@code zipCode}.
     *
     * @param zipCode zip code in a valid format
     * @return all localities matching given zip code or empty list
     */
    List<Locality> findLocalitiesForZipCode(String zipCode);

    /**
     * Finds city by its parent district and city name.
     * @param district parent district
     * @param cityName name of city itself
     * @return city with given name or null if no such city has been found
     * @throws org.hibernate.NonUniqueResultException if more than one city has been found
     * @throws IllegalArgumentException if district is null or {@code cityName} is empty
     */
    Locality findCityByName(Locality district, String cityName);

    /**
     * Finds district by its region and district name.
     * @param region parent region
     * @param districtName name of district (County in USA) itself
     * @return district with given name or null if no such district has been found
     * @throws org.hibernate.NonUniqueResultException if more than one district has been found
     * @throws IllegalArgumentException if region is null or {@code districtName} is empty
     */
    Locality findDistrictByName(Locality region, String districtName);

    /**
     * Finds region by its abbreviation.
     * Typically (in USA) each state has its two-letter abbreviation.
     * @param abbreviation region abbreviation
     * @return region matching giving abbreviation or null if no such region exists.
     */
    Locality findRegionByAbbreviation(String abbreviation);

    /**
     * Finds region by its name.
     * @param region region name
     * @return region matching giving name or null if no such region exists.
     */
    Locality findRegionByName(String region);

    /**
     * Get all localities of given type.
     *
     * @param localityType type of locality
     * @param resultCriteria optional additional criteria which can be applied to the localities
     * @return localities of given type filtered further by given criteria
     */
    List<Locality> getLocalities(LocalityType localityType, ResultCriteria resultCriteria);

    /**
     * Gets a list of localities whose name is shorter than <code>maxlengthExcl</code> and
     * contains <code>nameSubstring</code>.
     * @param maxLengthExcl all <code>Locality</code>-ies' names returned must be shorter than the given length
     * @param nameSubstring a <code>String</code> that all the localities' names must contain
     * @return a <code>List<code> of localities satisfying criteria
     */
    List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String nameSubstring);

    /**
     * Gets a list of localities whose name is the same length or longer than <code>minLength</code> and
     * contains <code>nameSubstring</code>.
     * @param minLength all <code>Locality</code>-ies' names returned must be at least of the given length
     * @param nameSubstring a <code>String</code> that all the localities' names must contain
     * @return a <code>List<code> of localities satisfying criteria
     */
    List<Locality> getLocalitiesByMinLength(int minLength, String nameSubstring);

    /**
     * Gets a list of localities whose name is shorter than <code>maxlengthExcl</code> and
     * contains <code>nameSubstring</code> and whose type is <code>type</code>.
     * @param maxLengthExcl all <code>Locality</code>-ies' names returned must be shorter than the given length
     * @param nameSubstring a <code>String</code> that all the localities' names must contain
     * @param type just <code>Locality</code>-ies of the given type will be returned
     * @return a <code>List<code> of localities satisfying criteria
     */
    List<Locality> getLocalitiesByMaxLengthExcl(int maxLengthExcl, String nameSubstring,
            LocalityType type);

    /**
     * Gets a list of localities whose name is the same length or longer than <code>minLength</code> and
     * contains <code>nameSubstring</code> and whose type is <code>type</code>
     * @param minLength all <code>Locality</code>-ies' names returned must be at least of the given length
     * @param nameSubstring a <code>String</code> that all the localities' names must contain
     * @param type just <code>Locality</code>-ies of the given type will be returned
     * @return a <code>List<code> of localities satisfying criteria
     */
    List<Locality> getLocalitiesByMinLength(int minLength, String nameSubstring,
            LocalityType type);

}
