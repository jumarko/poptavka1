package com.eprovement.poptavka.shared.domain.offer;

import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.google.gwt.view.client.ProvidesKey;
import java.io.Serializable;
import java.util.Date;

/**
 * TODO refactoring, maybe deleted later.
 *
 * @author Martin Slavkovsky
 */
public class FullOfferDetail implements Serializable, TableDisplay, IUniversalDetail {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -563380651738612866L;
    // TODO remove dipsplayed
    private int messageCount;
    private int unreadSubmessages;
    private MessageDetail messageDetail = new MessageDetail();
    private OfferDetail offerDetail = new OfferDetail();
    private FullDemandDetail demandDetail = new FullDemandDetail();
    //Keyprovider
    public static final ProvidesKey<IUniversalDetail> KEY_PROVIDER =
            new ProvidesKey<IUniversalDetail>() {
                @Override
                public Object getKey(IUniversalDetail item) {
                    return item == null ? null : item.getDemandId();
                }
            };
    private boolean isRead;

    public FullOfferDetail() {
    }

    public FullOfferDetail(FullOfferDetail detail) {
        this.updateWholeOffer(detail);
    }

    public void updateWholeOffer(FullOfferDetail detail) {
        if (detail == null) {
            return;
        }
        offerDetail.updateWholeOfferDetail(detail.offerDetail);
        messageDetail.updateWholeMessage(detail.getMessageDetail());
    }

    public MessageDetail getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(MessageDetail userMessageDetail) {
        this.messageDetail = userMessageDetail;
    }

    public OfferDetail getOfferDetail() {
        return offerDetail;
    }

    public void setOfferDetail(OfferDetail offerDetail) {
        this.offerDetail = offerDetail;
    }

    public FullDemandDetail getDemandDetail() {
        return demandDetail;
    }

    public void setDemandDetail(FullDemandDetail demandDetail) {
        this.demandDetail = demandDetail;
    }

    @Override
    public boolean isStarred() {
        return this.messageDetail.isStarred();
    }

    @Override
    public void setIsStarred(boolean value) {
        this.messageDetail.setStarred(value);
    }

    @Override
    public Date getEndDate() {
        return this.offerDetail.getFinishDate();
    }

    @Override
    public DemandStatus getDemandStatus() {
        return demandDetail.getDemandStatus();
    }

    @Override
    public OfferStateType getOfferState() {
        return this.offerDetail.getState();
    }

    @Override
    public long getClientId() {
        return demandDetail.getClientId();
    }

    @Override
    public String getClientName() {
        return offerDetail.getClientName();
    }

    @Override
    public long getSupplierId() {
        return offerDetail.getSupplierId();
    }

    @Override
    public String getSupplierName() {
        return offerDetail.getSupplierName();
    }

    @Override
    public int getRating() {
        return offerDetail.getRating();
    }

    @Override
    public long getMessageId() {
        return messageDetail.getMessageId();
    }

    @Override
    public long getThreadRootId() {
        return messageDetail.getThreadRootId();
    }

    @Override
    public long getSenderId() {
        return messageDetail.getSenderId();
    }

    @Override
    public Date getMessageSent() {
        return messageDetail.getSent();
    }

    @Override
    public long getUserMessageId() {
        return messageDetail.getUserMessageId();
    }

    @Override
    public void setUserMessageId(long userMessageId) {
        this.messageDetail.setUserMessageId(userMessageId);
    }

    @Override
    public int getMessageCount() {
        return messageCount;
    }

    @Override
    public int getUnreadMessageCount() {
        return unreadSubmessages;
    }

    @Override
    public Date getDeliveryDate() {
        return new Date();
    }

    @Override
    public long getDemandId() {
        return demandDetail.getDemandId();
    }

    @Override
    public Date getValidTo() {
        return demandDetail.getValidToDate();
    }

    @Override
    public Date getReceivedDate() {
        return demandDetail.getCreated();
    }

    @Override
    public String getTitle() {
        return demandDetail.getTitle();
    }

    @Override
    public String getPrice() {
        return demandDetail.getPrice().toString();
    }

    @Override
    public void setMessageCount(int messageCount) {
        this.messageCount = messageCount;
    }

    public void setUnreadSubmessages(int unreadSubmessages) {
        this.unreadSubmessages = unreadSubmessages;
    }

    @Override
    public long getOfferId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRead() {
        return isRead;
    }

    @Override
    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }
}
