package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.user.rights.Permission;
import java.io.Serializable;

/**
 * Represents full detail of domain object <b>Permission</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 *
 */
public class PermissionDetail implements Serializable, Comparable<PermissionDetail> {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long id;
    private String name;
    private String description;
    private String code;

    /** for serialization. **/
    public PermissionDetail() {
    }

    public PermissionDetail(PermissionDetail prem) {
        this.updateWholePermission(prem);
    }

    /**
     * Method created <b>PermissionDetail</b> from provided Demand domain object.
     * @param domain - given domain object
     * @return PermissionDetail - created detail object
     */
    public static PermissionDetail createPermissionsDetail(Permission domain) {
        PermissionDetail detail = new PermissionDetail();

        detail.setId(domain.getId());
        detail.setName(domain.getName());
        detail.setDescription(domain.getDescription());
        detail.setCode(domain.getCode());

        return detail;
    }

    /**
     * Method created domain object <b>Permission</b> from provided <b>PermissionDetail</b> object.
     * @param domain - domain object to be updated
     * @param detail - detail object which provides updated data
     * @return Permission - updated given domain object
     */
    public static Permission updatePermission(Permission domain, PermissionDetail detail) {
        if (!domain.getName().equals(detail.getName())) {
            domain.setName(detail.getName());
        }
        if (!domain.getDescription().equals(detail.getDescription())) {
            domain.setDescription(detail.getDescription());
        }
        if (!domain.getCode().equals(detail.getCode())) {
            domain.setCode(detail.getCode());
        }
        return domain;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholePermission(PermissionDetail permissionsDetail) {
        id = permissionsDetail.getId();
        name = permissionsDetail.getName();
        description = permissionsDetail.getDescription();
        code = permissionsDetail.getCode();
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

    @Override
    public String toString() {
        return "\nGlobal Permissions Detail Info:"
                + "\n    Id=" + Long.toString(id)
                + "\n    Name=" + name
                + "\n    Description=" + description
                + "\n    Code=" + code;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PermissionDetail other = (PermissionDetail) obj;
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

    @Override
    public int compareTo(PermissionDetail t) {
        return name.compareTo(t.getName());
    }
}
