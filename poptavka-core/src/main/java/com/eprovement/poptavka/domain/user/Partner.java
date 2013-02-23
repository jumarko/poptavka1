package com.eprovement.poptavka.domain.user;

import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
@Entity
@Audited
public class Partner extends BusinessUserRole {

    @Override
    public String toString() {
        return "Partner:" + super.toString();
    }
}
