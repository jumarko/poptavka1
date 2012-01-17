package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;

/** MESSAGES - INBOX, SENT, DELETED **/
public class MessagesTab implements Serializable {

    private String subject = null;
    private String sender = null;
    private String body = null;
    private String created = null;

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

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getCreated() {
        return created;
    }

    public void setCreated(String created) {
        this.created = created;
    }
}