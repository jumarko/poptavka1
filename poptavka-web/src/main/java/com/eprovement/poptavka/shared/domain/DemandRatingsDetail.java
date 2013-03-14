package com.eprovement.poptavka.shared.domain;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.google.gwt.view.client.ProvidesKey;

public class DemandRatingsDetail implements IsSerializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 104799426040035341L;
    private long demandId;
    private long supplierId;
    private long threadRootId;
    private String demandTitle;
    private Integer ratingClient;
    private Integer ratingSupplier;
    private String ratingClientMessage;
    private String ratingSupplierMessage;
    //KeyProvider
    public static final ProvidesKey<DemandRatingsDetail> KEY_PROVIDER =
            new ProvidesKey<DemandRatingsDetail>() {
            @Override
            public Object getKey(DemandRatingsDetail item) {
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

    public DemandRatingsDetail() {
    }

    public String getDemandTitle() {
        return demandTitle;
    }

    public void setDemandTitle(String demandTitle) {
        this.demandTitle = demandTitle;
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
}
