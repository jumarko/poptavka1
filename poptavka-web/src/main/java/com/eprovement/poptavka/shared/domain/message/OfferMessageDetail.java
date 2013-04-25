package com.eprovement.poptavka.shared.domain.message;

import com.eprovement.poptavka.client.common.validation.DateEqualOrGreater;
import java.math.BigDecimal;
import java.util.Date;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.validator.constraints.NotBlank;

public class OfferMessageDetail extends MessageDetail {

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
    private long supplierId;
    @NotBlank(message = "{bodyNotBlank}")
    @Size(min = 2, max = 1500, message = "{bodySize}")
    private String body;
    @NotNull(message = "{priceNotNull}")
    @Min(value = 0, message = "{priceMin}")
    @Digits(integer = 12, fraction = 0, message = "{priceDigits}")
    private BigDecimal price;
    @NotNull(message = "{finishDateNotNull}")
    @DateEqualOrGreater(message = "{finishDateEqualOrGreater}")
    private Date finishDate;

    public long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(long supplierId) {
        this.supplierId = supplierId;
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
