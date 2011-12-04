package cz.poptavka.sample.shared.domain.message;

import java.util.Date;

import cz.poptavka.sample.domain.demand.DemandStatus;


public interface TableDisplay {

    long getMessageId();

    String getTitle();

    boolean isRead();

    void setRead(boolean value);

    boolean isStarred();

    void setStarred(boolean value);

    Date getCreated();

    Date getEndDate();

    String getDemandPrice();

    String getFormattedMessageCount();

    /**
     * @return demand-related conversation messages
     */
    int getMessageCount();

    String getClientName();

    int getClientRating();

    DemandStatus getDemandStatus();

    Date getExpireDate();
}
