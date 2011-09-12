package cz.poptavka.sample.shared.domain.message;

import java.util.Date;


public interface TableDisplay {

    String getTitle();

    boolean isRead();

    boolean isStarred();

    void setStarred(boolean value);

    Date getCreated();

    Date getEndDate();
}
