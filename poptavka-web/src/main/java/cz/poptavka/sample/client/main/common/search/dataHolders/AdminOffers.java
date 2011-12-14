package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;
import java.util.Date;

/** ADMINOFFER **/
public class AdminOffers implements Serializable {

    private Long offerIdFrom = null;
    private Long offerIdTo = null;
    private Long demandIdFrom = null;
    private Long demandIdTo = null;
    private Long supplierIdFrom = null;
    private Long supplierIdTo = null;
    private Integer priceFrom = null;
    private Integer priceTo = null;
    private String state = null;
    private Date createdFrom = null;
    private Date createdTo = null;
    private Date finnishFrom = null;
    private Date finnishTo = null;

    public Long getDemandIdFrom() {
        return demandIdFrom;
    }

    public void setDemandIdFrom(Long demandIdFrom) {
        this.demandIdFrom = demandIdFrom;
    }

    public Long getDemandIdTo() {
        return demandIdTo;
    }

    public void setDemandIdTo(Long demandIdTo) {
        this.demandIdTo = demandIdTo;
    }

    public Date getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Date getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(Date createdTo) {
        this.createdTo = createdTo;
    }

    public Date getFinnishFrom() {
        return finnishFrom;
    }

    public void setFinnishFrom(Date finnishFrom) {
        this.finnishFrom = finnishFrom;
    }

    public Date getFinnishTo() {
        return finnishTo;
    }

    public void setFinnishTo(Date finnishTo) {
        this.finnishTo = finnishTo;
    }

    public Long getOfferIdFrom() {
        return offerIdFrom;
    }

    public void setOfferIdFrom(Long offerIdFrom) {
        this.offerIdFrom = offerIdFrom;
    }

    public Long getOfferIdTo() {
        return offerIdTo;
    }

    public void setOfferIdTo(Long offerIdTo) {
        this.offerIdTo = offerIdTo;
    }

    public Integer getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Integer priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Integer getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Integer priceTo) {
        this.priceTo = priceTo;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Long getSupplierIdFrom() {
        return supplierIdFrom;
    }

    public void setSupplierIdFrom(Long supplierIdFrom) {
        this.supplierIdFrom = supplierIdFrom;
    }

    public Long getSupplierIdTo() {
        return supplierIdTo;
    }

    public void setSupplierIdTo(Long supplierIdTo) {
        this.supplierIdTo = supplierIdTo;
    }
}