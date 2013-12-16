package com.eprovement.poptavka.domain.user.rights;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Entity;

/**
 * Simple action that can be invoked by a user.
 *
 * @deprecated  will be removed -- it provides the unnecessary flexibility. Using of {@link Permission} should be
 * sufficient.
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Deprecated
@Entity
public class Action extends DomainObject {

    private String code;

    private String name;

    private String description;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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
        sb.append("Action");
        sb.append("{code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
