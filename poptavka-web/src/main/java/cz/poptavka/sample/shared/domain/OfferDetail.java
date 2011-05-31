package cz.poptavka.sample.shared.domain;

import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.offer.Offer;
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
    private long messageId;
    private String supplierName;
    private MessageDetail messageDetail;

    public static OfferDetail generateOfferDetail(Message message) {
        MessageDetail m = new MessageDetail();
        m.setId(message.getId());
        m.setBody(message.getBody());
        m.setCreated(message.getCreated());
//        m.setFirstBornId(serialVersionUID);
        m.setMessageState(message.getMessageState().name());
//        m.setNexSiblingId(serialVersionUID);
        m.setParentId(message.getParent().getId());
//        m.setReceiverId();
        m.setSenderId(message.getSender().getId());
        m.setSent(message.getSent());
        m.setSubject(message.getSubject());
        m.setThreadRootId(message.getThreadRoot().getId());
        OfferDetail o = new OfferDetail();
        Offer offer = message.getOffer();
        o.setDemandId(offer.getDemand().getId());
        o.setFinishDate(offer.getFinishDate());
        o.setMessageDetail(m);
        // tofo ivlcek verify id
        o.setMessageId(m.getId());
        o.setPrice(offer.getPrice());
        o.setSupplierId(offer.getSupplier().getId());
        return o;
    }

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

    public void setMessageId(long id) {
        this.messageId = id;
    }

    public long getMessageId() {
        return messageId;
    }

    /**
     * @return the messageDetail
     */
    public MessageDetail getMessageDetail() {
        return messageDetail;
    }

    /**
     * @param messageDetail the messageDetail to set
     */
    public void setMessageDetail(MessageDetail messageDetail) {
        this.messageDetail = messageDetail;
    }

    /**
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }
}
