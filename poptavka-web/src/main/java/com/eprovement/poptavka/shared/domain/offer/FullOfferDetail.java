package com.eprovement.poptavka.shared.domain.offer;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import java.io.Serializable;

import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
import java.util.Date;

public class FullOfferDetail implements Serializable, TableDisplay {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -563380651738612866L;
    // TODO remove dipsplayed
    private boolean isRead;
    private UserMessageDetail userMessageDetail = new UserMessageDetail();
    private OfferDetail offerDetail = new OfferDetail();
    private FullDemandDetail demandDetail = new FullDemandDetail();

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
        userMessageDetail.updateWholeUserMessage(detail.getUserMessageDetail());
        isRead = detail.isRead();
    }

    public UserMessageDetail getUserMessageDetail() {
        return userMessageDetail;
    }

    public void setUserMessageDetail(UserMessageDetail userMessageDetail) {
        this.userMessageDetail = userMessageDetail;
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

    /**
     * Display string as HTML. We suppose calling of this method always come from trusted (programmed) source.
     * User CANNOT call this nethod due to security issues.
     * @param trustedHtml
     * @return string in html tags
     */
    public static String displayHtml(String trustedHtml, boolean isRead) {
        if (isRead) {
            return trustedHtml;
        } else {
            return "<strong>" + trustedHtml + "</strong>";
        }
    }

    @Override
    public boolean isRead() {
        return this.userMessageDetail.isRead();
    }

    @Override
    public void setRead(boolean value) {
        this.userMessageDetail.setRead(value);
    }

    @Override
    public boolean isStarred() {
        return this.userMessageDetail.isStarred();
    }

    @Override
    public void setStarred(boolean value) {
        this.userMessageDetail.setStarred(value);
    }

    @Override
    public Date getEndDate() {
        return this.offerDetail.getFinishDate();
    }

    @Override
    public DemandStatus getDemandStatus() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public OfferStateType getOfferState() {
        return this.offerDetail.getState();
    }
}
