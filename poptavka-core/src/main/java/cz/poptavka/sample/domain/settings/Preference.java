package cz.poptavka.sample.domain.settings;

import cz.poptavka.sample.domain.common.DomainObject;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;

/**
 * Represents one user's preference.
 * <p>
 * To manage the required flexibility for storing arbitrary preferences, it is neccessary to store preferences in simple
 * form: <key, value>.
 * Key is used for accessing
 * <p>
 * This approach comes with limitation to store only simple settings which values can be represents by strings, e.g.>
 * <ul>
 *  <li>User's preferred color.</li>
 *  <li>User's preferred font-size.</li>
 *  <li>User's preferred color.</li>
 *  <li>User's preferred color.</li>
 *
 * </ul>
 *
 * @author Juraj Martinka
 *         Date: 4.2.11
 */
@Entity
@Audited
@NamedQuery(name = "getPreferencesByKey", query = "from Preference p where p.key = :key")
public class Preference extends DomainObject {

    @Column(name = "pKey") // key is not allowed as a name of column
    private String key;

    private String value;

    private String description;


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Preference");
        sb.append("{key='").append(key).append('\'');
        sb.append(", value='").append(value).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
