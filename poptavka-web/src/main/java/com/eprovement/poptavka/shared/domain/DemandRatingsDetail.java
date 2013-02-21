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
    private int ratingClient;
    private int ratingSupplier;
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

    public int getRatingClient() {
        return ratingClient;
    }

    public void setRatingClient(int ratingClient) {
        this.ratingClient = ratingClient;
    }

    public int getRatingSupplier() {
        return ratingSupplier;
    }

    public void setRatingSupplier(int ratingSupplier) {
        this.ratingSupplier = ratingSupplier;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append(demandTitle);
        str.append(", ");
        str.append(ratingClient);
        str.append(", ");
        str.append(ratingSupplier);
        return str.toString();
    }
}
