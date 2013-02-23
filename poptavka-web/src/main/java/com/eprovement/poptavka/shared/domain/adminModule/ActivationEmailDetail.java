package com.eprovement.poptavka.shared.domain.adminModule;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.util.Date;

/**
 * Represents full detail of domain object <b>EmailActivation</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
 *
 */
public class ActivationEmailDetail implements IsSerializable {

    private Long id;
    private String activationCode;
    private Date timeout;

    /** for serialization. **/
    public ActivationEmailDetail() {
    }

    public ActivationEmailDetail(ActivationEmailDetail demand) {
        this.updateWholeEmailActivation(demand);
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeEmailActivation(ActivationEmailDetail emailActivationDetail) {
        id = emailActivationDetail.getId();
        activationCode = emailActivationDetail.getActivationCode();
        timeout = emailActivationDetail.getTimeout();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActivationCode() {
        return activationCode;
    }

    public void setActivationCode(String name) {
        this.activationCode = name;
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
                + "\n    Name=" + activationCode
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
        final ActivationEmailDetail other = (ActivationEmailDetail) obj;
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
