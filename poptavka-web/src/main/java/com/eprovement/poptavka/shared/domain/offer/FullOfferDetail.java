package com.eprovement.poptavka.shared.domain.offer;

import com.eprovement.poptavka.shared.domain.converter.MessageConverter;
import com.eprovement.poptavka.shared.domain.converter.OfferConverter;
import java.io.Serializable;

import com.google.gwt.core.client.GWT;

import com.eprovement.poptavka.domain.message.Message;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.message.MessageDetail;

public class FullOfferDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -563380651738612866L;
    // TODO remove dipsplayed
    private boolean isRead;
    private MessageDetail messageDetail;
    private OfferDetail offerDetail;

    public static FullOfferDetail createOfferDetail(Message message) {
        FullOfferDetail detail = new FullOfferDetail();
        if (message == null) {
            return detail;
        }
        detail.setMessageDetail(new MessageConverter().convertToTarget(message));
        detail.setOfferDetail(new OfferConverter().convertToTarget(message.getOffer()));

        detail.setIsRead(true);

        GWT.log("OFFER ID: " + message.getId() + ", OFFER DETAIL ID: " + message.getOffer().getId());
        return detail;
    }

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
        isRead = detail.isRead();
    }

    public boolean isRead() {
        return isRead;
    }

    public void setIsRead(boolean isRead) {
        this.isRead = isRead;
    }

    public MessageDetail getMessageDetail() {
        return messageDetail;
    }

    public void setMessageDetail(MessageDetail messageDetail) {
        this.messageDetail = messageDetail;
    }

    public OfferDetail getOfferDetail() {
        return offerDetail;
    }

    public void setOfferDetail(OfferDetail offerDetail) {
        this.offerDetail = offerDetail;
    }
}
