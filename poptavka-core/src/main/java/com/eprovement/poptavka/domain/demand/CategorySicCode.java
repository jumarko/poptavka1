package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 * Entity represents mapping between poptavka's categories and sic codes.
 *
 * See <a href="http://www.naics.com/search.htm">SIC codes search.</a>
 */
@Entity
@NamedQuery(name = "getCategoryBySicCode",
            query = "select c.category from CategorySicCode c where c.sicCode = :sicCode")
public class CategorySicCode extends DomainObject {
    /** sic code, 8 chars should be sufficient for all sic codes. we reserve 12 chars for sure. */
    @Column(unique = true, length = 12)
    private String sicCode;

    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    public String getSicCode() {
        return sicCode;
    }

    public void setSicCode(String sicCode) {
        this.sicCode = sicCode;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    @Override
    public String toString() {
        return "CategorySicCode{"
                + "sicCode='" + sicCode + '\''
                + '}';
    }
}
