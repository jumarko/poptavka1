package cz.poptavka.sample.domain.address;

/**
 * Type of locality available in system.
 *
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
public enum LocalityType {
    COUNTRY(1),
    /** "kraj" in the Czech Republic.*/
    REGION(2),
    /** "okres" in the Czech Republic.*/
    DISTRICT(3),
    /** "obec in the Czech Republic.*/
    CITY(4);

    /**
     * Level which corresponds to this locality type.
     *@see cz.poptavka.sample.domain.common.TreeItem
     */
    private final int level;


    LocalityType(int level) {
        this.level = level;
    }


    public int getLevel() {
        return level;
    }
}
