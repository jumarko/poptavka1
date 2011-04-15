/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.mail;


import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.user.User;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Vojtech Hubr
 *         Date 12.4.11
 *
 * User's contact, i.e. information a user stores for (usually) another
 * user in their contact list.
 * If the two users (the one that saves the <code>UserContact</code> and
 * the one that is represented by the <code>UserContact</code> are the same,
 * that it will be used as a template.
 */
@Entity
public class UserContact extends DomainObject {

    private String displayName;

    private String contactStatus;

    private String jobTitle;

    private String note;
    /* the user in whose contact list the given user appears */
    @ManyToOne
    private User savedBy;
    /* the user whom this entity represents */
    @ManyToOne
    private User represents;
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date created;

    public String getContactStatus() {
        return contactStatus;
    }

    public void setContactStatus(String contactStatus) {
        this.contactStatus = contactStatus;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public User getRepresents() {
        return represents;
    }

    public void setRepresents(User represents) {
        this.represents = represents;
    }

    public User getSavedBy() {
        return savedBy;
    }

    public void setSavedBy(User savedBy) {
        this.savedBy = savedBy;
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserContact");
        sb.append("{displayName='").append(displayName).append('\'');
        sb.append("{savedBy='").append(savedBy.getLogin()).append('\'');
        sb.append("{savedBy='").append(savedBy.getLogin()).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
