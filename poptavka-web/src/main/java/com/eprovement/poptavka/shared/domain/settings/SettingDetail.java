package com.eprovement.poptavka.shared.domain.settings;

import com.eprovement.poptavka.shared.domain.BusinessUserDetail;
import com.eprovement.poptavka.shared.domain.SupplierDetail;
import com.google.gwt.user.client.rpc.IsSerializable;

import java.util.ArrayList;

/**
 * represents all types of system users
 * @author Beho
 *
 */
public class SettingDetail implements IsSerializable {

    public enum Role {

        CLIENT, SUPPLIER, PARTNER, OPERATOR, ADMIN
    }
    /** Instances of roles. **/
    private long userId;
    //user settings
    private BusinessUserDetail user;
    //client settings
    private int clientRating;
    //supplier settings
    private SupplierDetail supplier;
    //system settings
    private ArrayList<NotificationDetail> notifications;

    public SettingDetail() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    //user settings
    public BusinessUserDetail getUser() {
        return user;
    }

    public void setUser(BusinessUserDetail user) {
        this.user = user;
    }

    //client settings
    public int getClientRating() {
        return clientRating;
    }

    public void setClientRating(int clientRating) {
        this.clientRating = clientRating;
    }

    //supplier settings
    public SupplierDetail getSupplier() {
        return supplier;
    }

    public void setSupplier(SupplierDetail supplier) {
        this.supplier = supplier;
    }

    //system settings
    public ArrayList<NotificationDetail> getNotifications() {
        return notifications;
    }

    public void setNotifications(ArrayList<NotificationDetail> notifications) {
        if (notifications != null) {
            this.notifications = new ArrayList<NotificationDetail>(notifications);
        }
    }
}
