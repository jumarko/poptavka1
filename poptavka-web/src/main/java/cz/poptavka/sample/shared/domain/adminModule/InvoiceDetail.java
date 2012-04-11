package cz.poptavka.sample.shared.domain.adminModule;

import cz.poptavka.sample.domain.invoice.Invoice;
import cz.poptavka.sample.domain.product.UserService;
import cz.poptavka.sample.shared.domain.UserServiceDetail;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Represents full detail of domain object <b>Invoice</b> used in <i>Administration Module</i>.
 * Contains 2 static methods:  1. creating detail object
 *                             2. updating domain object
 *
 * @author Martin Slavkovsky
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
     * Method created <b>InvoiceDetail</b> from provided Demand domain object.
     * @param domain - given domain object
     * @return InvoiceDetail - created detail object
     */
    public static InvoiceDetail createInvoiceDetail(Invoice domain) {
        InvoiceDetail detail = new InvoiceDetail();

        detail.setBankAccountNumber(domain.getBankAccountNumber());
        detail.setBankCode(domain.getBankCode());
        detail.setConstSymbol(domain.getConstSymbol());
        detail.setDueDate(domain.getDueDate());
        detail.setId(domain.getId());
        detail.setInvoiceNumber(domain.getInvoiceNumber());
        detail.setIssueDate(domain.getIssueDate());
        detail.setPaymentMethod(PaymentMethodDetail.createPaymentMethodDetail(domain.getPaymentMethod()));
        detail.setShipmentDate(domain.getShipmentDate());
        detail.setTaxBasis(domain.getTaxBasis());
        detail.setTotalPrice(domain.getTotalPrice());
        List<UserServiceDetail> userServices = new ArrayList<UserServiceDetail>();
        for (UserService userService : domain.getUserServices()) {
            userServices.add(UserServiceDetail.createAccessRoleDetail(userService));
        }
        detail.setUserServices(userServices);
        detail.setVariableSymbol(domain.getVariableSymbol());
        detail.setVat(domain.getVat());
        detail.setVatRate(domain.getVatRate());

        return detail;
    }

    /**
     * Method created domain object <b>Invoice</b> from provided <b>InvoiceDetail</b> object.
     * @param domain - domain object to be updated
     * @param detail - detail object which provides updated data
     * @return InvoiceDetail - updated given domain object
     */
    public static Invoice updateInvoice(Invoice domain, InvoiceDetail detail) {
        if (!domain.getInvoiceNumber().equals(detail.getInvoiceNumber())) {
            domain.setInvoiceNumber(detail.getInvoiceNumber());
        }
        //------------------------------ Dates ---------------------------------
        if (!domain.getIssueDate().equals(detail.getIssueDate())) {
            domain.setIssueDate(detail.getIssueDate());
        }
        if (!domain.getShipmentDate().equals(detail.getShipmentDate())) {
            domain.setShipmentDate(detail.getShipmentDate());
        }
        if (!domain.getDueDate().equals(detail.getDueDate())) {
            domain.setDueDate(detail.getDueDate());
        }
        //------------------------ Bank information ----------------------------
        if (!domain.getBankAccountNumber().equals(detail.getBankAccountNumber())) {
            domain.setBankAccountNumber(detail.getBankAccountNumber());
        }
        if (!domain.getBankCode().equals(detail.getBankCode())) {
            domain.setBankCode(detail.getBankCode());
        }
        if (!domain.getVariableSymbol().equals(detail.getVariableSymbol())) {
            domain.setVariableSymbol(detail.getVariableSymbol());
        }
        if (!domain.getConstSymbol().equals(detail.getConstSymbol())) {
            domain.setConstSymbol(detail.getConstSymbol());
        }
        //------------------------------ Price ---------------------------------
        if (!domain.getTaxBasis().equals(detail.getTaxBasis())) {
            domain.setTaxBasis(detail.getTaxBasis());
        }
        if (domain.getVatRate() != detail.getVatRate()) {
            domain.setVatRate(detail.getVatRate());
        }
        if (!domain.getVat().equals(detail.getVat())) {
            domain.setVat(detail.getVat());
        }
        if (!domain.getTotalPrice().equals(detail.getTotalPrice())) {
            domain.setTotalPrice(detail.getTotalPrice());
        }
        if (!domain.getConstSymbol().equals(detail.getConstSymbol())) {
            domain.setConstSymbol(detail.getConstSymbol());
        }
        //TODO Martin - how to update userServices, paymentMethods
        return domain;
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
        return "\nGlobal Invoice Detail Info:"
                + "\n    Id=" + Long.toString(id)
                + "\n    InvoiceNumber=" + invoiceNumber
                + "\n    UserServices=" + userServices.toString()
                + "\n    IssueDate=" + issueDate.toString()
                + "\n    ShipmentDate=" + shipmentDate.toString()
                + "\n    DueDate=" + dueDate.toString()
                + "\n    BankAccountNum=" + bankAccountNumber
                + "\n    BankCode" + bankCode
                + "\n    VS=" + variableSymbol
                + "\n    KS=" + constSymbol
                + "\n    TaxtBasis=" + taxBasis.toString()
                + "\n    VatRate=" + Integer.valueOf(vatRate)
                + "\n    Vat=" + vat.toString()
                + "\n    TotalPrice=" + totalPrice.toString()
                + "\n    PaymentMethod=" + paymentMethod.toString();
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
