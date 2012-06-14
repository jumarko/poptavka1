package com.eprovement.poptavka.shared.domain.adminModule;

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
