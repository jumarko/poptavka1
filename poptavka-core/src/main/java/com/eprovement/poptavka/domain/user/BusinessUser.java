package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.address.Address;
import com.eprovement.poptavka.domain.invoice.Invoice;
import com.eprovement.poptavka.domain.product.UserService;
import com.eprovement.poptavka.util.orm.OrmConstants;
import com.eprovement.poptavka.util.strings.ToStringUtils;
import java.util.ArrayList;
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

/**
 * Represents essential group users in application that have business relationship to us.
 * <p>
 * Three main categories of this users exist:
 * <ul>
 *  <li>Client - is searching for some services at most situations.</li>
 *  <li>Supplier - provides some services to clients.</li>
 *  <li>Partner - a subject that has partnership beneficial to both parts </li>
 * </ul>
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
@Audited
public class BusinessUser extends User {
    @OneToMany(mappedBy = "businessUser")
    private List<BusinessUserRole> businessUserRoles = new ArrayList<BusinessUserRole>();
    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private BusinessType businessType;
    /** Business user data about company and contact person. */
    @OneToOne
    @Cascade(value = CascadeType.ALL)
    private BusinessUserData businessUserData;
    /** All user's addresses. */
    @NotAudited
    @OneToMany
    @Cascade(value = CascadeType.ALL)
    private List<Address> addresses;
    @NotAudited
    @OneToMany(mappedBy = "user")
    @Cascade(value = CascadeType.ALL)
    private List<UserService> userServices;
    @OneToMany
    @NotAudited
    private List<Invoice> invoices;

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

    //-------------------------- End of GETTERS AND SETTERS ------------------------------------------------------------
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("BusinessUser");
        sb.append("{id=").append(getId());
        sb.append(", businessUserData=").append(ToStringUtils.printId(businessUserData));
        sb.append('}');
        return sb.toString();
    }
}
