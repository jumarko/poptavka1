package com.eprovement.poptavka.domain.product;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.util.orm.OrmConstants;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ManyToOne;

/**
 * Represents the binding between {@link com.eprovement.poptavka.domain.user.BusinessUser} and {@link Service}.
 *
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
@Entity
@Audited
public class UserService extends DomainObject {

    @ManyToOne
    @NotAudited
    private Service service;

    @ManyToOne
    private BusinessUser user;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
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
