/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.user;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.demand.Category;
import cz.poptavka.sample.domain.demand.Rating;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import java.util.List;

/**
 *
 * @author Excalibur
 */
@Entity
@Audited
public class Supplier extends BusinessUser {

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


    /** All suppliers ratings. */
    @OneToMany
    @NotAudited
    private List<Rating> ratings;


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


    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }
}
