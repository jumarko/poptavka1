package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.DomainObject;

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
        @NamedQuery(name = "getDemandsCountForLocality",
                query = "select count(distinct demandLocality.demand)"
                        + " from DemandLocality demandLocality"
                        + " where demandLocality.locality.leftBound between :leftBound and :rightBound"),
        /**
         * In one query compute demands count for each locality and return it as a list of pairs
         * <localityId, demandsCountForLocality>.
         */
        @NamedQuery(name = "getDemandsCountForAllLocalities",
                query = "select new map(l AS locality, "
                        + "  (select count(distinct demandLocality.demand)"
                        + "    from DemandLocality demandLocality"
                        + "    where demandLocality.locality.leftBound "
                        + "          between l.leftBound and l.rightBound)"
                        + "   AS demandsCount)"
                        + " from Locality l"),
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
