package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.domain.message.UserMessage;
import cz.poptavka.sample.shared.domain.type.MessageType;

public class PotentialDemandMessageImpl extends MessageDetailImpl implements Serializable, PotentialDemandMessage {

    private long userMessageId;
    private BigDecimal price;
    private boolean read;
    private boolean starred;
    private Date endDate;
    private Date validToDate;

    public static PotentialDemandMessage createMessageDetail(UserMessage message) {
        PotentialDemandMessage detail = new PotentialDemandMessageImpl();
        detail = (PotentialDemandMessage) MessageDetailImpl.fillMessageDetail(detail, message.getMessage());
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
        if (price == null) {
            this.price = BigDecimal.ZERO;
        } else {
            this.price = price;
        }
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

    @Override
    public MessageType getMessageType() {
        return MessageType.POTENTIAL_DEMAND;
    }


}
