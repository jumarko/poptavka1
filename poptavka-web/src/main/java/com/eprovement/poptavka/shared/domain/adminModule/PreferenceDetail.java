package com.eprovement.poptavka.shared.domain.adminModule;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents full detail of domain object <b>Preference</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 *
 */
public class PreferenceDetail implements IsSerializable {

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
                + "\n    DemandTypeId=" + Long.toString(id)
                + "\n    Key=" + key
                + "\n    Value=" + value
                + "\n    Description=" + description;
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
