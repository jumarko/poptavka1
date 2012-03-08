package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.user.rights.Permission;
import java.io.Serializable;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
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
     * Method created FullDemandDetail from provided Demand domain object.
     * @param perm
     * @return DemandDetail
     */
    public static PermissionDetail createPermissionsDetail(Permission perm) {
        PermissionDetail detail = new PermissionDetail();

        detail.setId(perm.getId());
        detail.setName(perm.getName());
        detail.setDescription(perm.getDescription());
        detail.setCode(perm.getCode());

        return detail;
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
                + "\n    Id="
                + id + "\n     name="
                + name + "\n    Description="
                + description + "\n    Code="
                + code;
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
