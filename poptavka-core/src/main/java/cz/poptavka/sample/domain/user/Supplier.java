/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import java.util.List;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
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
                        + " and supplierLocality.locality.id in (:localityIds)")
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
    private List<Locality> localities;

    @ManyToMany
    @NotAudited
    @JoinTable(
        name = "SUPPLIER_CATEGORY",
        joinColumns = @JoinColumn(name = "SUPPLIER_ID"),
        inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    private List<Category> categories;

    /** Flag indicates wheter supplier is certified or not. TODO: the type of ceritification should be available!*/
    private Boolean certified;

    /** Total rating of supplier for all his "processed" demands .*/
    private Integer overalRating;


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

    public void setCertified(boolean certified) {
        this.certified = certified;
    }

    public Integer getOveralRating() {
        return overalRating;
    }

    public void setOveralRating(Integer overalRating) {
        this.overalRating = overalRating;
    }
}
