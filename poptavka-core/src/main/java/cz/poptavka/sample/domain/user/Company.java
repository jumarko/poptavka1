package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.common.DomainObject;
import org.hibernate.envers.Audited;

import javax.persistence.Column;
import javax.persistence.Entity;

/**
 * Represents company registered in application.
 * <p>
 * For naming conventions see
 * <a href="http://www.porada.sk/t73758-nalezitosti-faktury-v-anglickom-a-nemeckom-jazyku.html">
 *     nalezitosti-faktury-v-anglickom-a-nemeckom-jazyku</a>
 *
 * @author Juraj Martinka
 *         Date: 28.1.11
 */
@Entity
@Audited
public class Company extends DomainObject {

    private String name;

    /** aka "ic" in czech republic. */
    @Column(length = 16)
    private String identificationNumber;

    /** aka "dic* in czech republic */
    @Column(length = 16)
    private String taxId;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentificationNumber() {
        return identificationNumber;
    }

    public void setIdentificationNumber(String identificationNumber) {
        this.identificationNumber = identificationNumber;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Company");
        sb.append("{name='").append(name).append('\'');
        sb.append(", ic='").append(identificationNumber).append('\'');
        sb.append(", dic='").append(taxId).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
