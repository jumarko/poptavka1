package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * Detail representation of domain object UserService.
 * @author Martin Slavkovsky
 */
public class UserServiceDetail implements IsSerializable {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    private Long id;
    private Long orderNumber;
    private ServiceDetail service;
    private String status;

    /**************************************************************************/
    /*  Initialization                                                        */
    /**************************************************************************/
    public UserServiceDetail() {
        //for serialization
    }

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    /**************************************************************************/
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Long orderNumber) {
        this.orderNumber = orderNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ServiceDetail getService() {
        return service;
    }

    public void setService(ServiceDetail service) {
        this.service = service;
    }
}
