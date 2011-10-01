package cz.poptavka.sample.shared.domain.demand;

import java.io.Serializable;
import java.util.logging.Logger;

import cz.poptavka.sample.domain.demand.DemandType;

/**
 * Represents full detail of demandType. Serves for creating new
 * demandType or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class DemandTypeDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private final static Logger LOGGER = Logger.getLogger("    FullDemandDetail");
    private Long id;
    private String value;
    private String description;

    /** for serialization. **/
    public DemandTypeDetail() {
    }

    public DemandTypeDetail(DemandTypeDetail demand) {
        this.updateWholeDemandType(demand);
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param demand
     * @return DemandDetail
     */
    public static DemandTypeDetail createDemandTypeDetail(DemandType demand) {
        DemandTypeDetail detail = new DemandTypeDetail();

        detail.setId(demand.getId());
        detail.setValue(demand.getType().getValue());
        detail.setDescription(demand.getDescription());

        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeDemandType(DemandTypeDetail demandTypeDetail) {
        id = demandTypeDetail.getId();
        value = demandTypeDetail.getValue();
        description = demandTypeDetail.getDescription();
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {

        return "\nGlobal DemandType Detail Info:"
                + "\n    demandTypeId="
                + id + "\n     value="
                + value + "\n    Description="
                + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final DemandTypeDetail other = (DemandTypeDetail) obj;
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
