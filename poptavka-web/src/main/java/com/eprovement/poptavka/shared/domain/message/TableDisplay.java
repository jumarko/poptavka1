package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.enums.OfferStateType;
import java.util.Date;


public interface TableDisplay {

    boolean isStarred();

    void setIsStarred(boolean value);

    Date getEndDate();

    DemandStatus getDemandStatus();

    OfferStateType getOfferState();
}
