package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
@Entity
@Table(name = "DEMAND_LOCALITY")
@NamedQueries({
        @NamedQuery(name = "getDemandsForLocalities", query = "select demandLocality.demand"
                        + DemandLocality.DEMANDS_FOR_LOCALITIES_FW_CLAUSE),
        @NamedQuery(name = "getDemandsCountForLocalities", query = "select count(distinct demandLocality.demand)"
                        + DemandLocality.DEMANDS_FOR_LOCALITIES_FW_CLAUSE),
        @NamedQuery(name = "getDemandsCountForLocality", query = "select count(distinct demandLocality.demand)"
                + DemandLocality.DEMANDS_FOR_LOCALITIES_QUICK_FW_CLAUSE),
        /**
         * Get count of all demands that belongs directly to the specified locality. No demands belonging to
         * any sublocality are included!
         */
        @NamedQuery(name = "getDemandsCountForLocalityWithoutChildren", query = "select count(demandLocality.demand) "
                + " from DemandLocality demandLocality where demandLocality.locality = :locality")
})
public class DemandLocality extends DomainObject {

    static final String DEMANDS_FOR_LOCALITIES_FW_CLAUSE = " from DemandLocality demandLocality"
            + " where demandLocality.locality.id in (:localitiesIds)";

    static final String DEMANDS_FOR_LOCALITIES_QUICK_FW_CLAUSE = " from DemandLocality demandLocality"
            + " where demandLocality.locality.id = :localityId "
            + "or (demandLocality.locality.leftBound between :leftBound and :rightBound)";



    @ManyToOne
    private Demand demand;

    @ManyToOne
    private Locality locality;


    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DemandLocality");
        sb.append("{demand=").append(demand);
        sb.append(", locality=").append(locality);
        sb.append('}');
        return sb.toString();
    }
}
