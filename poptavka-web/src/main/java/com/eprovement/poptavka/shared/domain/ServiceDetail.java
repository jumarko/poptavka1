package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.domain.enums.ServiceType;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;

public class ServiceDetail implements IsSerializable {

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    private long id;
    private String code;
    private String title;
    private String description;
    private BigDecimal price;
    private int credits;
    private ServiceType serviceType;

    public static final ProvidesKey<ServiceDetail> KEY_PROVIDER = new ProvidesKey<ServiceDetail>() {
        @Override
        public Object getKey(ServiceDetail item) {
            return item == null ? null : item.getId();
        }
    };

    /**************************************************************************/
    /*  Initialization                                                        */
    /**************************************************************************/
    public ServiceDetail() {
        //for serialization
    }

    public ServiceDetail(ServiceDetail detail) {
        this.id = detail.getId();
        this.code = detail.getCode();
        this.title = detail.getTitle();
        this.description = detail.getDescription();
        this.price = detail.getPrice();
        this.credits = detail.getCredits();
        this.serviceType = detail.getServiceType();
    }

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    /**************************************************************************/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

    /**************************************************************************/
    /* Override methods                                                       */
    /**************************************************************************/
    @Override
    public String toString() {
        return "ServiceDetail{" + "id=" + id
            + ", code=" + code
            + ", title=" + title
            + ", description=" + description
            + ", price=" + price
            + ", credits=" + credits
            + ", serviceType=" + serviceType + '}';
    }

    @Override
    public int hashCode() {
        return (int) id;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ServiceDetail other = (ServiceDetail) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }
}
