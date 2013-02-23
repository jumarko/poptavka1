/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.demand;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.math.BigDecimal;
import java.util.Date;


/**
 * Servers for OFFER view.
 *
 * @author ivan.vlcek
 */
public class OfferDemandDetail implements IsSerializable {

    private int numberOfOffers;
    private int maxOffers;

    // refactoring
    private long demandId;
    // messageId = threadRoot
    private long messageId;
//    private long userMessageId;
//    private boolean read;
//    private boolean starred;
    private Date endDate;
    private Date validToDate;
//
    private String title;
//    private String description;
    private BigDecimal price;




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

    @Override
    public String toString() {
        return "OffersDemandDetail{"
                + ", numberOfOffers=" + numberOfOffers
                + ", maxOffers=" + maxOffers
                + "}";
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
     * @return the validToDate
     */
    public Date getValidToDate() {
        return validToDate;
    }

    /**
     * @param validToDate the validToDate to set
     */
    public void setValidToDate(Date validToDate) {
        this.validToDate = validToDate;
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

}
