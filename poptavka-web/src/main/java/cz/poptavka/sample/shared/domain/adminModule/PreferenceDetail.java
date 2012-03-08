package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.settings.Preference;
import java.io.Serializable;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class PreferenceDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long id;
    private String key;
    private String value;
    private String description;

    /** for serialization. **/
    public PreferenceDetail() {
    }

    public PreferenceDetail(PreferenceDetail pref) {
        this.updateWholePreference(pref);
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param pref
     * @return DemandDetail
     */
    public static PreferenceDetail createPreferenceDetail(Preference pref) {
        PreferenceDetail detail = new PreferenceDetail();

        detail.setId(pref.getId());
        detail.setKey(pref.getKey());
        detail.setValue(pref.getValue());
        detail.setDescription(pref.getDescription());

        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholePreference(PreferenceDetail preferenceDetail) {
        id = preferenceDetail.getId();
        key = preferenceDetail.getKey();
        value = preferenceDetail.getValue();
        description = preferenceDetail.getDescription();
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String name) {
        this.key = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {

        return "\nGlobal Preference Detail Info:"
                + "\n    demandTypeId="
                + id + "\n     key="
                + key + "\n    value="
                + key + "\n    Description="
                + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PreferenceDetail other = (PreferenceDetail) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
