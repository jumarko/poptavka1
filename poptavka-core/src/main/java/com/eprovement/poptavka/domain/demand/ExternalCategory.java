package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.common.ExternalSource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * Entity represents mapping between poptavka's internal categories and categories from External systems
 * (such as fbo.gov).
 *
 * See <a href="http://www.naics.com/search.htm">SIC codes search.</a> as an example of different category system.
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getCategoriesMappingForExternalSource",
            query = "from ExternalCategory ec where ec.externalSource = :source"),
    @NamedQuery(name = "getExternalCategory",
        query = "from ExternalCategory ec where ec.externalId = :externalId")
})

public class ExternalCategory extends DomainObject {
    /**
     * Unique identifier of category in external system.
     * Typically this would be a category name but different identifiers such as SIC code or NAICS code can be used.
     */
    @Column(unique = true, length = 255, nullable = false)
    private String externalId;

    /**
     * Internal categories to which the external one is mapped.
     * Typically, one external category should be mapped to single internal category.
     * However, there are some cases when we cannot determine the exact single category
     * and want to map external category to multiple internal categories.
     *
     * The next part of story is, that one internal category will typically be connected to multiple external categories
     * mappings because ExternalCategory entity can contain mappings for multiple external sources
     * such as fbo.gov, uscompanydatabase.com etc.
     */
    @ManyToMany
    @JoinTable(
        name = "EXTERNAL_CATEGORY_TO_CATEGORY",
        joinColumns = @JoinColumn(name = "EXTERNAL_CATEGORY_ID"),
        inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    private List<Category> categories;

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

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public ExternalSource getExternalSource() {
        return externalSource;
    }

    public void setExternalSource(ExternalSource externalSource) {
        this.externalSource = externalSource;
    }

    @Override
    public String toString() {
        return "ExternalCategory{"
                + "externalId='" + externalId + '\''
                + ", categories=" + categories
                + ", externalSource=" + externalSource
                + '}';
    }
}
