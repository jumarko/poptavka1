package cz.poptavka.sample.domain.address;

/**
 * Type of locality available in system.
 *
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public enum LocalityType {
    COUNTRY,
    /** In Czech Republic "oblast".*/
    REGION,
    /** In Czech Republic "kraj".*/
    DISTRICT,
    /** In Czech Republic "okres".*/
    TOWNSHIP,
    /** In Czech Republic "obec".*/
    CITY
}
