package com.eprovement.poptavka.domain.user.rights;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * {@link Permission} aggreates actions to the consistent group that can be assinged to the {@link AccessRole}.
 * <p>
 * Permission can be related to an important action (comparable to use cases), e.g.
 * <ul>
 *  <li>Create new demand</li>
 *  <li>Search for demands</li>
 *  <li>Send an email</li>
 *
 * @see Action
 * @see AccessRole
 * </ul>
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
public class Permission extends DomainObject {

    @Column(length = 64)
    private String code;

    @Column(length = 64)
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
        sb.append("Permission");
        sb.append("{code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
