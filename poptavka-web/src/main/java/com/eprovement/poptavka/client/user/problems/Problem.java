package com.eprovement.poptavka.client.user.problems;

import com.eprovement.poptavka.shared.domain.message.MessageDetail;
import java.util.Date;
import java.util.List;

/**
 * Private class for Fake Data.
 *
 * @author Martin Slavkovsky
 *
 */
public class Problem {
    private String demandName;
    private String state;
    private Date date;
    private String price;
    private MessageDetail root;
    private List<MessageDetail> answers;

    public Problem(String demandName, String state, Date date, String price) {
        this.demandName = demandName;
        this.state = state;
        this.date = date;
        this.price = price;
    }

    public String getDemandName() {
        return demandName;
    }

    public String getState() {
        return state;
    }

    public Date getDate() {
        return date;
    }

    public String getPrice() {
        return price;
    }

    public void setRoot(MessageDetail root) {
        this.root = root;
    }

    public MessageDetail getRoot() {
        return this.root;
    }

    public void setAnswers(List<MessageDetail> answers) {
        this.answers = answers;
    }

    public List<MessageDetail> getAnswers() {
        return this.answers;
    }
}
