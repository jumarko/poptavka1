package com.eprovement.poptavka.shared.domain.demand;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class DemandOriginDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private final static Logger LOGGER = Logger.getLogger("DemandOriginDetail");
    private Long id;
    private String name;
    private String description;
    private String url;

    /** for serialization. **/
    public DemandOriginDetail() {
    }

    public DemandOriginDetail(DemandOriginDetail demand) {
        this.updateWholeDemandOrigin(demand);
    }


    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeDemandOrigin(DemandOriginDetail demandOriginDetail) {
        id = demandOriginDetail.getId();
        name = demandOriginDetail.getName();
        description = demandOriginDetail.getDescription();
        url = demandOriginDetail.getUrl();
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {

        return "\nGlobal DemandOrigin Detail Info:"
                + "\n    demandTypeId="
                + id + "\n     name="
                + name + "\n    Description="
                + description + "\n    Url="
                + url;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DemandOriginDetail other = (DemandOriginDetail) obj;
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
}
