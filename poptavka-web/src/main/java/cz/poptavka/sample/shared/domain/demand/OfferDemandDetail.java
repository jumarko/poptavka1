/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.poptavka.sample.shared.domain.demand;

import java.io.Serializable;


/**
 * Servers for OFFER view.
 *
 * @author ivan.vlcek
 */
public class OfferDemandDetail extends BaseDemandDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -927462467233123906L;
    private int numberOfOffers;
    private int maxOffers;

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

}
