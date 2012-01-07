package cz.poptavka.sample.shared.domain.demand;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.domain.demand.Demand;
import cz.poptavka.sample.shared.domain.type.DemandDetailType;


public class BaseDemandDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7279268325707740135L;
    /**
     * Constants for html display of demands in view tables.
     */
    private static final String HTML_UNREAD_START = "<strong>";
    private static final String HTML_UNREAD_END = "</strong>";
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

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param demand
     * @return FullDemandDetail
     */
    public static BaseDemandDetail createDemandDetail(Demand demand) {
        return fillDemandDetail(new BaseDemandDetail(), demand);
    }

    public static BaseDemandDetail fillDemandDetail(BaseDemandDetail detail, Demand demand) {
        detail.setDemandId(demand.getId());
        detail.setTitle(demand.getTitle());
        detail.setDescription(demand.getDescription());
        detail.setPrice(demand.getPrice());
        detail.setEndDate(demand.getEndDate());
        detail.setValidToDate(demand.getValidTo());
        return detail;
    }


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
    public boolean isRead() {
        return read;
    }
    public void setRead(boolean read) {
        this.read = read;
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
            return HTML_UNREAD_START + trustedHtml + HTML_UNREAD_END;
        }
    }

    public void setType(DemandDetailType detailType) {
        this.detailType = detailType;
    }

    public DemandDetailType getType() {
        return detailType;
    }

}
