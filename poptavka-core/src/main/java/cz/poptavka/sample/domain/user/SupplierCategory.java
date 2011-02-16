package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.demand.Category;

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
@Table(name = "SUPPLIER_CATEGORY")
@NamedQueries({
        @NamedQuery(name = "getSuppliersForCategories", query = "select supplierCategory.supplier"
                        + " from SupplierCategory supplierCategory"
                        + " where supplierCategory.category.id in (:categoriesIds)"),
        @NamedQuery(name = "getSuppliersCountForCategories", query = "select count(distinct supplierCategory.supplier)"
                        + " from SupplierCategory supplierCategory"
                        + " where supplierCategory.category.id in (:categoriesIds)")

})
public class SupplierCategory extends DomainObject {

    @ManyToOne
    private Supplier supplier;

    @ManyToOne
    private Category category;


    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("DemandCategory");
        sb.append("{supplier=").append(supplier);
        sb.append(", category=").append(category);
        sb.append('}');
        return sb.toString();
    }
}
