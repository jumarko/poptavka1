package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.domain.product.Service;
import java.io.Serializable;
import java.math.BigDecimal;

public class ServiceDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1325587667656361089L;
    private long id;
    private String title;
    private String description;
    private BigDecimal price;
    private int prepaidMonths;
    private String type;

    public ServiceDetail() {
    }

    public ServiceDetail(long id, String title, String desc, BigDecimal price, int months) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.price = price;
        this.prepaidMonths = months;
    }

    public static ServiceDetail createServiceDetail(Service service) {
        ServiceDetail detail = new ServiceDetail();
        detail.setId(service.getId());
        detail.setTitle(service.getTitle());
        detail.setDescription(service.getDescription());
        detail.setPrice(service.getPrice());
        detail.setPrepaidMonths(service.getPrepaidMonths());
        detail.setType(service.getServiceType().getValue());
        return detail;
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

    public int getPrepaidMonths() {
        return prepaidMonths;
    }

    public void setPrepaidMonths(int prepaidMonths) {
        this.prepaidMonths = prepaidMonths;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
