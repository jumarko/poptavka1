package com.eprovement.poptavka.domain.product;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.PaypalTransactionStatus;
import com.eprovement.poptavka.domain.enums.Status;
import com.eprovement.poptavka.domain.user.BusinessUser;
import com.eprovement.poptavka.util.orm.OrmConstants;
import java.util.Date;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Represents the binding between {@link com.eprovement.poptavka.domain.user.BusinessUser} and {@link Service}.
 *
 * <code>UserService</code> always contains particular <code>Service</code> FREE, BASIC, PROFI.
 * Everytime <code>BusinessUser</code> pays for a <code>Service</code> the <code>UserService</code> is created.
 * The Free service can be chosen only during new registration.
 *
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
@Entity
@Audited
public class UserService extends DomainObject {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    @ManyToOne
    @NotAudited
    private Service service;

    @ManyToOne
    private BusinessUser businessUser;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private Status status;

    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderNumber;

    private String transactionNumber;

    @Enumerated(EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private PaypalTransactionStatus transactionStatus;

    @Temporal(TemporalType.TIMESTAMP)
    private Date request = new Date();

    @Temporal(TemporalType.TIMESTAMP)
    private Date response;

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    /**************************************************************************/
    /**
     * @return Appropriate service
    */
    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }

    /**
     * @return Appropriate BusinessUser
    */
    public BusinessUser getBusinessUser() {
        return businessUser;
    }

    public void setBusinessUser(BusinessUser businessUser) {
        this.businessUser = businessUser;
    }

    /**
     * @return User Service status
    */
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * @return Unique value for payment.
     */
    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * @return Transaction number sent from paypal.
     */
    public String getTransactionNumber() {
        return transactionNumber;
    }

    public void setTransactionNumber(String transactionNumber) {
        this.transactionNumber = transactionNumber;
    }

    /**
     * @return Transaction status sent by paypal after finnished transaction.
     */
    public PaypalTransactionStatus getTransactionStatus() {
        return transactionStatus;
    }

    public void setTransactionStatus(PaypalTransactionStatus transactionStatus) {
        this.transactionStatus = transactionStatus;
    }

    /**
     * @return Date when was UserService created and a request has been sent to PayPal.
     */
    public Date getRequest() {
        return request;
    }

    public void setRequest(Date request) {
        this.request = request;
    }

    /**
     * @return Date when response came from PayPal.
     */
    public Date getResponse() {
        return response;
    }

    public void setResponse(Date response) {
        this.response = response;
    }

    /**************************************************************************/
    /*  Override methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        return new StringBuilder().append("UserService")
            .append("{service=").append(service)
            .append(", businessUser=").append(businessUser)
            .append(", status=").append(status)
            .append(", orderNumber=").append(orderNumber)
            .append(", transactionNumber=").append(transactionNumber)
            .append(", transactionStatus=").append(transactionStatus)
            .append(", request=").append(request)
            .append(", response=").append(response).append('}')
            .toString();
    }
}
