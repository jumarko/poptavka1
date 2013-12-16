package com.eprovement.poptavka.domain.invoice;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Advice of an upcoming invoice
 *
 * @author Juraj Martinka
 *         Date: 11.4.11
 */
@Entity
public class Advice extends DomainObject {

    @OneToOne
    private Invoice invoice;

    @Column(length = 24)
    private String remitterAccount;
    private String remitterName;
    private String remitterSurname;

    @Column(length = 8)
    private String bankCode;

    @Temporal(value = TemporalType.DATE)
    private Date paymentDate;

    @Column(length = 10)
    private String variableSymbol;

    // workaround - see http://stackoverflow.com/questions/8667965/found-bit-expected-boolean-after-hibernate-4-upgrade
    @Column(columnDefinition = "BIT")
    private boolean recognized;

    private BigDecimal bounty;


    public Invoice getInvoice() {
        return invoice;
    }

    public void setInvoice(Invoice invoice) {
        this.invoice = invoice;
    }

    public String getRemitterAccount() {
        return remitterAccount;
    }

    public void setRemitterAccount(String remitterAccount) {
        this.remitterAccount = remitterAccount;
    }

    public String getRemitterName() {
        return remitterName;
    }

    public void setRemitterName(String remitterName) {
        this.remitterName = remitterName;
    }

    public String getRemitterSurname() {
        return remitterSurname;
    }

    public void setRemitterSurname(String remitterSurname) {
        this.remitterSurname = remitterSurname;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public Date getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(Date paymentDate) {
        this.paymentDate = paymentDate;
    }

    public String getVariableSymbol() {
        return variableSymbol;
    }

    public void setVariableSymbol(String variableSymbol) {
        this.variableSymbol = variableSymbol;
    }

    public boolean isRecognized() {
        return recognized;
    }

    public void setRecognized(boolean recognized) {
        this.recognized = recognized;
    }

    public BigDecimal getBounty() {
        return bounty;
    }

    public void setBounty(BigDecimal bounty) {
        this.bounty = bounty;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Advice");
        sb.append("{invoice=").append(invoice);
        sb.append(", remitterAccount='").append(remitterAccount).append('\'');
        sb.append(", remitterName='").append(remitterName).append('\'');
        sb.append(", remitterSurname='").append(remitterSurname).append('\'');
        sb.append(", bankCode='").append(bankCode).append('\'');
        sb.append(", paymentDate=").append(paymentDate);
        sb.append(", variableSymbol='").append(variableSymbol).append('\'');
        sb.append(", recognized=").append(recognized);
        sb.append(", bounty=").append(bounty);
        sb.append('}');
        return sb.toString();
    }
}
