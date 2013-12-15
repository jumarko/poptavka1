package com.eprovement.poptavka.domain.common;

import javax.persistence.Entity;

/**
 * Represents additional information gathered for domain object, especially for
 * {@link com.eprovement.poptavka.domain.demand.Category} and
 * {@link com.eprovement.poptavka.domain.address.Locality}.
 *
 * @see com.eprovement.poptavka.domain.demand.Category
 * @see com.eprovement.poptavka.domain.address.Locality
 * 
 * This information is generated automatically by a recurring job.
 *
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
@Entity
public class AdditionalInfo extends DomainObject {

    /**
     * Number of demands related to the associated domain object.
     * E.g. Number of demands in given locality.
     */
    private Long demandsCount;

    /**
     * Number of suppliers related to the associated domain object.
     * E.g. number of suppliers that are registered for supplies in given category.
     */
    private Long suppliersCount;

    public AdditionalInfo() {
    }

    public AdditionalInfo(Long demandsCount, Long suppliersCount) {
        this.demandsCount = demandsCount;
        this.suppliersCount = suppliersCount;
    }

    public Long getDemandsCount() {
        return demandsCount;
    }

    public AdditionalInfo setDemandsCount(Long demandsCount) {
        this.demandsCount = demandsCount;
        return this;
    }

    public Long getSuppliersCount() {
        return suppliersCount;
    }

    public AdditionalInfo setSuppliersCount(Long suppliersCount) {
        this.suppliersCount = suppliersCount;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("AdditionalInfo");
        sb.append("{demandsCount=").append(demandsCount);
        sb.append(", suppliersCount=").append(suppliersCount);
        sb.append('}');
        return sb.toString();
    }

}
