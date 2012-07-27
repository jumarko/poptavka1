package com.eprovement.poptavka.shared.domain.adminModule;

import com.eprovement.poptavka.domain.enums.OfferStateType;
import java.io.Serializable;
//import java.math.BigDecimal;
import java.util.Date;

/**
 * Represents full detail of domain object <b>Offer</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 */
public class OfferDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -563380651738612866L;
    // OfferDetail
    private long id;
//    private BigDecimal price;
    private Number price;
    private OfferStateType state;
    private Date createdDate;
    private Date finishDate;
    //Demand info
    private long demandId;
    private String demandTitle;
    //Supplier info
    private long supplierId;
    private String clientName;
    private String supplierName;
    private int rating;

    /** for serialization. **/
    public OfferDetail() {
    }

    public OfferDetail(OfferDetail offerDetail) {
        this.updateWholeOfferDetail(offerDetail);
    }

    //---------------------------- GETTERS AND SETTERS --------------------
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

//    public BigDecimal getPrice() {
//        return price;
//    }
//
//    public void setPrice(BigDecimal price) {
//        this.price = price;
//    }


    public Number getPrice() {
        return price;
    }

    public void setPrice(Number price) {
        this.price = price;
    }

    public OfferStateType getState() {
        return state;
    }

    public void setState(OfferStateType state) {
        this.state = state;
    }



    public String getSupplierName() {
        return supplierName;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "\nGlobal AccessRole Detail Info:"
                + "\n    Id=" + Long.toString(id)
                + "\n    Price=" + price.toString()
                + "\n    State=" + state
                + "\n    CreatedDate=" + createdDate.toString()
                + "\n    FinnishDate=" + finishDate.toString()
                + "\n    DemandId=" + Long.toString(demandId)
                + "\n    DemandTitle=" + demandTitle
                + "\n    SupplierId=" + Long.toString(supplierId)
                + "\n    SupplierName=" + supplierName;
    }
}
