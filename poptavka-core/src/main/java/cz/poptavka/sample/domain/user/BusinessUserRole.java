package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.util.orm.Constants;
import org.hibernate.envers.Audited;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

/**
 * Represents the role of {@link BusinessUser}.
 * <p>
 * User can have multiple roles, therefore the descendants of this class don't inherit directly from
 * {@link BusinessUser}, but from this class and list of roles is assigned to the {@link BusinessUser}.
 * In this case, inheritance is replaced by composition.
 *
 * @author Juraj Martinka
 *         Date: 8.5.11
 */
@Entity
@Audited
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class BusinessUserRole extends DomainObject {

    /**
     * BusinessUser to which this role is assigned. Useful for retrieving basic information about that business user.
     * <p>
     *     Cascading is set to <code>CascadeType.PERSIST</code> because we do not want to removed referenced
     *     {@link BusinessUser} if this role is removed (business user can have other roles, not only this one),
     *     however, we want to persist business user if it is created/modified through this role.
     */
    @ManyToOne(cascade = CascadeType.PERSIST)
    private BusinessUser businessUser = new BusinessUser();

    /**
     *  Verification state of client. No default value!
     * @see {@link cz.poptavka.sample.domain.user.Verification} enum
     */
    @Enumerated(value = EnumType.STRING)
    @Column(length = Constants.ENUM_FIELD_LENGTH)
    private Verification verification;


    //---------------------------------- GETTERS AND SETTERS -----------------------------------------------------------

    public BusinessUser getBusinessUser() {
        return businessUser;
    }

    public void setBusinessUser(BusinessUser businessUser) {
        this.businessUser = businessUser;
    }

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessUserRole");
        sb.append("{businessUser=").append(businessUser);
        sb.append(", verification=").append(verification);
        sb.append('}');
        return sb.toString();
    }
}
