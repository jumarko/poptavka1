package com.eprovement.poptavka.domain.offer;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.util.strings.ToStringUtils;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 * Response to a demand by a supplier
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getOffersCountForSupplier",
    query = "select count(*) from Offer offer\n"
        + "where offer.state = :state"
        + " and offer.supplier.id = :supplier\n"),
    @NamedQuery(name = "getOffersCountForSupplierByStates",
    query = "select count(*) from Offer offer\n"
        + "where (offer.state = :state1"
        + " or offer.state = :state2)"
        + " and offer.supplier.id = :supplier\n")
})

public class Offer extends DomainObject {
    /**
     * price offered by the supplier
     */
    private BigDecimal price;
    @Temporal(value = TemporalType.TIMESTAMP)
    @ManyToOne
    private OfferState state;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;
    /**
     * a date by which the demand (project) should have been realized
     */
    @Temporal(value = TemporalType.DATE)
    private Date finishDate;
    @ManyToOne
    private Demand demand;

    @OneToOne
    private Supplier supplier;

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Demand getDemand() {
        return demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public OfferState getState() {
        return state;
    }

    public void setState(OfferState state) {
        this.state = state;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Offer");
        sb.append("{price=").append(price);
        sb.append(", state=").append(state);
        sb.append(", created=").append(created);
        sb.append(", finishDate=").append(finishDate);
        sb.append(", demand=").append(ToStringUtils.printId(demand));
        sb.append(", supplier=").append(ToStringUtils.printId(supplier));
        sb.append('}');
        return sb.toString();
    }
}
