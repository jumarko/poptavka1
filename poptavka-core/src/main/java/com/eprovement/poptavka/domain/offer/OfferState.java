package com.eprovement.poptavka.domain.offer;

import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.domain.register.Register;

import javax.persistence.Entity;

/**
 * Wrapper for the enum representing a life-cycle state of an offer
 * 
 * @author Vojtech Hubr
 *         Date 12.4.11

 */
@Entity
public class OfferState extends Register {

    private String description;

    public OfferState() {
    }

    public OfferStateType getType() {
        return OfferStateType.fromValue(getCode());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("OfferState");
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
