package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

public class OfferDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -563380651738612866L;

    private BigDecimal price;
    private boolean displayed = false;
    private Date finishDate;
    private long demandId;
    private long supplierId;
    private String supplierName;
    private long messageId;

    public OfferDetail() {
    }


    public BigDecimal getPrice() {
        return price;
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }
    public boolean isDisplayed() {
        return displayed;
    }
    public void setDisplayed(boolean displayed) {
        this.displayed = displayed;
    }
    public Date getFinishDate() {
        return finishDate;
    }
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }
    public long getDemandId() {
        return demandId;
    }
    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }
    public long getSupplierId() {
        return supplierId;
    }
    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }
    public String getSupplierName() {
        return supplierName;
    }
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
    public void setMessageId(long id) {
        this.messageId = id;
    }
    public long getMessageId() {
        return messageId;
    }

}
