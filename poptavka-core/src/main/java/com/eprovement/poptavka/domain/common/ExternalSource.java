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
     * Unique code of external source. It's like a simplified name, e.g. "FBOGOV".
     */
    @Column(unique = true, length = 64)
    private String code;

    @Column(unique = true)
    private String url;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "ExternalSource{"
                + "code='" + code + '\''
                + ", url='" + url + '\''
                + '}';
    }
}
