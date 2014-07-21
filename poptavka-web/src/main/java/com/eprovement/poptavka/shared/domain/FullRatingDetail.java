package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.client.user.widget.grid.columns.DemandTitleColumn.TableDisplayDemandTitle;
import com.eprovement.poptavka.client.user.widget.grid.columns.PriceColumn.TableDisplayPrice;
import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;
import java.math.BigDecimal;

public class FullRatingDetail implements IsSerializable,
    TableDisplayDemandTitle, TableDisplayPrice {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 104799426040035341L;
    private long demandId;
    private long supplierId;
    private long threadRootId;
    //Demand
    private String demandTitle;
    private BigDecimal demandPrice;
    private String demandDescription;
    //Client
    private Integer ratingClient;
    private Integer ratingSupplier;
    //Supplier
    private String ratingClientMessage;
    private String ratingSupplierMessage;
    private String supplierName;
    private String clientName;
    //KeyProvider
    public static final ProvidesKey<RatingDetail> KEY_PROVIDER =
            new ProvidesKey<RatingDetail>() {
            @Override
            public Object getKey(RatingDetail item) {
                return item == null ? null : item.getDemandId();
            }
        };

    public long getDemandId() {
        return demandId;
    }

    public void setDemandId(long demandId) {
        this.demandId = demandId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getThreadRootId() {
        return threadRootId;
    }

    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    public FullRatingDetail() {
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

    public String getDemandDescription() {
        return demandDescription;
    }

    public void setDemandDescription(String demandDescription) {
        this.demandDescription = demandDescription;
    }

    public Integer getRatingClient() {
        return ratingClient;
    }

    public void setRatingClient(Integer ratingClient) {
        this.ratingClient = ratingClient;
    }

    public Integer getRatingSupplier() {
        return ratingSupplier;
    }

    public void setRatingSupplier(Integer ratingSupplier) {
        this.ratingSupplier = ratingSupplier;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(demandTitle);
        str.append(", ");
        str.append(ratingClient != null ? ratingClient.intValue() : "no ratingClient yet");
        str.append(", ");
        str.append(ratingSupplier != null ? ratingSupplier.intValue() : "no ratingSupplier yet");
        return str.toString();
    }

    /**
     * @return the ratingClientMessage
     */
    public String getRatingClientMessage() {
        return ratingClientMessage;
    }

    /**
     * @param ratingClientMessage the ratingClientMessage to set
     */
    public void setRatingClientMessage(String ratingClientMessage) {
        this.ratingClientMessage = ratingClientMessage;
    }

    /**
     * @return the ratingSupplierMessage
     */
    public String getRatingSupplierMessage() {
        return ratingSupplierMessage;
    }

    /**
     * @param ratingSupplierMessage the ratingSupplierMessage to set
     */
    public void setRatingSupplierMessage(String ratingSupplierMessage) {
        this.ratingSupplierMessage = ratingSupplierMessage;
    }

    /**
     * @return the supplierName
     */
    public String getSupplierName() {
        return supplierName;
    }

    /**
     * @param supplierName the supplierName to set
     */
    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    /**
     * @return the clientName
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * @param clientName the clientName to set
     */
    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

}
