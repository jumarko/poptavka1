package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Martin Slavkovsky
 */
public class PropertiesDetail implements IsSerializable {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    private Long id;
    private String key;
    private String value;
    private String title;
    private String description;

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    public PropertiesDetail() {
        //for serialization
    }

    /**************************************************************************/
    /*  Getter & Setter                                                       */
    /**************************************************************************/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
