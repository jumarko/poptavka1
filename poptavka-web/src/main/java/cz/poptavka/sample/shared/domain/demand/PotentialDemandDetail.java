/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.demand;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ivan.vlcek
 */
public class PotentialDemandDetail implements Serializable {

    private static final String HTML_READ = "";
    private static final String HTML_UNREAD_START = "<strong>";
    private static final String HTML_UNREAD_END = "</strong>";
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -2609824637843192491L;

    private long demandId;
    private long messageId;
    private long userMessageId;
    private boolean read;
    private boolean starred;
    private String clientName;
    private int numberOfReplies;
    private String demandTitle;
    /* cutDescription may contain only few characters to fill up the whole row. */
    private String cutDescription;
    private Date sent;
    private Date endDate;
    /* numberOfReplies designates the number of all messages in this thread. */
    private BigDecimal price;
    private String demandStatus;

    private String htmlDisplayStart = HTML_UNREAD_START;
    private String htmlDisplayEnd = HTML_UNREAD_END;

    /** for serialization. **/
    public PotentialDemandDetail() {
    }

    /**
     * @return the demandId
     */
    public long getDemandId() {
        return demandId;
    }

    /**
     * @param demandId the demandId to set
     */
    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    /**
     * @return the messageId
     */
    public long getMessageId() {
        return messageId;
    }

    /**
     * @param messageId the messageId to set
     */
    public void setMessageId(long messageId) {
        this.messageId = messageId;
    }

    /**
     * @return the userMessageId
     */
    public long getUserMessageId() {
        return userMessageId;
    }

    /**
     * @param userMessageId the userMessageId to set
     */
    public void setUserMessageId(long userMessageId) {
        this.userMessageId = userMessageId;
    }

    /**
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read true, if is read, or was read in the past
     */
    public void setRead(boolean isRead) {
        this.read = isRead;
        if (read) {
            this.htmlDisplayStart = HTML_READ;
            this.htmlDisplayEnd = HTML_READ;
        } else {
            this.htmlDisplayStart = HTML_UNREAD_START;
            this.htmlDisplayEnd = HTML_UNREAD_END;
        }
    }

    /**
     * @return the starred
     */
    public boolean isStarred() {
        return starred;
    }

    /**
     * @param starred the starred to set
     */
    public void setStarred(boolean isStarred) {
        this.starred = isStarred;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }
    public String getClientNameHtml() {
        return htmlDisplay(clientName);
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    /**
     * @return the numberOfReplies
     */
    public int getNumberOfReplies() {
        return numberOfReplies;
    }

    /**
     * @param numberOfReplies the numberOfReplies to set
     */
    public void setNumberOfReplies(int numberOfReplies) {
        this.numberOfReplies = numberOfReplies;
    }

    /**
     * @return the demandTitle
     */
    public String getDemandTitle() {
        return demandTitle;
    }

    public String getDemandTitleHtml() {
        return htmlDisplay(demandTitle);
    }

    /**
     * @param demandTitle the demandTitle to set
     */
    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    /**
     * @return the cutDescription
     */
    public String getCutDescription() {
        return cutDescription;
    }

    /**
     * @param cutDescription the cutDescription to set
     */
    public void setCutDescription(String cutDescription) {
        this.cutDescription = cutDescription;
    }

    /**
     * @return the sent
     */
    public Date getSent() {
        return sent;
    }

    /**
     * @param sent the sent to set
     */
    public void setSent(Date sent) {
        this.sent = sent;
    }

    /**
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the price
     */
    public BigDecimal getPrice() {
        return price;
    }

    /**
     * @param price the price to set
     */
    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    /**
     * @return the demandStatus
     */
    public String getDemandStatus() {
        return demandStatus;
    }

    /**
     * @param demandStatus the demandStatus to set
     */
    public void setDemandStatus(String demandStatus) {
        this.demandStatus = demandStatus;
    }

    /**
     * Display string as HTML. We suppose calling of this method always come from trusted (programmed) source.
     * User CANNOT call this nethod due to security issues.
     * @param trustedHtml
     * @return string in html tags
     */
    public String htmlDisplay(String trustedHtml) {
        StringBuilder sb = new StringBuilder()
            .append(htmlDisplayStart)
            .append(trustedHtml)
            .append(htmlDisplayEnd);
        return sb.toString();
    }

}
