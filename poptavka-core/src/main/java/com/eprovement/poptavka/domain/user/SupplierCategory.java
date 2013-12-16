package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.demand.Category;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Categories of demands the suppliers can realize
 * 
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
                        + " where supplierCategory.category.id in (:categoriesIds)"),
        @NamedQuery(name = "getSuppliersCountForCategory",
                query = "select count(distinct supplierCategory.supplier)"
                        + " from SupplierCategory supplierCategory"
                        + " where supplierCategory.category.leftBound  between :leftBound and :rightBound"),

        /**
         * In one query compute suppliers count for each locality and return it as a list of pairs
         * <localityId, suppliersCountForLocality>.
         */
        @NamedQuery(name = "getSuppliersCountForAllCategories",
                query = "select new map(c AS category, "
                        + "  (select count(distinct supplierCategory.supplier)"
                        + "    from SupplierCategory supplierCategory"
                        + "    where supplierCategory.category.leftBound "
                        + "          between c.leftBound and c.rightBound)"
                        + "   AS suppliersCount)"
                        + " from Category c"),

        /**
         * Get count of all suppliers that belongs directly to the specified cateogy. No suppliers belonging to
         * any subcategory are included!
         */
        @NamedQuery(name = "getSuppliersCountForCategoryWithoutChildren",
                query = "select count(supplierCategory.supplier) "
                + " from SupplierCategory supplierCategory where supplierCategory.category = :category")

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
