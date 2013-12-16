package com.eprovement.poptavka.domain.activation;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Entity;
import java.util.Date;
import javax.persistence.Temporal;

/**
 * Represents request for activation that is sent to the user via email.
 *
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class ActivationEmail extends DomainObject {

    private String activationCode;

    /** After this date the activation will not be possible. */
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date validTo;

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String activationCode) {
        this.activationCode = activationCode;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date timeout) {
        this.validTo = timeout;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("EmailActivation");
        sb.append("{activationCode='").append(activationCode).append('\'');
        sb.append(", timeout=").append(validTo);
        sb.append('}');
        return sb.toString();
    }
}
