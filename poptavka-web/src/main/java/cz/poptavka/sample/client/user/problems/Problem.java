package cz.poptavka.sample.client.user.problems;

import cz.poptavka.sample.domain.message.Message;

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
    private Message root;
    private List<Message> answers;

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

    public void setRoot(Message root) {
        this.root = root;
    }

    public Message getRoot() {
        return this.root;
    }

    public void setAnswers(List<Message> answers) {
        this.answers = answers;
    }

    public List<Message> getAnswers() {
        return this.answers;
    }
}
