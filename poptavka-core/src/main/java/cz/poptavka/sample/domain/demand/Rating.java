package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * The rating of supply of some demand.
 *
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@Entity
public class Rating extends DomainObject {

    private int rating;

    private String message;

    @Temporal(value = TemporalType.TIMESTAMP)
    private Date createdAt;

    private String reply;


    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Rating");
        sb.append("{rating=").append(rating);
        sb.append(", message='").append(message).append('\'');
        sb.append(", createdAt=").append(createdAt);
        sb.append(", reply='").append(reply).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
