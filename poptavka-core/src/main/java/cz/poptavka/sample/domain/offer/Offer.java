/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.offer;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.domain.user.Supplier;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 *
 * Reponse to a demand by a supplier
 */
@Entity
public class Offer extends DomainObject {
    private BigDecimal price;
    @Temporal(value = TemporalType.TIMESTAMP)
    @ManyToOne
    private OfferState state;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;
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


}
