package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.domain.message.UserMessage;

public class PotentialMessageDetailImpl extends MessageDetailImpl implements Serializable, PotentialMessageDetail {

    private long userMessageId;
    private BigDecimal price;
    private boolean read;
    private boolean starred;
    private Date endDate;
    private Date validToDate;

    public static PotentialMessageDetail createMessageDetail(UserMessage message) {
        PotentialMessageDetail detail = new PotentialMessageDetailImpl();
        detail = (PotentialMessageDetail) MessageDetailImpl.fillMessageDetail(detail, message.getMessage());
        detail.setUserMessageId(message.getId());
        detail.setDemandId(message.getMessage().getDemand().getId());
        detail.setPrice(message.getMessage().getDemand().getPrice());
        detail.setRead(message.isIsRead());
        detail.setStarred(message.isIsStarred());
        detail.setEndDate(message.getMessage().getDemand().getEndDate());
        detail.setValidToDate(message.getMessage().getDemand().getValidTo());
        return detail;
    }

    @Override
    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean isRead() {
        return read;
    }

    @Override
    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    @Override
    public boolean isStarred() {
        return starred;
    }

    @Override
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    @Override
    public Date getEndDate() {
        return endDate;
    }

    @Override
    public void setValidToDate(Date validTo) {
        this.validToDate = validTo;
    }

    @Override
    public Date getValidToDate() {
        return validToDate;
    }

    @Override
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public void setUserMessageId(Long userMessageId) {
        this.userMessageId = userMessageId;
    }

    @Override
    public Long getUserMessageId() {
        return userMessageId;
    }


}
