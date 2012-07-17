package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import java.util.Date;


public interface TableDisplay {

    boolean isRead();

    void setRead(boolean value);

    boolean isStarred();

    void setStarred(boolean value);

    Date getEndDate();

    DemandStatus getDemandStatus();
}
