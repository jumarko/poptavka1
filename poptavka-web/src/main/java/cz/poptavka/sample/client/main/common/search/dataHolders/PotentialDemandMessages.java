package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;
import java.util.Date;

/** POTENTIALDEMANDMESSAGES **/
public class PotentialDemandMessages implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private Boolean isStared = null;
    private String sender = null;
    private String demandTitle = null;
    private String urgention = null;
    private Integer ratingFrom = null;
    private Integer ratingTo = null;
    private Integer priceFrom = null;
    private Integer priceTo = null;
    private Date createdFrom = null;
    private Date createdTo = null;

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    public Boolean getIsStared() {
        return isStared;
    }

    public void setIsStared(Boolean isStared) {
        this.isStared = isStared;
    }

    public Integer getPriceFrom() {
        return priceFrom;
    }

    public void setPriceFrom(Integer priceFrom) {
        this.priceFrom = priceFrom;
    }

    public Integer getPriceTo() {
        return priceTo;
    }

    public void setPriceTo(Integer priceTo) {
        this.priceTo = priceTo;
    }

    public Integer getRatingFrom() {
        return ratingFrom;
    }

    public void setRatingFrom(Integer ratingFrom) {
        this.ratingFrom = ratingFrom;
    }

    public Integer getRatingTo() {
        return ratingTo;
    }

    public void setRatingTo(Integer ratingTo) {
        this.ratingTo = ratingTo;
    }

    public Date getCreatedFrom() {
        return createdFrom;
    }

    public void setCreatedFrom(Date createdFrom) {
        this.createdFrom = createdFrom;
    }

    public Date getCreatedTo() {
        return createdTo;
    }

    public void setCreatedTo(Date createdTo) {
        this.createdTo = createdTo;
    }

    public String getUrgention() {
        return urgention;
    }

    public void setUrgention(String urgention) {
        this.urgention = urgention;
    }
}