package com.eprovement.poptavka.domain.user;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.demand.Category;
import javax.persistence.Column;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import java.util.List;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * Supplier posts offers and realizes projects demanded by clients
 *
 * @author Excalibur
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "getSuppliersForCategoriesAndLocalities",
                    query = "select supplierCategory.supplier"
                        + " from SupplierCategory supplierCategory,"
                        + " SupplierLocality supplierLocality"
                        + " where supplierCategory.supplier ="
                        + " supplierLocality.supplier"
                        + " and supplierCategory.category.id in (:categoryIds)"
                        + " and supplierLocality.locality.id in (:localityIds)"),
        @NamedQuery(name = "getSuppliersCountForCategoriesAndLocalities",
                    query = "select count(supplierCategory.supplier)"
                        + " from SupplierCategory supplierCategory,"
                        + " SupplierLocality supplierLocality"
                        + " where supplierCategory.supplier ="
                        + " supplierLocality.supplier"
                        + " and supplierCategory.category.id in (:categoryIds)"
                        + " and supplierLocality.locality.id in (:localityIds)"),
        @NamedQuery(name = "getSuppliersForCategoriesAndLocalitiesIncludingParents",
                query = "select supplierCategory.supplier"
                        + " from SupplierCategory supplierCategory,"
                        + " SupplierLocality supplierLocality "
                        + "where supplierCategory.supplier ="
                        + " supplierLocality.supplier"
                        + " and (supplierCategory.supplier.enabled IS NULL or supplierCategory.supplier.enabled='1')"
                        + " and exists (select c.id from Category c "
                        + "where c.leftBound >= supplierCategory.category.leftBound"
                        + " and c.rightBound <= supplierCategory.category.rightBound "
                        + "and c.id in (:categoryIds))"
                        + " and exists (select l.id from Locality l "
                        + "where l.leftBound >= supplierLocality.locality.leftBound"
                        + " and l.rightBound <= supplierLocality.locality.rightBound"
                        + " and l.id in (:localityIds))"
        ),
        @NamedQuery(name = "getSuppliersCountForCategoriesAndLocalitiesIncludingParents",
                query = "select count(supplierCategory.supplier)"
                        + " from SupplierCategory supplierCategory,"
                        + " SupplierLocality supplierLocality"
                        + " where supplierCategory.supplier ="
                        + " supplierLocality.supplier"
                        + " and (supplierCategory.supplier.enabled IS NULL or supplierCategory.supplier.enabled='1')"
                        + " and exists (select c.id from Category c "
                        + "where c.leftBound >= supplierCategory.category.leftBound"
                        + " and c.rightBound <= supplierCategory.category.rightBound"
                        + " and c.id in (:categoryIds))"
                        + " and exists (select l.id from Locality l "
                        + "where l.leftBound >= supplierLocality.locality.leftBound"
                        + " and l.rightBound <= supplierLocality.locality.rightBound"
                        + " and l.id in (:localityIds))"),
})
@Audited
public class Supplier extends BusinessUserRole {

    @ManyToMany
    @NotAudited
    @JoinTable(
        name = "SUPPLIER_LOCALITY",
        joinColumns = @JoinColumn(name = "SUPPLIER_ID"),
        inverseJoinColumns = @JoinColumn(name = "LOCALITY_ID")
    )
    // at least one locality
    @NotEmpty
    private List<Locality> localities;

    @ManyToMany
    @NotAudited
    @JoinTable(
        name = "SUPPLIER_CATEGORY",
        joinColumns = @JoinColumn(name = "SUPPLIER_ID"),
        inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    // at least one category
    @NotEmpty
    private List<Category> categories;

    /** Flag indicates whether supplier is certified or not.
     * TODO LATER Juraj : the type of ceritification should be available!*/
    // workaround - see http://stackoverflow.com/questions/8667965/found-bit-expected-boolean-after-hibernate-4-upgrade
    @Column(columnDefinition = "BIT")
    private Boolean certified;

    /** Total rating of supplier for all his "processed" demands .*/
    private Integer overalRating = Integer.valueOf(0);


    public List<Locality> getLocalities() {
        return localities;
    }

    public void setLocalities(List<Locality> localities) {
        this.localities = localities;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public Boolean isCertified() {
        return certified;
    }

    public void setCertified(Boolean certified) {
        this.certified = certified;
    }

    public Integer getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(Integer overalRating) {
        this.overalRating = overalRating;
    }


    @Override
    public String toString() {
        return "Supplier:" + super.toString();
    }
}
