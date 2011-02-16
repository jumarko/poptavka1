package cz.poptavka.sample.domain.user.rights;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Role in a application. {@link cz.poptavka.sample.domain.user.User} can have different roles, e.g.
 * <ul>
 *  <li>Client</li>
 *  <li><Administrator/li>
 *  <li>Operator</li>
 * </ul>
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
public class AccessRole extends DomainObject {

    @Column(length = 64)
    private String code;

    @Column(length = 64)
    private String name;

    private String description;

    /**
     *  All permissions allowed for this roleactions that can be invoked by
     * {@link cz.poptavka.sample.domain.user.User} with {@link AccessRole} that has this permission.
     */
    @ManyToMany
    @JoinTable(name = "access_role_permission")
    private List<Permission> permissions;


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

    public List<Permission> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<Permission> permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AccessRole");
        sb.append("{description='").append(description).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
