package cz.poptavka.sample.shared.domain.message;

import java.util.Date;


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
}
