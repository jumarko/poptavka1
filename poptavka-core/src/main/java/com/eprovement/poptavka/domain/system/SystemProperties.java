package com.eprovement.poptavka.domain.system;

import com.eprovement.poptavka.domain.common.DomainObject;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * @author Martin Slavkovsky
 * @since 1.8.2014
 */
@Entity
public class SystemProperties extends DomainObject {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    @Column(name = "pKey")
    private String key;
    @Column(name = "pValue")
    private String value;
    private String title;
    private String description;

    /**************************************************************************/
    /*  Getter & Setter                                                       */
    /**************************************************************************/
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**************************************************************************/
    /*  Override methods                                                      */
    /**************************************************************************/
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + Objects.hashCode(this.key);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemProperties other = (SystemProperties) obj;
        if (!Objects.equals(this.key, other.key)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Properties{" + "key=" + key
            + ", value=" + value
            + ", title=" + title
            + ", description=" + description + '}';
    }

}
