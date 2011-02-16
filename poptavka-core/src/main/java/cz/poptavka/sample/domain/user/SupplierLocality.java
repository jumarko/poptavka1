package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
@Entity
@Table(name = "SUPPLIER_LOCALITY")
@NamedQueries({
        @NamedQuery(name = "getSuppliersForLocalities", query = "select supplierLocality.supplier"
                + SupplierLocality.SUPPLIERS_FOR_LOCALITIES_FW_CLAUSE),
        @NamedQuery(name = "getSuppliersCountForLocalities", query = "select count(distinct supplierLocality.supplier)"
                + SupplierLocality.SUPPLIERS_FOR_LOCALITIES_FW_CLAUSE)
})
public class SupplierLocality extends DomainObject {

    static final String SUPPLIERS_FOR_LOCALITIES_FW_CLAUSE = " from SupplierLocality supplierLocality"
                + " where supplierLocality.locality.id in (:localitiesIds)";

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private Locality locality;


    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Locality getLocality() {
        return locality;
    }

    public void setLocality(Locality locality) {
        this.locality = locality;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DemandLocality");
        sb.append("{supplier=").append(supplier);
        sb.append(", locality=").append(locality);
        sb.append('}');
        return sb.toString();
    }
}
