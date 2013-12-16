package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.common.Origin;
import com.eprovement.poptavka.domain.enums.BusinessType;
import com.eprovement.poptavka.domain.invoice.Invoice;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.util.orm.OrmConstants;
import com.eprovement.poptavka.util.strings.ToStringUtils;
import java.util.ArrayList;
import javax.validation.constraints.NotNull;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Represents an essential type of users that have a business relationship with us.
 * <p>
 * Three main categories of these users exist:
 * <ul>
 *  <li>Client - demands to have a project realized</li>
 *  <li>Supplier - offers to realize projects demanded by clients</li>
 *  <li>Partner</li>
 * </ul>
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
@Audited
public class BusinessUser extends User {
    @OneToMany(mappedBy = "businessUser")
    @NotEmpty
    private List<BusinessUserRole> businessUserRoles = new ArrayList<>();

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private BusinessType businessType;

    /** Business user data about company and contact person. */
    @OneToOne
    @Cascade(value = CascadeType.ALL)
    @NotNull
    private BusinessUserData businessUserData;

    /** All user's addresses. */
    @NotAudited
    @OneToMany
    @Cascade(value = CascadeType.ALL)
    // At least one address must be specified
    @NotEmpty
    private List<Address> addresses;

    @NotAudited
    @OneToMany(mappedBy = "user")
    @Cascade(value = CascadeType.ALL)
    private List<UserService> userServices;

    @OneToMany
    @NotAudited
    private List<Invoice> invoices;

    /**
     * Origin of user in case of user imported from external system. Should be null for plain old internal users.
     */
    @OneToOne
    @NotAudited
    private Origin origin;

    public BusinessUser() {
    }

    public BusinessUser(String email, String password) {
        super(email, password);
    }

    //-------------------------- GETTERS AND SETTERS -------------------------------------------------------------------
    public List<BusinessUserRole> getBusinessUserRoles() {
        return businessUserRoles;
    }

    public void setBusinessUserRoles(List<BusinessUserRole> businessUserRoles) {
        this.businessUserRoles = businessUserRoles;
    }

    public BusinessType getBusinessType() {
        return businessType;
    }

    public void setBusinessType(BusinessType businessType) {
        this.businessType = businessType;
    }

    public BusinessUserData getBusinessUserData() {
        return businessUserData;
    }

    public void setBusinessUserData(BusinessUserData businessUserData) {
        this.businessUserData = businessUserData;
    }

    public List<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<Address> addresses) {
        this.addresses = addresses;
    }

    public List<UserService> getUserServices() {
        return userServices;
    }

    public void setUserServices(List<UserService> userServices) {
        this.userServices = userServices;
    }

    public List<Invoice> getInvoices() {
        return invoices;
    }

    public void setInvoices(List<Invoice> invoices) {
        this.invoices = invoices;
    }

    /**
     * @return composed display name for business user - personal name or company name
     * @see com.eprovement.poptavka.domain.user.BusinessUserData#getDisplayName()
     */
    @Override
    public String getDisplayName() {
        return businessUserData.getDisplayName();
    }

    public Origin getOrigin() {
        return origin;
    }

    public void setOrigin(Origin origin) {
        this.origin = origin;
    }

    public boolean isUserFromExternalSystem() {
        return this.origin != null && origin.isExternal();
    }

    //-------------------------- End of GETTERS AND SETTERS ------------------------------------------------------------
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessUser");
        sb.append("{id=").append(getId());
        sb.append(", email=").append(getEmail());
        sb.append(", businessUserData=").append(ToStringUtils.printId(businessUserData));
        sb.append('}');
        return sb.toString();
    }
}
