package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.common.ExternalSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 * Entity represents mapping between poptavka's internal categories and categories from External systems
 * (such as fbo.gov).
 *
 * See <a href="http://www.naics.com/search.htm">SIC codes search.</a> as an example of different category system.
 */
@Entity
@NamedQuery(name = "getCategoryBySicCode",
            query = "select c.category from CategorySicCode c where c.sicCode = :sicCode")
public class ExternalCategory extends DomainObject {
    /**
     * Unique identifier of category in external system.
     * Typically this would be a category name but different identifiers such as SIC code or NAICS code can be used.
     */
    @Column(unique = true, length = 255)
    private String externalId;

    /**
     * Internal category to which the external one is mapped.
     */
    @OneToOne(fetch = FetchType.LAZY)
    private Category category;

    /**
     * External source for which this mapping is relevant
     */
    @OneToOne(fetch = FetchType.LAZY)
    private ExternalSource externalSource;


    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public ExternalSource getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(ExternalSource externalSource) {
        this.externalSource = externalSource;
    }
}
