package cz.poptavka.sample.shared.domain.adminModule;

import java.io.Serializable;

import cz.poptavka.sample.domain.user.rights.AccessRole;
import cz.poptavka.sample.domain.user.rights.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents full detail of AccessRole. Serves for creating new
 * AccessRole or for call of detail, that supports editing.
 *
 * @author Martin Slavkovsky
 *
 */
public class AccessRoleDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long id;
    private String name;
    private String description;
    private String code;
    private List<PermissionDetail> permissions;

    /** for serialization. **/
    public AccessRoleDetail() {
    }

    public AccessRoleDetail(AccessRoleDetail role) {
        this.updateWholeAccessRole(role);
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param role
     * @return DemandDetail
     */
    public static AccessRoleDetail createAccessRoleDetail(AccessRole role) {
        AccessRoleDetail detail = new AccessRoleDetail();

        detail.setId(role.getId());
        detail.setName(role.getName());
        detail.setDescription(role.getDescription());
        detail.setCode(role.getCode());
        List<PermissionDetail> permissions = new ArrayList<PermissionDetail>();
        for (Iterator<Permission> it = role.getPermissions().iterator(); it.hasNext();) {
            permissions.add(PermissionDetail.createPermissionsDetail(it.next()));
        }
        detail.setPermissions(permissions);
        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeAccessRole(AccessRoleDetail roleDetail) {
        id = roleDetail.getId();
        name = roleDetail.getName();
        description = roleDetail.getDescription();
        code = roleDetail.getCode();
        permissions = roleDetail.getPermissions();
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public List<PermissionDetail> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionDetail> permissions) {
        this.permissions = permissions;
        Collections.sort(this.permissions);
    }

    @Override
    public String toString() {

        return "\nGlobal AccessRole Detail Info:"
                + "\n    AccessRoleId="
                + id + "\n     name="
                + name + "\n    Code="
                + code + "\n    Description="
                + description + "\n    Permissions="
                + permissions.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AccessRoleDetail other = (AccessRoleDetail) obj;
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
