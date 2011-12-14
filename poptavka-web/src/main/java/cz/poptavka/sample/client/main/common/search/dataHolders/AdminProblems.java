package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;

/** ADMINPROBLEMS **/
public class AdminProblems implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private String text = null;

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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}