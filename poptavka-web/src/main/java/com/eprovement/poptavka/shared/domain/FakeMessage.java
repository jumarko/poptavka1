package com.eprovement.poptavka.shared.domain;

import java.io.Serializable;

public class FakeMessage implements Serializable {

    private static final long serialVersionUID = -1593439444967380366L;

    private String author;
    private String date;
    private String messageBody;

    public FakeMessage() {
        this.author = "Sam Hough (salk31)";
        this.date = "Oct 3, 2007";
        this.messageBody = "Since gwt-html is a bit of an underdog and html version of app will probably always be a "
            + "secondary concern for users I think EventFormPanel? will put a lot of people off. Maybe forcing the"
            + " whole page into a single form will give more consistent state semantics (state of fields is not lost"
            + "no matter what you click). This will break some CSS (based on other form tags and form field names) "
            + "but that is possible to work around. Similarly iframes etc should probably be flattened into the parent"
            + " HTML as GWT events in the iframe could update the parent."
            + "<br />"
            + " A workable design alternative might be to use htmlunit as a sort of proxy. The performance would really"
            + "suck compared to the original design but vanilla GWT should work including "
            + " parts. htmlunit has things like dyntable working already although kitchen sink fails. I've got a very "
            + "rough prototype that has some promise. I'll try and get the kitchen sink working before tidying up and "
            + "publishing source. I'm really smug about my HTML version of alert in HelloWorld? ;) Many thanks Gergely";
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMessageBody() {
        return messageBody;
    }

    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }


}
