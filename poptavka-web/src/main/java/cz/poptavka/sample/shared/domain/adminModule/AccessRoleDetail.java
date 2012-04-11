package cz.poptavka.sample.shared.domain.adminModule;

import java.io.Serializable;

import cz.poptavka.sample.domain.user.rights.AccessRole;
import cz.poptavka.sample.domain.user.rights.Permission;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents full detail of domain object <b>AccessRole</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
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
     * Method created <b>AccessRoleDetail</b> from provided Demand domain object.
     * @param domain - given domain object
     * @return AccessRoleDetail - created detail object
     */
    public static AccessRoleDetail createAccessRoleDetail(AccessRole domain) {
        AccessRoleDetail detail = new AccessRoleDetail();

        detail.setId(domain.getId());
        detail.setName(domain.getName());
        detail.setDescription(domain.getDescription());
        detail.setCode(domain.getCode());
        List<PermissionDetail> permissions = new ArrayList<PermissionDetail>();
        for (Iterator<Permission> it = domain.getPermissions().iterator(); it.hasNext();) {
            permissions.add(PermissionDetail.createPermissionsDetail(it.next()));
        }
        detail.setPermissions(permissions);
        return detail;
    }

    /**
     * Method created domain object <b>AccessRole</b> from provided <b>AccessRoleDetail</b> object.
     * @param domain - domain object to be updated
     * @param detail - detail object which provides updated data
     * @return AccessRole - updated given domain object
     */
    public static AccessRole updateAccessRole(AccessRole domain, AccessRoleDetail detail) {
        if (!domain.getName().equals(detail.getName())) {
            domain.setName(detail.getName());
        }
        if (!domain.getDescription().equals(detail.getDescription())) {
            domain.setDescription(detail.getDescription());
        }
        if (!domain.getCode().equalsIgnoreCase(detail.getCode())) {
            domain.setCode(detail.getCode());
        }
        //TODO Martin - update permissions
        return domain;
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
                + "\n    AccessRoleId=" + Long.toString(id)
                + "\n    Name=" + name
                + "\n    Code=" + code
                + "\n    Description=" + description
                + "\n    Permissions=" + permissions.toString();
    }

    //TODO je to potrebne mat?
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
