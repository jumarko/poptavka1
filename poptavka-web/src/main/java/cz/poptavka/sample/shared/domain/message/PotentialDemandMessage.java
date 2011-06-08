package cz.poptavka.sample.shared.domain.message;

import java.math.BigDecimal;
import java.util.Date;

public interface PotentialDemandMessage extends MessageDetail {

    void setPrice(BigDecimal price);
    BigDecimal getPrice();

    void setRead(boolean read);
    boolean isRead();

    void setStarred(boolean starred);
    boolean isStarred();

    void setEndDate(Date endDate);
    Date getEndDate();

    void setValidToDate(Date validTo);
    Date getValidToDate();

    void setUserMessageId(Long userMessageId);
    Long getUserMessageId();

    // maybe later
    // setOfferCount
    // getOfferCount
}
