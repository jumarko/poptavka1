package cz.poptavka.sample.client.main.common.search.dataHolders;

import java.io.Serializable;

/** ADMININVOICE **/
public class AdminInvoices implements Serializable {

    private Long idFrom = null;
    private Long idTo = null;
    private Integer invoiceNumberFrom = null;
    private Integer invoiceNumberTo = null;
    private String variableSymbol = null;
    private Integer totalPriceFrom = null;
    private Integer totalPriceTo = null;
    private Long paymentMethodId = null;

    public Long getIdFrom() {
        return idFrom;
    }

    public void setIdFrom(Long idFrom) {
        this.idFrom = idFrom;
    }

    public Long getIdTo() {
        return idTo;
    }

    public void setIdTo(Long idTo) {
        this.idTo = idTo;
    }

    public Integer getInvoiceNumberFrom() {
        return invoiceNumberFrom;
    }

    public void setInvoiceNumberFrom(Integer invoiceNumberFrom) {
        this.invoiceNumberFrom = invoiceNumberFrom;
    }

    public Integer getInvoiceNumberTo() {
        return invoiceNumberTo;
    }

    public void setInvoiceNumberTo(Integer invoiceNumberTo) {
        this.invoiceNumberTo = invoiceNumberTo;
    }

    public Long getPaymentMethodId() {
        return paymentMethodId;
    }

    public void setPaymentMethodId(Long paymentMethodId) {
        this.paymentMethodId = paymentMethodId;
    }

    public Integer getTotalPriceFrom() {
        return totalPriceFrom;
    }

    public void setTotalPriceFrom(Integer totalPriceFrom) {
        this.totalPriceFrom = totalPriceFrom;
    }

    public Integer getTotalPriceTo() {
        return totalPriceTo;
    }

    public void setTotalPriceTo(Integer totalPriceTo) {
        this.totalPriceTo = totalPriceTo;
    }

    public String getVariableSymbol() {
        return variableSymbol;
    }

    public void setVariableSymbol(String variableSymbol) {
        this.variableSymbol = variableSymbol;
    }
}
