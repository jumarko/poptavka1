package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.demand.Demand;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Client demands to have projects realized
 * 
 * @author Juraj Martinka
 */
@Entity
//@org.hibernate.annotations.Table(comment = )
@Audited
public class Client extends BusinessUserRole {

    @OneToMany(mappedBy = "client")
    private List<Demand> demands;

    /** Total rating of supplier for all his "processed" demands .*/
    private Integer overalRating = Integer.valueOf(0);

    @OneToOne(mappedBy = "client")
    @NotAudited
    private SupplierBlacklist supplierBlacklist;


    public List<Demand> getDemands() {
        return demands;
    }

    public void setDemands(List<Demand> demands) {
        this.demands = demands;
    }

    public Integer getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(Integer overalRating) {
        this.overalRating = overalRating;
    }

    public SupplierBlacklist getSupplierBlacklist() {
        return supplierBlacklist;
    }

    public void setSupplierBlacklist(SupplierBlacklist supplierBlacklist) {
        this.supplierBlacklist = supplierBlacklist;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Client");
        sb.append(", ID=").append(this.getId());
        sb.append(", overalRating=").append(overalRating);
        sb.append('}');
        return sb.toString();
    }
}
