package com.eprovement.poptavka.shared.domain.demand;

import java.math.BigDecimal;
import java.util.Date;

import com.eprovement.poptavka.shared.domain.type.DemandDetailType;
import com.google.gwt.user.client.rpc.IsSerializable;


public class BaseDemandDetail implements IsSerializable {

    private DemandDetailType detailType = DemandDetailType.BASE;

    private long demandId;
    // messageId = threadRoot
    private long messageId;
    private long userMessageId;
    private boolean read;
    private boolean starred;
    private Date endDate;
    private Date validToDate;

    private String title;
    private String description;
    private BigDecimal price;



    public BaseDemandDetail() {    }

    public long getDemandId() {
        return demandId;
    }
    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }
    public long getMessageId() {
        return messageId;
    }
    /**
     * @param messageId for demand object is ALWAYS threadRootId
     */
    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }
    public long getUserMessageId() {
        return userMessageId;
    }
    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }
    public boolean isStarred() {
        return starred;
    }
    public void setStarred(boolean starred) {
        this.starred = starred;
    }
    public Date getEndDate() {
        return endDate;
    }
    public void setEndDate(Date finishDate) {
        this.endDate = finishDate;
    }
    public Date getValidToDate() {
        return validToDate;
    }
    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public BigDecimal getPrice() {
        return price;
    }
    public String displayPrice() {
        if (price == null) {
            return "";
        }
        return price.toString();
    }
    public void setPrice(String price) {
        if (price.equals("") || price.equals("null")) {
            this.price = null;
        } else {
            this.price = BigDecimal.valueOf(Long.valueOf(price));
        }
    }
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String toString() {
        return "\nGlobal Demand Detail Info"
                + "\n- BaseDemandDetail:"
                + "\n    demandId="
                + demandId + "\n     title="
                + title + "\n    Description="
                + description + "\n  Price="
                + price + "\n    endDate="
                + endDate + "\n  validToDate="
                + validToDate + "\n  read="
                + read + "\n     isStarred="
                + starred + "\n  detailType="
                + detailType + "\n";
    }

    public void setType(DemandDetailType detailType) {
        this.detailType = detailType;
    }

    public DemandDetailType getType() {
        return detailType;
    }

}
