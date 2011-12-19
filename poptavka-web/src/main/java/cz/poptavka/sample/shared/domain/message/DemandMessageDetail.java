package cz.poptavka.sample.shared.domain.message;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.domain.demand.DemandStatus;
import cz.poptavka.sample.domain.demand.DemandType;
import cz.poptavka.sample.domain.message.UserMessage;

public class DemandMessageDetail extends MessageDetail implements
        Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -6105359783491407143L;
    private String demandTitle;
    private BigDecimal price;
    private Date endDate;
    private Date validToDate;
    private long demandId;

    private DemandType demandType;

    private DemandStatus demandStatus;

    public static DemandMessageDetail createMessageDetail(UserMessage message) {
        return fillMessageDetail(new DemandMessageDetail(), message);
    }

    public static DemandMessageDetail fillMessageDetail(DemandMessageDetail detail,
            UserMessage userMessage) {
        MessageDetail.fillMessageDetail(detail, userMessage);
        detail.setDemandId(userMessage.getMessage().getDemand().getId());
        detail.setPrice(userMessage.getMessage().getDemand().getPrice());
        detail.setEndDate(userMessage.getMessage().getDemand().getEndDate());
        detail.setValidToDate(userMessage.getMessage().getDemand().getValidTo());
        detail.setDemandType(userMessage.getMessage().getDemand().getType());
        detail.setDemandStatus(userMessage.getMessage().getDemand().getStatus());
        return detail;
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    public void setDemandStatus(DemandStatus status) {
        this.demandStatus = status;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setValidToDate(Date validTo) {
        this.validToDate = validTo;
    }

    public Date getValidToDate() {
        return validToDate;
    }

    public void setPrice(BigDecimal price) {
        if (price == null) {
            this.price = BigDecimal.ZERO;
        } else {
            this.price = price;
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public DemandType getDemandType() {
        return demandType;
    }

    public void setDemandType(DemandType demandType) {
        this.demandType = demandType;
    }

    public String getDemandPrice() {
        return price.toString();
    }

    public DemandStatus getDemandStatus() {
        return demandStatus;
    }

    public Date getExpireDate() {
        return null;
    }

}
