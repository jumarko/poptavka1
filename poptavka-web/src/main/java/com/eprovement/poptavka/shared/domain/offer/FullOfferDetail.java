package com.eprovement.poptavka.shared.domain.offer;

import com.eprovement.poptavka.client.user.widget.grid.IUniversalDetail;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import com.eprovement.poptavka.shared.domain.adminModule.OfferDetail;
import com.eprovement.poptavka.shared.domain.demand.FullDemandDetail;
import com.eprovement.poptavka.shared.domain.message.TableDisplay;
import com.eprovement.poptavka.shared.domain.message.UserMessageDetail;
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
    private boolean isRead;
    private UserMessageDetail userMessageDetail = new UserMessageDetail();
    private OfferDetail offerDetail = new OfferDetail();
    private FullDemandDetail demandDetail = new FullDemandDetail();
    //Keyprovider
    public static final ProvidesKey<FullOfferDetail> KEY_PROVIDER =
            new ProvidesKey<FullOfferDetail>() {
                @Override
                public Object getKey(FullOfferDetail item) {
                    return item == null ? null : item.getOfferDetail().getDemandId();
                }
            };

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
    public String displayHtml(String trustedHtml, boolean isRead) {
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
        return userMessageDetail.getMessageDetail().getMessageId();
    }

    @Override
    public long getThreadRootId() {
        return userMessageDetail.getMessageDetail().getThreadRootId();
    }

    @Override
    public long getSenderId() {
        return userMessageDetail.getMessageDetail().getSenderId();
    }

    @Override
    public Date getMessageSent() {
        return userMessageDetail.getMessageDetail().getSent();
    }

    @Override
    public long getUserMessageId() {
        return userMessageDetail.getId();
    }

    @Override
    public int getMessageCount() {
        return userMessageDetail.getMessageCount();
    }

    @Override
    public int getUnreadMessageCount() {
        return userMessageDetail.getUnreadMessageCount();
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
}
