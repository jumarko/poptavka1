package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * The rating of supply of some demand.
 * There are two perspectives of rating>
 * <ul>
 *   <li>from client's point of view</li>
 *   <li>from supplier's point of view</li>
 * </ul>
 *
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getAvgRatingForClient",
    query = "select avg(demand.rating.clientRating) from Demand as demand\n"
            + "where demand.client = :client group by demand.client"),
    @NamedQuery(name = "getAvgRatingForSupplier",
    query = "select avg(demand.rating.supplierRating) from Demand as demand"
            + " join demand.suppliers as supplier\n"
            + "where supplier = :supplier group by supplier")
})
public class Rating extends DomainObject {

    // rating in percents - % - 100 -> 100%
    private Integer supplierRating;
    private String supplierMessage;

    // rating in percents - % - 100 -> 100%
    private Integer clientRating;
    private String clientMessage;


    public Integer getSupplierRating() {
        return supplierRating;
    }

    public void setSupplierRating(Integer supplierRating) {
        this.supplierRating = supplierRating;
    }

    public String getSupplierMessage() {
        return supplierMessage;
    }

    public void setSupplierMessage(String supplierMessage) {
        this.supplierMessage = supplierMessage;
    }

    public Integer getClientRating() {
        return clientRating;
    }

    public void setClientRating(Integer clientRating) {
        this.clientRating = clientRating;
    }

    public String getClientMessage() {
        return clientMessage;
    }

    public void setClientMessage(String clientMessage) {
        this.clientMessage = clientMessage;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Rating");
        sb.append("{supplierRating=").append(supplierRating);
        sb.append(", supplierMessage='").append(supplierMessage).append('\'');
        sb.append(", clientRating=").append(clientRating);
        sb.append(", clientMessage='").append(clientMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
