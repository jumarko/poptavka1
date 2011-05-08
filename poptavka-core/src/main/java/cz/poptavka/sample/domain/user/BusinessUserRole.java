package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.util.orm.Constants;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.MappedSuperclass;

/**
 * @author Juraj Martinka
 *         Date: 8.5.11
 */
@MappedSuperclass
public abstract class BusinessUserRole extends DomainObject {

    /**
     *  Verification state of client. No default value!
     * @see {@link cz.poptavka.sample.domain.user.Verification} enum
     */
    @Enumerated(value = EnumType.STRING)
    @Column(length = Constants.ENUM_FIELD_LENGTH)
    private Verification verification;

    public Verification getVerification() {
        return verification;
    }

    public void setVerification(Verification verification) {
        this.verification = verification;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessUserUser");
        sb.append("{verification=").append(verification);
        sb.append('}');
        return sb.toString();
    }
}
