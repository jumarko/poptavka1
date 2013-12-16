package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.PriceColumn.TableDisplayPrice;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import java.math.BigDecimal;

public class RatingDetail implements IsSerializable,
    TableDisplayDemandTitle, TableDisplayPrice {

    private long demandId;
    private String demandTitle;
    private BigDecimal demandPrice;
    //KeyProvider
    public static final ProvidesKey<RatingDetail> KEY_PROVIDER =
        new ProvidesKey<RatingDetail>() {
            @Override
            public Object getKey(RatingDetail item) {
                return item == null ? null : item.getDemandId();
            }
        };

    public RatingDetail() {
        //for serialization
    }

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    @Override
    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
    }

    @Override
    public BigDecimal getPrice() {
        return demandPrice;
    }

    public void setPrice(BigDecimal demandPrice) {
        this.demandPrice = demandPrice;
    }

    @Override
    public int getUnreadMessagesCount() {
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("id=" + demandId);
        str.append(", title=" + demandTitle);
        str.append(", price=" + demandPrice);
        return str.toString();
    }
}
