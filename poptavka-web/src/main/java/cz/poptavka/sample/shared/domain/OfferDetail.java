package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


import cz.poptavka.sample.domain.offer.Offer;

public class OfferDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -563380651738612866L;
    // OfferDetail
    private long id;
    private BigDecimal price;
    private String state;
    private Date createdDate;
    private Date finishDate;
    //Demand info
    private long demandId;
    private String demandTitle;
    //Supplier info
    private long supplierId;
    private String supplierName;

    public static OfferDetail createOfferDetail(Offer offer) {
        OfferDetail o = new OfferDetail();
        //offer info
        o.setId(offer.getId());
        o.setPrice(offer.getPrice());
        o.setState(offer.getState().getCode());
        o.setCreatedDate(offer.getCreated());
        o.setFinishDate(offer.getFinishDate());
        //demand info
        o.setDemandId(offer.getDemand().getId());
        o.setDemandTitle(offer.getDemand().getTitle());
        //supplier info
        o.setSupplierId(offer.getSupplier().getId());
        o.setSupplierName(offer.getSupplier().getBusinessUser().getBusinessUserData().getCompanyName());
        return o;
    }

    public OfferDetail() {
    }

    public OfferDetail(OfferDetail offerDetail) {
        this.updateWholeOfferDetail(offerDetail);
    }

    public void updateWholeOfferDetail(OfferDetail detail) {
        id = detail.getId();
        price = detail.getPrice();
        state = detail.getState();
        createdDate = detail.getCreatedDate();
        finishDate = detail.getFinishDate();

        //Demand info
        demandId = detail.getDemandId();
        demandTitle = detail.getDemandTitle();
        //Supplier info
        supplierId = detail.getSupplierId();
        supplierName = detail.getSupplierName();
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
