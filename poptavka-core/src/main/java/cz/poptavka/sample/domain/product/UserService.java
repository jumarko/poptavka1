package cz.poptavka.sample.domain.product;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.common.Status;
import cz.poptavka.sample.domain.user.BusinessUser;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

/**
 * Represents the binding between {@link cz.poptavka.sample.domain.user.BusinessUser} and {@link Service}.
 *
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
@Entity
public class UserService extends DomainObject {

    // TODO martinka -- fill in with attributes
    @ManyToOne
    private Service service;

    @ManyToOne
    private BusinessUser user;

    private Status status;


    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    public BusinessUser getUser() {
        return user;
    }

    public void setUser(BusinessUser user) {
        this.user = user;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("UserService");
        sb.append("{service=").append(service);
        sb.append(", user=").append(user);
        sb.append(", status=").append(status);
        sb.append('}');
        return sb.toString();
    }
}
