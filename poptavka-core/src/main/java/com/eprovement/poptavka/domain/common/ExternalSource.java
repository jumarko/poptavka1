package com.eprovement.poptavka.domain.common;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Represents external source of demands and/or suppliers.
 * This can be sites such as fbo.gov and similar
 */
@Entity
public class ExternalSource extends DomainObject {

    /**
     * Unique name of external source.
     */
    @Column(unique = true, length = 64)
    private String name;

    @Column(unique = true)
    private String url;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ExternalSource{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
