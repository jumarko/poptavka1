package com.eprovement.poptavka.domain.common;

import com.eprovement.poptavka.service.fulltext.FulltextAnalyzer;
import org.hibernate.search.annotations.Analyzer;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

/**
 * Base class for al domain objects.
 * Main responsibilities:
 * <ol>
 *     <li>Handles ID field mapping at one place.</li>
 *     <li>Defines "caseAccentInsensitiveAnalyzer" which enables case and accent insensitive fulltext search
 *         to be available on all domain objects. Particular object must use this Analyzer through annotation
 *        {@link org.hibernate.search.annotations.Analyzer}.
 *        See also {@link com.eprovement.poptavka.service.fulltext.HibernateFulltextSearchService}
 *     </li>
 * </ol>
 *
 */
@Analyzer(impl = FulltextAnalyzer.class)
@MappedSuperclass
public abstract class DomainObject implements Serializable {

    /** Id of the entity. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        if (this.id != null && id == null) {
            throw new NullPointerException("id");
        }
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        final DomainObject that = (DomainObject) o;

        return !(id != null ? !id.equals(that.id) : that.id != null);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
