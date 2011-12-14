package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;
import java.util.Date;

/** MESSAGES - INBOX, SENT, DELETED **/
public class MessagesTab implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private Boolean isStar = null;
    private String subject = null;
    private String sender = null;
    private Date createFrom = null;
    private Date createTo = null;

    public Date getCreateFrom() {
        return createFrom;
    }

    public void setCreateFrom(Date createFrom) {
        this.createFrom = createFrom;
    }

    public Date getCreateTo() {
        return createTo;
    }

    public void setCreateTo(Date createTo) {
        this.createTo = createTo;
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

    public Boolean getIsStar() {
        return isStar;
    }

    public void setIsStar(Boolean isStar) {
        this.isStar = isStar;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}