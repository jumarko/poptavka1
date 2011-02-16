package cz.poptavka.sample.domain.common;

import java.io.Serializable;

/**
 * Represents additional information that can be gattered for domain object, especially for
 * {@link cz.poptavka.sample.domain.demand.Category} and
 * {@link cz.poptavka.sample.domain.address.Locality}.
 *
 * @see cz.poptavka.sample.domain.demand.Category
 * @see cz.poptavka.sample.domain.address.Locality
 *
 * @author Juraj Martinka
 *         Date: 12.2.11
 */
public class AdditionalInfo implements Serializable {

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
