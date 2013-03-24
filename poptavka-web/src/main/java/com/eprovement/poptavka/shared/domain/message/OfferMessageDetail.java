package com.eprovement.poptavka.shared.domain.message;

import com.google.gwt.user.client.rpc.IsSerializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.Future;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public class OfferMessageDetail implements IsSerializable {

    public enum MessageField {

        BODY("body"),
        PRICE("price"),
        FINISH_DATE("finishDate");

        private MessageField(String value) {
            this.value = value;
        }
        private String value;

        public String getValue() {
            return value;
        }
    }

    /**************************************************************************/
    /* Attibutes                                                              */
    /**************************************************************************/
    private long senderId;
    private long parentId;
    private long supplierId;
    private long threadRootId;
    @NotBlank(message = "{messageNotBlankBody}")
    @Size(min = 10, message = "{messageSizeBody}")
    private String body;
    @NotNull(message = "{messageNotNullPrice}")
    @Min(value = 0, message = "{messageMinPrice}")
    private BigDecimal price;
    @NotNull(message = "{messageNotNullFinishDate}")
    @Future(message = "{messageFutureFinishDate}")
    private Date finishDate;

    /**************************************************************************/
    /* Getters & Setters pairs                                                */
    /**************************************************************************/
    public long getSenderId() {
        return senderId;
    }

    public void setSenderId(long senderId) {
        this.senderId = senderId;
    }

    public long getParentId() {
        return parentId;
    }

    public void setParentId(long parentId) {
        this.parentId = parentId;
    }

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
    }

    public long getThreadRootId() {
        return threadRootId;
    }

    public void setThreadRootId(long threadRootId) {
        this.threadRootId = threadRootId;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return body;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date offerFinishDate) {
        this.finishDate = offerFinishDate;
    }
}
