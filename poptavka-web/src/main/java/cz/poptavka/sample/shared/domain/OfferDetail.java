package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import com.google.gwt.core.client.GWT;

import cz.poptavka.sample.domain.message.Message;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.shared.domain.message.MessageDetail;
import cz.poptavka.sample.shared.domain.message.MessageDetailImpl;

public class OfferDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -563380651738612866L;
    private BigDecimal price;
    // TODO remove dipslpayed
    private boolean isRead;
    private Date finishDate;
    private long supplierId;
    private long messageId;
    private long offerId;
    private String supplierName;
    private MessageDetail messageDetail;
    private String state;

    public static OfferDetail generateOfferDetail(Message message) {
        MessageDetailImpl m = new MessageDetailImpl();
        m.setMessageId(message.getId());
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
        o.setFinishDate(offer.getFinishDate());
        o.setMessageDetail(m);
        // tofo ivlcek verify id
        o.setMessageId(m.getMessageId());
        o.setPrice(offer.getPrice());
        o.setSupplierId(offer.getSupplier().getId());
        o.setDemandId(offer.getDemand().getId());
        o.setOfferId(offer.getId());
        GWT.log("OFFER ID: " + offer.getId() + ", OFFER DETAIL ID: " + o.getOfferId());
        // TODO ivlcek - opravit na nieco rozumne
        o.setState(offer.getState().getCode());
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

    public void setPrice(String price) {
        long priceLong = Long.parseLong(price);
        this.price = BigDecimal.valueOf(priceLong);
    }

    public void setDemandId(Long demandId) {
        this.getMessageDetail().setDemandId(demandId);
    }

    public long getDemandId() {
        return this.getMessageDetail().getDemandId();
    }

    public String getPriceString() {
        return price.toPlainString();
    }

    public boolean getIsRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
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
    public void setMessageDetail(MessageDetail messageDetailImpl) {
        this.messageDetail = messageDetailImpl;
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

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the offerId
     */
    public long getOfferId() {
        return offerId;
    }

    /**
     * @param offerId the offerId to set
     */
    public void setOfferId(long offerId) {
        this.offerId = offerId;
    }
}
