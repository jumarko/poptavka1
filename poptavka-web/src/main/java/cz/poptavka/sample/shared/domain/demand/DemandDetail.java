package cz.poptavka.sample.shared.domain.demand;

import java.math.BigDecimal;
import java.util.Date;

import cz.poptavka.sample.shared.domain.type.DemandDetailType;

public interface DemandDetail {

    long getDemandId();
    void setDemandId(long demandId);

    long getMessageId();
    void setMessageId(long messageId);

    long getUserMessageId();
    void setUserMessageId(long userMessageId);

    boolean isRead();
    void setRead(boolean read);

    boolean isStarred();
    void setStarred(boolean starred);

    Date getEndDate();
    void setEndDate(Date finishDate);

    Date getValidToDate();
    void setValidToDate(Date validToDate);

    String getTitle();
    void setTitle(String title);

    String getDescription();
    void setDescription(String description);

    BigDecimal getPrice();
    String displayPrice();

    void setPrice(String price);
    void setPrice(BigDecimal price);

    void setType(DemandDetailType detailType);
    DemandDetailType getType();

    String toString();
}
