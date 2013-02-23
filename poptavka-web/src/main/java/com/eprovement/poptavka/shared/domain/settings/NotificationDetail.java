/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eprovement.poptavka.shared.domain.settings;

import com.eprovement.poptavka.domain.enums.Period;
import com.google.gwt.user.client.rpc.IsSerializable;



/**
 *
 * @author Martin Slavkovsky
 */
public class NotificationDetail implements IsSerializable {

    private long notificationIdemId;
    private Boolean enabled;
    private String name;
    private Period period;

    public NotificationDetail() {
    }

    public NotificationDetail(long notificationIdemId) {
        this.notificationIdemId = notificationIdemId;
    }

    public long getNotificationIdemId() {
        return notificationIdemId;
    }

    public void setNotificationIdemId(long notificationIdemId) {
        this.notificationIdemId = notificationIdemId;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Period getPeriod() {
        return period;
    }

    public void setPeriod(Period period) {
        this.period = period;
    }

//    @Override
//    public int hashCode() {
//        int hash = 3;
//        hash = 67 * hash + (int) (this.notificationIdemId ^ (this.notificationIdemId >>> 32));
//        hash = 67 * hash + (this.name != null ? this.name.hashCode() : 0);
//        return hash;
//    }
//
//    @Override
//    public boolean equals(Object obj) {
//        if (obj == null) {
//            return false;
//        }
//        if (getClass() != obj.getClass()) {
//            return false;
//        }
//        final NotificationDetail other = (NotificationDetail) obj;
//        if (this.notificationIdemId != other.notificationIdemId) {
//            return false;
//        }
//        return true;
//    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("NotificationDetail{");
        str.append("notificationIdemId=");
        str.append(notificationIdemId);
        str.append(", enabled=");
        str.append(enabled);
        str.append(", name=");
        str.append(name);
        str.append(", period=");
        str.append(period);
        str.append('}');
        return str.toString();
    }
}
