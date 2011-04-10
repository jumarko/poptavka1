package cz.poptavka.sample.domain.activation;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;
import java.util.Date;

/**
 * Represents request for activation that is sent to the user via email.
 *
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class EmailActivation extends DomainObject {

    private String activationLink;

    /** After this date the activation will not be possible. */
    private Date timeout;

    public String getActivationLink() {
        return activationLink;
    }

    public void setActivationLink(String activationLink) {
        this.activationLink = activationLink;
    }

    public Date getTimeout() {
        return timeout;
    }

    public void setTimeout(Date timeout) {
        this.timeout = timeout;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EmailActivation");
        sb.append("{activationLink='").append(activationLink).append('\'');
        sb.append(", timeout=").append(timeout);
        sb.append('}');
        return sb.toString();
    }
}
