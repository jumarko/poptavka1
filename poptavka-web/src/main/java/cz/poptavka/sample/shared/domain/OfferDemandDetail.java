/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ivan.vlcek
 */
public class OfferDemandDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -927462467233123906L;
    private long demandId;
    private long threadRootId;
    private int numberOfOffers;
    private int maxOffers;
    private String title;
    private BigDecimal price;
    private Date endDate;
    private Date validTo;
    private boolean bold;

    /**
     * Get the value of bold
     *
     * @return the value of bold
     */
    public boolean isBold() {
        return bold;
    }

    /**
     * Set the value of bold
     *
     * @param bold new value of bold
     */
    public void setBold(boolean bold) {
        this.bold = bold;
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
     * @return the numberOfOffers
     */
    public int getNumberOfOffers() {
        return numberOfOffers;
    }

    /**
     * @param numberOfOffers the numberOfOffers to set
     */
    public void setNumberOfOffers(int numberOfOffers) {
        this.numberOfOffers = numberOfOffers;
    }

    /**
     * @return the maxOffers
     */
    public int getMaxOffers() {
        return maxOffers;
    }

    /**
     * @param maxOffers the maxOffers to set
     */
    public void setMaxOffers(int maxOffers) {
        this.maxOffers = maxOffers;
    }

    /**
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
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
     * @return the finishDate
     */
    public Date getFinishDate() {
        return validTo;
    }

    /**
     * @param finishDate the finishDate to set
     */
    public void setValidToDate(Date finishDate) {
        this.validTo = finishDate;
    }

    @Override
    public String toString() {
        return "OffersDemandDetail{"
                + "demandId=" + demandId
                + ", numberOfOffers=" + numberOfOffers
                + ", maxOffers=" + maxOffers
                + ", title=" + title
                + ", price=" + price
                + ", endDate=" + endDate
                + ", threadRootId=" + threadRootId
                + ", finishDate=" + validTo + '}';
    }

    /**
     * @return the threadRootId
     */
    public long getThreadRootId() {
        return threadRootId;
    }

    /**
     * @param threadRootId the threadRootId to set
     */
    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }
}
