package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Entity;

/**
 * Rating of the client and supplier of a demand
 *
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@Entity
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
