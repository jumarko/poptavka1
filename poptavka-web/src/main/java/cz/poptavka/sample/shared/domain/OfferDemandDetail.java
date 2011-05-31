/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author ivan.vlcek
 */
public class OfferDemandDetail {

    private long demandId;
    private int numberOfOffers;
    private int maxOffers;
    private String title;
    private BigDecimal price;
    private Date endDate;
    private Date finishDate;
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
        return finishDate;
    }

    /**
     * @param finishDate the finishDate to set
     */
    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
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
                + ", finishDate=" + finishDate + '}';
    }
}
