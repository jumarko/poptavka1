package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;

/** ADMINPERMISSION **/
public class AdminPermissions implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private String code = null;
    private String name = null;
    private String description = null;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}