package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import java.util.Date;


public interface TableDisplay {

    boolean isStarred();

    int getUnreadSubmessagesCount();

    void setIsStarred(boolean value);

    Date getValidTo();

    DemandStatus getDemandStatus();

    OfferStateType getOfferState();
}
