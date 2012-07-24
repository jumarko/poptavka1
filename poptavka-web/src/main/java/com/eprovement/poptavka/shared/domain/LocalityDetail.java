package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.domain.enums.LocalityType;
import java.io.Serializable;

/**
 * Low-bandwitdh representation of Locality designed for direct use on frontend.
 *
 * @author Beho
 *
 */
public class LocalityDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 8480517227278818048L;
    private Long id;
    private String name;
    private String code;
    private LocalityType localityType;

    public LocalityDetail() {
    }

    public LocalityDetail(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public LocalityDetail(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
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
        return name;
    }

    public LocalityType getLocalityType() {
        return localityType;
    }

    public void setLocalityType(LocalityType localityType) {
        this.localityType = localityType;
    }
}
