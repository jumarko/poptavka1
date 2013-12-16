package com.eprovement.poptavka.domain.common;

import com.eprovement.poptavka.domain.register.Register;

import javax.persistence.Entity;

/**
 * Register of all possible origins for demands, clients and other entities in our system.
 * This register is quite dynamic, because new origins can be added all the time.
 * <p>
 *     Not all attributes must be filled - e.g. the url can be missing. However, {@code code} and {@code name}
 *     should be always provided.
 * </p>
 *
 * @author Juraj Martinka
 */
@Entity
public class Origin extends Register {

    /** code of generic origin for external systems in case no concrete origin is specified. */
    public static final String EXTERNAL_ORIGIN_CODE = "external";


    /** link to the source of demand. */
    private String url;

    /** The "official" name of the source. Mandatory */
    private String name;

    /** The description of source. */
    private String description;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isExternal() {
        return getCode() != null && getCode().startsWith(EXTERNAL_ORIGIN_CODE);
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Origin");
        sb.append("{code='").append(getCode()).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
