/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.domain.address.Locality;
import cz.poptavka.sample.domain.common.DomainObject;
import cz.poptavka.sample.domain.offer.Offer;
import cz.poptavka.sample.domain.user.Client;
import cz.poptavka.sample.domain.user.Supplier;
import cz.poptavka.sample.util.orm.Constants;
import cz.poptavka.sample.util.strings.ToStringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.Store;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * The core domain class which represents demand entered by client.
 *
 * @author Juraj Martinka
 */
@Entity
@Audited
@Indexed
@NamedQueries({
    @NamedQuery(name = "getAllDemandsCount",
    query = "select count(*) from Demand")
})
public class Demand extends DomainObject {

    /** Fields that are available for full-text search. */
    public static final String[] DEMAND_FULLTEXT_FIELDS = new String[] {"title" , "description"};

    @Column(length = 100, nullable = false)
    @Field(index = Index.TOKENIZED, store = Store.NO)
    // title is most important - boost twice as much as other fields
    @Boost(2)
    private String title;

    /**
     * Description for demands. Can be quite long, but it is nor neccessary, nor desirable to have an arbitrary
     * big column.
     * <p>
     *     (At least) now for MySQL this column must be altered to be a TEXT (max 64 kB) type.
     *     Otherwise the Hibernate create by default the LONGTEXT type, which can have up to 4 GB.
     */
    @Lob
    @Column(name = "description")
    @Field(index = Index.TOKENIZED, store = Store.NO)
    private String description;

    private BigDecimal price;

    /** The max date when the demand should be finished. */
    @Temporal(value = TemporalType.DATE)
    private Date endDate;

    /** This demand is considered to be valid in system until this day. */
    @Temporal(value = TemporalType.DATE)
    private Date validTo;

    /** Path in the sotrage file system where all attachments are saved. */
    private String attachmentPath;

    @Enumerated(value = EnumType.STRING)
    @Column(length = Constants.ENUM_FIELD_LENGTH)
    private DemandStatus status;

    /**
     * Demand's type.
     * <p>
     * Many times is more handy to work with {@link cz.poptavka.sample.domain.demand.DemandType#getType()}
     * instead of wrapper object.
     * <p>
     *     If this type is not specified then it should be automatically set to the "attractive" when storing
     *     new demand => @see {@link cz.poptavka.sample.service.demand.DemandServiceImpl#create(Demand)}
     */
    @Audited
    @ManyToOne(optional = false)
    private DemandType type;

    @ManyToOne
    @Cascade(CascadeType.ALL)
    private Client client;

    @ManyToMany
    @NotAudited
    @JoinTable(
        name = "DEMAND_LOCALITY",
        joinColumns = @JoinColumn(name = "DEMAND_ID"),
        inverseJoinColumns = @JoinColumn(name = "LOCALITY_ID")
    )
    private List<Locality> localities;

    @ManyToMany
    @NotAudited
    @JoinTable(
        name = "DEMAND_CATEGORY",
        joinColumns = @JoinColumn(name = "DEMAND_ID"),
        inverseJoinColumns = @JoinColumn(name = "CATEGORY_ID")
    )
    private List<Category> categories;

    @ManyToMany
    @NotAudited
    private List<Supplier> suppliers;

    @ManyToMany
    private List<Supplier> excludedSuppliers;

    /** The minimum rating that the supplier ({@link Supplier} has to have to participate in this demand. */
    private Integer minRating;

    /** TODO make the decision: replace this attribute with "maxOffers" - maximum number of offers that can be
     * send to the client from suppliers.
     *
     * The maximum number of suppliers that can participate in this demand. */
    private Integer maxSuppliers;

    /** Demand rating, that means evalution from supplier and client as well. */
    @OneToOne(fetch = FetchType.LAZY)
    @NotAudited
    private Rating rating;

    @OneToMany(mappedBy = "demand")
    @NotAudited
    private List<Offer> offers;

    //----------------------------------  Attributes for demands gathered from external systems ------------------------
    //----------------------------------  such as epoptavka.cz, aaapoptavka.cz, etc. -----------------------------------

    /** Arbitrary category specification for demands gathered from external system. */
    private String foreignCategory;

    /** Arbitrary URL that represents the link to the original demand gathered from external system. */
    private String foreignLink;

    @OneToOne
    @NotAudited
    private DemandOrigin origin;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getValidTo() {
        return validTo;
    }

    public void setValidTo(Date validTo) {
        this.validTo = validTo;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public void setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
    }

    public DemandStatus getStatus() {
        return status;
    }

    public void setStatus(DemandStatus status) {
        this.status = status;
    }

    public DemandType getType() {
        return type;
    }

    public void setType(DemandType type) {
        this.type = type;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public List<Locality> getLocalities() {
        return localities;
    }

    public void setLocalities(List<Locality> localities) {
        this.localities = localities;
    }


    public void addCategory(Category category) {
        this.categories.add(category);
    }

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> category) {
        this.categories = category;
    }

    public void addSupplier(Supplier supplier) {
        this.suppliers.add(supplier);
    }

    public void removeSupplier(Supplier supplier) {
        this.suppliers.remove(supplier);
    }

    public List<Supplier> getSuppliers() {
        return suppliers;
    }

    public void setSuppliers(List<Supplier> suppliers) {
        this.suppliers = suppliers;
    }

    public void addExcludedSupplier(Supplier supplier) {
        this.excludedSuppliers.add(supplier);
    }

    public void removeExcludedSupplier(Supplier supplier) {
        this.excludedSuppliers.remove(supplier);
    }

    public List<Supplier> getExcludedSuppliers() {
        return excludedSuppliers;
    }

    public void setExcludedSuppliers(List<Supplier> excludedSuppliers) {
        this.excludedSuppliers = excludedSuppliers;
    }

    public Integer getMinRating() {
        return minRating;
    }

    public void setMinRating(Integer minRating) {
        this.minRating = minRating;
    }

    public Integer getMaxSuppliers() {
        return maxSuppliers;
    }

    public void setMaxSuppliers(Integer maxSuppliers) {
        this.maxSuppliers = maxSuppliers;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public void setOffers(List<Offer> offers) {
        this.offers = offers;
    }

    public String getForeignCategory() {
        return foreignCategory;
    }

    public void setForeignCategory(String foreignCategory) {
        this.foreignCategory = foreignCategory;
    }

    public String getForeignLink() {
        return foreignLink;
    }

    public void setForeignLink(String foreignLink) {
        this.foreignLink = foreignLink;
    }

    public DemandOrigin getOrigin() {
        return origin;
    }

    public void setOrigin(DemandOrigin origin) {
        this.origin = origin;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Demand");
        sb.append("{id='").append(getId()).append('\'');
        sb.append("{title='").append(title).append('\'');
        sb.append("{description='").append(description).append('\'');
        sb.append("{price='").append(price).append('\'');
        sb.append(", status=").append(status);
        sb.append(", type=").append(type);
        sb.append(", client=").append(ToStringUtils.printId(client));
        sb.append('}');
        return sb.toString();
    }
}
