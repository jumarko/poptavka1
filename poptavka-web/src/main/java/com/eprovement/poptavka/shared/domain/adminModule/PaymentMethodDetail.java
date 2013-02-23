package com.eprovement.poptavka.shared.domain.adminModule;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Represents full detail of domain object <b>PaymentMethod</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 *
 */
public class PaymentMethodDetail implements IsSerializable {

    private Long id;
    private String name;
    private String description;

    /** for serialization. **/
    public PaymentMethodDetail() {
    }

    public PaymentMethodDetail(PaymentMethodDetail demand) {
        this.updateWholePaymentMethod(demand);
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholePaymentMethod(PaymentMethodDetail paymentMethodDetail) {
        id = paymentMethodDetail.getId();
        name = paymentMethodDetail.getName();
        description = paymentMethodDetail.getDescription();
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

    @Override
    public String toString() {
        return "\nGlobal PaymentMethod Detail Info:"
                + "\n    DemandTypeId=" + Long.toString(id)
                + "\n    Name=" + name
                + "\n    Description=" + description;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PaymentMethodDetail other = (PaymentMethodDetail) obj;
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
