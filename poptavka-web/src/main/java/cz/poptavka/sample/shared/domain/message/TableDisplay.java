package cz.poptavka.sample.shared.domain.message;

import java.util.Date;

import cz.poptavka.sample.domain.demand.DemandStatus;

public interface TableDisplay {

    long getMessageId();

    //demandTitle,
    String getTitle();

    //supplierName, clientName
    String getSender();

    boolean isRead();

    void setRead(boolean value);

    boolean isStarred();

    void setStarred(boolean value);

    Date getCreated();

    Date getEndDate();

    String getPrice();

    String getFormattedMessageCount();

    /**
     * @return demand-related conversation messages
     */
    int getMessageCount();


    int getRating();

    DemandStatus getDemandStatus();

    Date getExpireDate();

    /**
     * @return demand-related conversation messages
     */
    Date getValidToDate();

    Date getReceivedDate();

    Date getAcceptedDate();
}
