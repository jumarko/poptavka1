package com.eprovement.poptavka.domain.invoice;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.product.UserService;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Represents invoice sent to the customer.
 * Only one invoice is sent to the customer for all his services - {@link }.
 * <p>
 * It might seems to be important to audit this entity,
 * however, invoice is created only once and then is not normally edited.
 * If any error occurs then that invoice should be discarded and new invoice will be issued.
 *
 * <p>
 * For naming conventions see
 * <a href="http://www.porada.sk/t73758-nalezitosti-faktury-v-anglickom-a-nemeckom-jazyku.html">
 *     nalezitosti-faktury-v-anglickom-a-nemeckom-jazyku</a>
 *
 * @author Juraj Martinka
 *         Date: 29.1.11
 */
@Entity
public class Invoice extends DomainObject {

    /**
     * "Number" of invoice - this must be unique code.
     * <p>
     * Format of this number is "YYYYMMDDNNNN", where:
     * <ul>
     *    <li>YYYY is a year of issue date</li>
     *    <li>MM is a month of issue date</li>
     *    <li>DD is a day of issue date</li>
     *    <li>NNNN is an order number of this invoice in given date.</li>
     * </ul>
     */
    @Column(length = 12)
    private String invoiceNumber;

    /**
     * All user services that are being paid by this invoice.
     */
    @OneToMany
    @JoinTable(name = "Invoice_UserService")
    private List<UserService> userServices;

    //----------------------------------------- Dates ------------------------------------------------------------------
    @Temporal(value = TemporalType.DATE)
    private Date issueDate;

    @Temporal(value = TemporalType.DATE)
    private Date shipmentDate;

    @Temporal(value = TemporalType.DATE)
    private Date dueDate;

    //---------------------------------------- Bank information --------------------------------------------------------
    @Column(length = 24)
    private String bankAccountNumber;

    @Column(length = 8)
    private String bankCode;

    @Column(length = 10)
    private String variableSymbol;

    @Column(length = 10)
    private String constSymbol;


    //------------------------------ Price -----------------------------------------------------------------------------
    private BigDecimal taxBasis;

    /** Vat rate from 0 to 100 % (percent). */
    private int vatRate;

    private BigDecimal vat;

    /** Total price (VAT included). */
    private BigDecimal totalPrice;


    @ManyToOne
    private PaymentMethod paymentMethod;



    //-------------------------- GETTERS and SETTERS -------------------------------------------------------------------
    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public List<UserService> getUserServices() {
        return userServices;
    }

    public void setUserServices(List<UserService> userServices) {
        this.userServices = userServices;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public void setBankAccountNumber(String bankAccountNumber) {
        this.bankAccountNumber = bankAccountNumber;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getVariableSymbol() {
        return variableSymbol;
    }

    public void setVariableSymbol(String variableSymbol) {
        this.variableSymbol = variableSymbol;
    }

    public String getConstSymbol() {
        return constSymbol;
    }

    public void setConstSymbol(String constSymbol) {
        this.constSymbol = constSymbol;
    }

    public BigDecimal getTaxBasis() {
        return taxBasis;
    }

    public void setTaxBasis(BigDecimal taxBasis) {
        this.taxBasis = taxBasis;
    }

    public int getVatRate() {
        return vatRate;
    }

    public void setVatRate(int vatRate) {
        this.vatRate = vatRate;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }


    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }


    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Invoice");
        sb.append("{invoiceNumber='").append(invoiceNumber).append('\'');
        sb.append(", issueDate=").append(issueDate);
        sb.append(", shipmentDate=").append(shipmentDate);
        sb.append(", dueDate=").append(dueDate);
        sb.append(", bankAccountNumber='").append(bankAccountNumber).append('\'');
        sb.append(", bankCode='").append(bankCode).append('\'');
        sb.append(", variableSymbol='").append(variableSymbol).append('\'');
        sb.append(", constSymbol='").append(constSymbol).append('\'');
        sb.append(", taxBasis=").append(taxBasis);
        sb.append(", vatRate=").append(vatRate);
        sb.append(", vat=").append(vat);
        sb.append(", totalPrice=").append(totalPrice);
        sb.append('}');
        return sb.toString();
    }
}
