package com.eprovement.poptavka.domain.address;

import com.eprovement.poptavka.domain.common.DomainObject;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.annotations.Index;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entity represents mapping between zip codes and localities.
 * Typically, only the cities have some zip code associated, regions and districts don't have such one.
 */
@Entity
@Table(name = "LocalityZipcode")
@NamedQuery(name = "getLocalitiesByZipCode",
            query = "select locality from LocalityZipCode where zipCode = :zipCode")
public class LocalityZipCode extends DomainObject {

    /**
     * Zip code - see
     * <a href="http://www.barnesandnoble.com/help/cds2.asp?PID=8134">International zip codes formats</a>.
     * The current max length is 9, so 16 should be sufficient.
     */
    @Index(name = "zip_code_idx")
    @Column(length = 16)
    private String zipCode;

    @OneToOne
    private Locality locality;

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .append("zipCode", zipCode)
                .append("locality", locality)
                .toString();
    }
}
