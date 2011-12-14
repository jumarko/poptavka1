package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;

/** ADMINPREFERENCES **/
public class AdminPreferences implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private String key = null;
    private String value = null;
    private String description = null;

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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}