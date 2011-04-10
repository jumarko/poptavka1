package cz.poptavka.sample.domain.invoice;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;

/**
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class PaymentMethod extends DomainObject {

    private String name;

    private String description;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("PaymentMethod");
        sb.append("{name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
