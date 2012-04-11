package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.activation.EmailActivation;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents full detail of domain object <b>EmailActivation</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 *
 */
public class EmailActivationDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private Long id;
    private String activationLink;
    private Date timeout;

    /** for serialization. **/
    public EmailActivationDetail() {
    }

    public EmailActivationDetail(EmailActivationDetail demand) {
        this.updateWholeEmailActivation(demand);
    }

    /**
     * Method created <b>EmailActivationDetail</b> from provided Demand domain object.
     * @param domain - given domain object
     * @return EmailActivationDetail - created detail object
     */
    public static EmailActivationDetail createEmailActivationDetail(EmailActivation domain) {
        EmailActivationDetail detail = new EmailActivationDetail();

        detail.setId(domain.getId());
        detail.setActivationLink(domain.getActivationLink());
        detail.setTimeout(domain.getTimeout());

        return detail;
    }

    /**
     * Method created domain object <b>EmailActivation</b> from provided <b>EmailActivationDetail</b> object.
     * @param domain - domain object to be updated
     * @param detail - detail object which provides updated data
     * @return EmailActivation - updated given domain object
     */
    public static EmailActivation updateEmailActivation(EmailActivation domain, EmailActivationDetail detail) {
        if (!domain.getActivationLink().equals(detail.getActivationLink())) {
            domain.setActivationLink(detail.getActivationLink());
        }
        if (!domain.getTimeout().equals(detail.getTimeout())) {
            domain.setTimeout(detail.getTimeout());
        }
        return domain;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeEmailActivation(EmailActivationDetail emailActivationDetail) {
        id = emailActivationDetail.getId();
        activationLink = emailActivationDetail.getActivationLink();
        timeout = emailActivationDetail.getTimeout();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivationLink() {
        return activationLink;
    }

    public void setActivationLink(String name) {
        this.activationLink = name;
    }

    public Date getTimeout() {
        return timeout;
    }

    public void setTimeout(Date timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        return "\nGlobal EmailActivation Detail Info:"
                + "\n    DemandTypeId=" + Long.toString(id)
                + "\n    Name=" + activationLink
                + "\n    Description=" + timeout;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final EmailActivationDetail other = (EmailActivationDetail) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
