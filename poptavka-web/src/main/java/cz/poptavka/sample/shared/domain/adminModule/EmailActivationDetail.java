package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.activation.EmailActivation;

import java.io.Serializable;
import java.util.Date;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
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
     * Method created FullDemandDetail from provided Demand domain object.
     * @param demand
     * @return DemandDetail
     */
    public static EmailActivationDetail createEmailActivationDetail(EmailActivation demand) {
        EmailActivationDetail detail = new EmailActivationDetail();

        detail.setId(demand.getId());
        detail.setActivationLink(demand.getActivationLink());
        detail.setTimeout(demand.getTimeout());

        return detail;
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
                + "\n    demandTypeId="
                + id + "\n     name="
                + activationLink + "\n    Description="
                + timeout;
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
