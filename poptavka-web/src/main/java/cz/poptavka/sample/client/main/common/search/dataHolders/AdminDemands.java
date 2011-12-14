package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;
import java.util.Date;

/** ADMINDEMAND **/
public class AdminDemands implements Serializable {

    private Long demandIdFrom = null;
    private Long demandIdTo = null;
    private Long clientIdFrom = null;
    private Long clientIdTo = null;
    private String demandTitle = null;
    private String demandType = null;
    private String demandStatus = null;
    private Date expirationDateFrom = null;
    private Date expirationDateTo = null;
    private Date endDateFrom = null;
    private Date endDateTo = null;

    public Long getClientIdFrom() {
        return clientIdFrom;
    }

    public void setClientIdFrom(Long clientIdFrom) {
        this.clientIdFrom = clientIdFrom;
    }

    public Long getClientIdTo() {
        return clientIdTo;
    }

    public void setClientIdTo(Long clientIdTo) {
        this.clientIdTo = clientIdTo;
    }

    public Long getDemandIdFrom() {
        return demandIdFrom;
    }

    public void setDemandIdFrom(Long demandIdFrom) {
        this.demandIdFrom = demandIdFrom;
    }

    public Long getDemandIdTo() {
        return demandIdTo;
    }

    public void setDemandIdTo(Long demandIdTo) {
        this.demandIdTo = demandIdTo;
    }

    public String getDemandStatus() {
        return demandStatus;
    }

    public void setDemandStatus(String demandStatus) {
        this.demandStatus = demandStatus;
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    public String getDemandType() {
        return demandType;
    }

    public void setDemandType(String demandType) {
        this.demandType = demandType;
    }

    public Date getEndDateFrom() {
        return endDateFrom;
    }

    public void setEndDateFrom(Date endDateFrom) {
        this.endDateFrom = endDateFrom;
    }

    public Date getEndDateTo() {
        return endDateTo;
    }

    public void setEndDateTo(Date endDateTo) {
        this.endDateTo = endDateTo;
    }

    public Date getExpirationDateFrom() {
        return expirationDateFrom;
    }

    public void setExpirationDateFrom(Date expirationDateFrom) {
        this.expirationDateFrom = expirationDateFrom;
    }

    public Date getExpirationDateTo() {
        return expirationDateTo;
    }

    public void setExpirationDateTo(Date expirationDateTo) {
        this.expirationDateTo = expirationDateTo;
    }
}