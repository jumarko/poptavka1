package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

import java.math.BigDecimal;

public class ServiceDetail implements IsSerializable {

    private long id;
    private String title;
    private String description;
    private BigDecimal price;
    private int prepaidMonths;
    private String type;

    public static final ProvidesKey<ServiceDetail> KEY_PROVIDER = new ProvidesKey<ServiceDetail>() {
        @Override
        public Object getKey(ServiceDetail item) {
            return item == null ? null : item.getId();
        }
    };

    public ServiceDetail() {
        //for serialization
    }

    public ServiceDetail(long id, String title, String desc, BigDecimal price, int months) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.price = price;
        this.prepaidMonths = months;
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

    @Override
    public String toString() {
        return title + " (" + price + "/" + prepaidMonths + ")";
    }
}
