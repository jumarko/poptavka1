package cz.poptavka.sample.shared.domain.demand;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;


public class BaseDemandDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 7279268325707740135L;
    /**
     * Constants for html display of demands in view tables.
     */
    private static final String HTML_READ = "";
    private static final String HTML_UNREAD_START = "<strong>";
    private static final String HTML_UNREAD_END = "</strong>";

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

    private String htmlDisplayStart = HTML_UNREAD_START;
    private String htmlDisplayEnd = HTML_UNREAD_END;

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
        if (read) {
            this.htmlDisplayStart = HTML_READ;
            this.htmlDisplayEnd = HTML_READ;
        } else {
            this.htmlDisplayStart = HTML_UNREAD_START;
            this.htmlDisplayEnd = HTML_UNREAD_END;
        }
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
    public String displayDescription() {
        return htmlDisplay(description);
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
    public String displayPriceHtml() {
        return htmlDisplay(displayPrice());
    }

    public void setPrice(String price) {
        if (price.equals("")) {
            this.price = null;
        } else {
            this.price = BigDecimal.valueOf(Long.valueOf(price));
        }
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "BaseDemandDetail{"
                + demandId + "\n demandId="
                + title + "\n title="
                + description + "\n Description="
                + price + "\n Price="
                + endDate + "\n endDate="
                + validToDate + "\n validToDate="
                + read + "\n isRead="
                + starred + "\n isStarred="
                + '}';
    }

    /**
     * Display string as HTML. We suppose calling of this method always come from trusted (programmed) source.
     * User CANNOT call this nethod due to security issues.
     * @param trustedHtml
     * @return string in html tags
     */
    protected String htmlDisplay(String trustedHtml) {
        StringBuilder sb = new StringBuilder()
            .append(htmlDisplayStart)
            .append(trustedHtml)
            .append(htmlDisplayEnd);
        return sb.toString();
    }

    /**
     * HTML read representation of demandTitle.
     */
    public String displayTitle() {
        return htmlDisplay(title);
    }

    /**
     * HTML read representation of endDate.
     */
    public String displayFinishDate() {
        return endDate.toString();
        // TODO wrong
//        return htmlDisplay(dateFormat.format(endDate));
    }

    /**
     * HTML read representation of endDate.
     */
    public String displayValidToDate() {
        return validToDate.toString();
        // TODO wrong
//        return htmlDisplay(dateFormat.format(validToDate));
    }

}
