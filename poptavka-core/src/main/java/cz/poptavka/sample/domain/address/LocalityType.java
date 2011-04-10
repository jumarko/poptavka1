package cz.poptavka.sample.domain.address;

/**
 * Type of locality available in system.
 *
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public enum LocalityType {
    COUNTRY,
    /** "kraj" in the Czech Republic.*/
    REGION,
    /** "okres" in the Czech Republic.*/
    DISTRICT,
    /** "obec in the Czech Republic.*/
    CITY
}
