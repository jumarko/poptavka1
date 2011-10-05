package cz.poptavka.sample.shared.domain;

import cz.poptavka.sample.domain.invoice.PaymentMethod;
import java.io.Serializable;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class PaymentMethodDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long id;
    private String name;
    private String description;

    /** for serialization. **/
    public PaymentMethodDetail() {
    }

    public PaymentMethodDetail(PaymentMethodDetail demand) {
        this.updateWholePaymentMethod(demand);
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param demand
     * @return DemandDetail
     */
    public static PaymentMethodDetail createPaymentMethodDetail(PaymentMethod demand) {
        PaymentMethodDetail detail = new PaymentMethodDetail();

        detail.setId(demand.getId());
        detail.setName(demand.getName());
        detail.setDescription(demand.getDescription());

        return detail;
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
                + "\n    demandTypeId="
                + id + "\n     name="
                + name + "\n    Description="
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
