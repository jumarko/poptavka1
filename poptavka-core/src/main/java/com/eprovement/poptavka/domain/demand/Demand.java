/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.address.Locality;
import com.eprovement.poptavka.domain.common.DomainObject;
import com.eprovement.poptavka.domain.enums.DemandStatus;
import com.eprovement.poptavka.domain.offer.Offer;
import com.eprovement.poptavka.domain.user.Client;
import com.eprovement.poptavka.domain.user.Supplier;
import com.eprovement.poptavka.util.orm.OrmConstants;
import com.eprovement.poptavka.util.strings.ToStringUtils;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.hibernate.search.annotations.Boost;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.validator.constraints.NotBlank;

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
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
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
                query = "select count(*) from Demand"),
    @NamedQuery(name = "getDemandsForCategoriesAndLocalities",
                query = "select demandCategory.demand"
                        + " from DemandCategory demandCategory,"
                        + " DemandLocality demandLocality"
                        + " where demandCategory.demand ="
                        + " demandLocality.demand"
                        + " and demandCategory.category.id in (:categoryIds)"
                        + " and demandLocality.locality.id in (:localityIds)"),
    @NamedQuery(name = "getDemandsCountForCategoriesAndLocalities",
                query = "select count(demandCategory.demand)"
                        + " from DemandCategory demandCategory,"
                        + " DemandLocality demandLocality"
                        + " where demandCategory.demand ="
                        + " demandLocality.demand"
                        + " and demandCategory.category.id in (:categoryIds)"
                        + " and demandLocality.locality.id in (:localityIds)"),
        @NamedQuery(name = "getDemandsForCategoriesAndLocalitiesIncludingParents",
                query = "select demandCategory.demand"
                        + " from DemandCategory demandCategory,"
                        + " DemandLocality demandLocality"
                        + " where demandCategory.demand ="
                        + " demandLocality.demand"
                        + " and exists (select c.id from Category c "
                        + "where c.leftBound >= demandCategory.category.leftBound"
                        + " and c.rightBound <= demandCategory.category.rightBound "
                        + "and c.id in (:categoryIds))"
                        + " and exists (select l.id from Locality l "
                        + "where l.leftBound >= demandLocality.locality.leftBound"
                        + " and l.rightBound <= demandLocality.locality.rightBound"
                        + " and l.id in (:localityIds))"),
    @NamedQuery(name = "getClientDemandsWithOfferCount",
                query = "select count(*) from Demand as demand, Offer as offer"
                        + " where demand.client = :client"
                        + " and offer.demand = demand"),
    @NamedQuery(name = "getClientDemandsWithOffer",
                query = "select demand from Demand as demand, Offer as offer"
                        + " where demand.client = :client"
                        + " and offer.demand = demand"),
        @NamedQuery(name = "getClientDemandsWithUnreadSubMsgs",
                query = "select demand, ("
                        + "select count(subUserMessage.id) from UserMessage as subUserMessage"
                        + " left join subUserMessage.message.offer as offer\n"
                        + "where subUserMessage.message.demand = demand"
                        + " and subUserMessage.isRead = false"
                        + " and offer is null"
                        + " and subUserMessage.user = :user"
                        + ")\n"
                        + "from Demand as demand\n"
                        + "where demand.client.businessUser = :user"
                        + " and (demand.status = 'ACTIVE' or demand.status = 'OFFERED')"),
        @NamedQuery(name = "getClientDemandsCount",
                query = "select count(demand.id)\n"
                        + "from Demand as demand\n"
                        + "where demand.client.businessUser = :user"
                        + " and (demand.status = 'ACTIVE' or demand.status = 'OFFERED'"
                        + " or demand.status = 'NEW' or demand.status = 'INACTIVE'"
                        + " or demand.status = 'INVALID')"),
        @NamedQuery(name = "getClientOfferedDemandsWithUnreadOfferSubMsgs",
                query = "select demand, ("
                        + "select count(subUserMessage.id) from UserMessage as subUserMessage"
                        + " inner join subUserMessage.message.offer as offer\n"
                        + "where subUserMessage.message.demand = demand"
                        + " and subUserMessage.isRead = false"
                        + " and subUserMessage.user = :user"
                        + ")\n"
                        + "from Demand as demand\n"
                        + "where demand.client.businessUser = :user"
                        + " and demand.status = 'OFFERED'"),
        @NamedQuery(name = "getClientOfferedDemands",
                query = "select count(demand.id)\n"
                        + "from Demand as demand\n"
                        + "where demand.client.businessUser = :user"
                        + " and demand.status = 'OFFERED'"),

})
public class Demand extends DomainObject {

    /** Fields that are available for full-text search. */
    public static final String[] DEMAND_FULLTEXT_FIELDS = new String[] {"title" , "description"};

    @Column(length = 100, nullable = false)
    @Field
    // title is most important - boost twice as much as other fields
    @Boost(2)
    @NotBlank
    @Size(min = 5)
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
    @Field
    @NotBlank
    @Size(min = 20)
    private String description;

    @DecimalMin(value = "0")
    private BigDecimal price;

    /** The max date when the demand should be finished. */
    @Temporal(value = TemporalType.TIMESTAMP)
    @Past
    private Date createdDate = new Date();

    /** The max date when the demand should be finished. */
    @Temporal(value = TemporalType.DATE)
    private Date endDate;

    /** This demand is considered to be valid in system until this day. */
    @Temporal(value = TemporalType.DATE)
    private Date validTo;

    /** Path in the storage file system where all attachments are saved. */
    private String attachmentPath;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    @NotNull
    private DemandStatus status;

    /**
     * Demand's type.
     * <p>
     * Many times is more handy to work with {@link DemandType#getType()}
     * instead of wrapper object.
     * <p>
     *     If this type is not specified then it should be automatically set to the "attractive" when storing
     *     new demand => @see {@link com.eprovement.poptavka.service.demand.DemandServiceImpl#create(Demand)}
     */
    @Audited
    @ManyToOne(optional = false)
    @NotNull
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
    @JoinTable(
        name = "DEMAND_EXCLUDED_SUPPLIER",
        joinColumns = @JoinColumn(name = "DEMAND_ID"),
        inverseJoinColumns = @JoinColumn(name = "SUPPLIER_ID")
    )
    private List<Supplier> excludedSuppliers;

    /** The minimum rating that the supplier ({@link Supplier} has to have to participate in this demand. */
    private Integer minRating;

    /** The maximum number of suppliers that will receive notification about this demand. */
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

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
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
