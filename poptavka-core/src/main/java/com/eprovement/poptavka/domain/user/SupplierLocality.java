package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.DomainObject;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Localities where the clients can realize demands
 * 
 * @author Juraj Martinka
 *         Date: 13.2.11
 */
@Entity
@Table(name = "SUPPLIER_LOCALITY")
@NamedQueries({
        @NamedQuery(name = "getSuppliersForLocalities", query = "select supplierLocality.supplier"
                + SupplierLocality.SUPPLIERS_FOR_LOCALITIES_FW_CLAUSE),
        @NamedQuery(name = "getSuppliersCountForLocalities", query = "select count(distinct supplierLocality.supplier)"
                + SupplierLocality.SUPPLIERS_FOR_LOCALITIES_FW_CLAUSE),
        @NamedQuery(name = "getSuppliersCountForLocality",
                query = "select count(distinct supplierLocality.supplier)"
                        + " from SupplierLocality supplierLocality "
                        +  "where supplierLocality.locality.leftBound between :leftBound and :rightBound"),

        /**
         * In one query compute suppliers count for each locality and return it as a list of pairs
         * <localityId, suppliersCountForLocality>.
         */
        @NamedQuery(name = "getSuppliersCountForAllLocalities",
                query = "select new map(l AS locality, "
                        + "  (select count(distinct supplierLocality.supplier)"
                        + "    from SupplierLocality supplierLocality"
                        + "    where supplierLocality.locality.leftBound "
                        + "          between l.leftBound and l.rightBound)"
                        + "   AS suppliersCount)"
                        + " from Locality l"),

        /**
         * Get count of all suppliers that belongs directly to the specified locality. No suppliers belonging to
         * any sublocality are included!
         */
        @NamedQuery(name = "getSuppliersCountForLocalityWithoutChildren",
                query = "select count(supplierLocality.supplier) "
                + " from SupplierLocality supplierLocality where supplierLocality.locality = :locality")

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
