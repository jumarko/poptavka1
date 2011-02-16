package cz.poptavka.sample.domain.product;

import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.math.BigDecimal;

/**
 * Represents a "product" that is offer to the some kind of {@link cz.poptavka.sample.domain.user.BusinessUser}.
 * Business user can have multiple services assigned (he can buy the servies or get them free (if they are gratis)).
 *
 * <p>
 * Some examples of services are:
 * <ul>
*      <li>Classic client</li>
 *     <li>Attractive client</li>
 *     <li>VIP Client</li>
 * </ul>
 *
 * @author Juraj Martinka
 *         Date: 30.1.11
 */
@Entity
public class Service extends DomainObject {

    @Column(length = 64)
    private String title;

    private String description;

    private BigDecimal price;

    private boolean valid;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Service");
        sb.append("{title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", valid=").append(valid);
        sb.append('}');
        return sb.toString();
    }
}
