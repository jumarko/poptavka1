package com.eprovement.poptavka.shared.domain.demand;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class OriginDetail implements IsSerializable {

    private Long id;
    private String name;
    private String description;
    private String url;

    /** for serialization. **/
    public OriginDetail() {
    }

    public OriginDetail(OriginDetail demand) {
        this.updateWholeDemandOrigin(demand);
    }


    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeDemandOrigin(OriginDetail originDetail) {
        id = originDetail.getId();
        name = originDetail.getName();
        description = originDetail.getDescription();
        url = originDetail.getUrl();
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

        return "\nGlobal Origin Detail Info:"
                + "\n    id="
                + id + "\n     name="
                + name + "\n    Description="
                + description + "\n    Url="
                + url;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final OriginDetail that = (OriginDetail) o;

        if (!description.equals(that.description)) {
            return false;
        }
        if (!id.equals(that.id)) {
            return false;
        }
        if (!name.equals(that.name)) {
            return false;
        }
        if (!url.equals(that.url)) {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        int result = id.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + description.hashCode();
        result = 31 * result + url.hashCode();
        return result;
    }
}
