package cz.poptavka.sample.shared.domain;

import cz.poptavka.sample.domain.invoice.Invoice;
import cz.poptavka.sample.domain.product.UserService;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents full detail of demandType. Serves for creating new demandType
 * or for call of detail, that supports editing.
 *
 * @author Beho
 *
 */
public class InvoiceDetail implements Serializable {

    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -530982467233195456L;
    private long id;
    private String invoiceNumber;
    private List<UserServiceDetail> userServices;
    //----------------------------------------- Dates ------------------------------------------------------------------
    private Date issueDate;
    private Date shipmentDate;
    private Date dueDate;
    //---------------------------------------- Bank information --------------------------------------------------------
    private String bankAccountNumber;
    private String bankCode;
    private String variableSymbol;
    private String constSymbol;
    //------------------------------ Price -----------------------------------------------------------------------------
    private BigDecimal taxBasis;
    /** Vat rate from 0 to 100 % (percent). */
    private int vatRate;
    private BigDecimal vat;
    /** Total price (VAT included). */
    private BigDecimal totalPrice;
    private PaymentMethodDetail paymentMethod;

    /** for serialization. **/
    public InvoiceDetail() {
    }

    public InvoiceDetail(InvoiceDetail demand) {
        this.updateWholeInvoice(demand);
    }

    /**
     * Method created FullDemandDetail from provided Demand domain object.
     * @param invoice
     * @return DemandDetail
     */
    public static InvoiceDetail createInvoiceDetail(Invoice invoice) {
        InvoiceDetail detail = new InvoiceDetail();

        detail.setBankAccountNumber(invoice.getBankAccountNumber());
        detail.setBankCode(invoice.getBankCode());
        detail.setConstSymbol(invoice.getConstSymbol());
        detail.setDueDate(invoice.getDueDate());
        detail.setId(invoice.getId());
        detail.setInvoiceNumber(invoice.getInvoiceNumber());
        detail.setIssueDate(invoice.getIssueDate());
        detail.setPaymentMethod(PaymentMethodDetail.createPaymentMethodDetail(invoice.getPaymentMethod()));
        detail.setShipmentDate(invoice.getShipmentDate());
        detail.setTaxBasis(invoice.getTaxBasis());
        detail.setTotalPrice(invoice.getTotalPrice());
        List<UserServiceDetail> userServices = new ArrayList<UserServiceDetail>();
        for (UserService userService : invoice.getUserServices()) {
            userServices.add(UserServiceDetail.createAccessRoleDetail(userService));
        }
        detail.setUserServices(userServices);
        detail.setVariableSymbol(invoice.getVariableSymbol());
        detail.setVat(invoice.getVat());
        detail.setVatRate(invoice.getVatRate());

        return detail;
    }

    //---------------------------- GETTERS AND SETTERS --------------------
    public void updateWholeInvoice(InvoiceDetail detail) {
        bankAccountNumber = detail.getBankAccountNumber();
        bankCode = detail.getBankCode();
        constSymbol = detail.getConstSymbol();
        dueDate = detail.getDueDate();
        id = detail.getId();
        invoiceNumber = detail.getInvoiceNumber();
        issueDate = detail.getIssueDate();
        paymentMethod = detail.getPaymentMethod();
        shipmentDate = detail.getShipmentDate();
        taxBasis = detail.getTaxBasis();
        totalPrice = detail.getTotalPrice();
        userServices = detail.getUserServices();
        variableSymbol = detail.getVariableSymbol();
        vat = detail.getVat();
        vatRate = detail.getVatRate();
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

    public String getConstSymbol() {
        return constSymbol;
    }

    public void setConstSymbol(String constSymbol) {
        this.constSymbol = constSymbol;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public Date getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(Date issueDate) {
        this.issueDate = issueDate;
    }

    public PaymentMethodDetail getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethodDetail paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Date getShipmentDate() {
        return shipmentDate;
    }

    public void setShipmentDate(Date shipmentDate) {
        this.shipmentDate = shipmentDate;
    }

    public BigDecimal getTaxBasis() {
        return taxBasis;
    }

    public void setTaxBasis(BigDecimal taxBasis) {
        this.taxBasis = taxBasis;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<UserServiceDetail> getUserServices() {
        return userServices;
    }

    public void setUserServices(List<UserServiceDetail> userServices) {
        this.userServices = userServices;
    }

    public String getVariableSymbol() {
        return variableSymbol;
    }

    public void setVariableSymbol(String variableSymbol) {
        this.variableSymbol = variableSymbol;
    }

    public BigDecimal getVat() {
        return vat;
    }

    public void setVat(BigDecimal vat) {
        this.vat = vat;
    }

    public int getVatRate() {
        return vatRate;
    }

    public void setVatRate(int vatRate) {
        this.vatRate = vatRate;
    }

    @Override
    public String toString() {

        return "\nGlobal Invoice Detail Info:";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InvoiceDetail other = (InvoiceDetail) obj;
        if (this.id != other.getId()) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
