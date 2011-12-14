package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;
import java.util.Date;

/** ADMINEMAILACTIVATION **/
public class AdminEmailActivation implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private String activationLink = null;
    private Date timeoutFrom = null;
    private Date timeoutTo = null;

    public String getActivationLink() {
        return activationLink;
    }

    public void setActivationLink(String activationLink) {
        this.activationLink = activationLink;
    }

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

    public Date getTimeoutFrom() {
        return timeoutFrom;
    }

    public void setTimeoutFrom(Date timeoutFrom) {
        this.timeoutFrom = timeoutFrom;
    }

    public Date getTimeoutTo() {
        return timeoutTo;
    }

    public void setTimeoutTo(Date timeoutTo) {
        this.timeoutTo = timeoutTo;
    }
}