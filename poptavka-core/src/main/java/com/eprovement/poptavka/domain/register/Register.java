package com.eprovement.poptavka.domain.register;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Common ancestor for all registers in application.
 * <p>
 *     The only required field for all registers is <code>code</code>.
 *
 * @author Juraj Martinka
 *         Date: 17.5.11
 */
@MappedSuperclass
public class Register extends DomainObject {

    /** Field required for all registers. */
    @Column(unique = true, nullable = false, length = 32)
    private String code;


    public Register() {
    }

    public Register(String code) {
        this.code = code;
    }


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Register");
        sb.append("{code='").append(code).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
