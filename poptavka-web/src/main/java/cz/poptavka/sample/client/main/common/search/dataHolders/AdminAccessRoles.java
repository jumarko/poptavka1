package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;

/** ADMINACCESSROLE **/
public class AdminAccessRoles implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private String code = null;
    private String roleName = null;
    private String roleDescription = null;

    public AdminAccessRoles() {
    }
    private String[] permisstions = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }

    public String[] getPermisstions() {
        return permisstions;
    }

    public void setPermisstions(String[] permisstions) {
        this.permisstions = permisstions;
    }

    public String getRoleDescription() {
        return roleDescription;
    }

    public void setRoleDescription(String roleDescription) {
        this.roleDescription = roleDescription;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}