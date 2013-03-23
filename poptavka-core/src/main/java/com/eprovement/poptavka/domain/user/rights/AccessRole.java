package com.eprovement.poptavka.domain.user.rights;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.CommonAccessRoles;
import com.eprovement.poptavka.domain.user.User;
import org.apache.commons.lang.Validate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;

/**
 * Role in a application. {@link com.eprovement.poptavka.domain.user.User} can have different roles, e.g.
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
     * {@link com.eprovement.poptavka.domain.user.User} with {@link AccessRole} that has this permission.
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


    public static boolean isAdmin(User user) {
        return hasAccessRole(user, CommonAccessRoles.ADMIN_ACCESS_ROLE_CODE);
    }

    public static boolean isUser(User user) {
        return hasAccessRole(user, CommonAccessRoles.USER_ACCESS_ROLE_CODE);
    }

    public static boolean isClient(User user) {
        return hasAccessRole(user, CommonAccessRoles.CLIENT_ACCESS_ROLE_CODE);
    }

    public static boolean isSupplier(User user) {
        return hasAccessRole(user, CommonAccessRoles.SUPPLIER_ACCESS_ROLE_CODE);
    }


    private static boolean hasAccessRole(User user, String accessRoleCode) {
        Validate.notNull(user, "user cannot be null!");
        for (AccessRole userAccessRole : user.getAccessRoles()) {
            if (accessRoleCode.equals(userAccessRole.getCode())) {
                return true;
            }
        }
        return false;
    }

}
