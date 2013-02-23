package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.common.DomainObject;
import org.hibernate.envers.Audited;

import javax.persistence.Entity;

/**
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
@Entity
@Audited
public class Problem extends DomainObject {

    private String text;


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ProblemReport");
        sb.append("{text='").append(text).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
