package com.eprovement.poptavka.domain.product;

import com.eprovement.poptavka.domain.enums.ServiceType;
import com.eprovement.poptavka.domain.register.Register;
import com.eprovement.poptavka.util.orm.OrmConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.math.BigDecimal;

/**
 * Represents a "product" that is offer to the some kind of {@link com.eprovement.poptavka.domain.user.BusinessUser}.
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
public class Service extends Register {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    @Column(length = 64)
    private String title;

    private String description;

    private BigDecimal price;

    // workaround - see http://stackoverflow.com/questions/8667965/found-bit-expected-boolean-after-hibernate-4-upgrade
    @Column(columnDefinition = "BIT")
    private boolean valid;

    private Integer credits;

    /**
     * Service type that indicates which role has ordered this system. It can be
     * Client, Supplier or Partner
     */
    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private ServiceType serviceType;

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    /**************************************************************************/
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

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    /**************************************************************************/
    /*  Override methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Service");
        sb.append("{title='").append(title).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", price=").append(price);
        sb.append(", valid=").append(valid);
        sb.append(", credits=").append(credits);
        sb.append(", serviceType=").append(serviceType);
        sb.append('}');
        return sb.toString();
    }
}
