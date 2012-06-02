package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.register.Register;

import javax.persistence.Entity;

/**
 * Register of all posible origins for demands in our systems. However, this register is quite dynamic, because
 * new origins can be added all the time.
 * <p>
 *     Not all attributes must be filled - e.g. the url can be missing. However, name should be always provided.
 *
 * @author Juraj Martinka
 *         Date: 22.4.11
 */
@Entity
public class DemandOrigin extends Register {

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


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DemandOrigin");
        sb.append("{code='").append(getCode()).append('\'');
        sb.append(", url='").append(url).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
