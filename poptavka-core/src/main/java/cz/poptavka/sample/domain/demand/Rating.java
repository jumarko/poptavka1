package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;

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
public class Rating extends DomainObject {

    private Integer supplierRating;
    private String supplierMessage;

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
