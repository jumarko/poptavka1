package com.eprovement.poptavka.domain.address;

import com.eprovement.poptavka.domain.common.TreeItem;
import com.eprovement.poptavka.domain.demand.Demand;
import com.eprovement.poptavka.domain.enums.LocalityType;
import com.eprovement.poptavka.util.orm.OrmConstants;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Some type of locality.
 * Typical examples can be country, region ,district, etc.
 * @author Juraj Martinka
 *         Date: 29.1.11
 *
 * @see TreeItem
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getLocalitiesByMaxLength",
        query = "select locality "
        + " from Locality locality\n"
        + "where LENGTH(locality.name) < :length"
        + " and lower(locality.name) like lower(:name)"),
    @NamedQuery(name = "getLocalitiesByMinLength",
        query = "select locality "
        + " from Locality locality\n"
        + "where LENGTH(locality.name) >= :length"
        + " and lower(locality.name) like (:name)"),
    @NamedQuery(name = "getLocalitiesByMaxLengthAndType",
        query = "select locality "
        + " from Locality locality\n"
        + "where LENGTH(locality.name) < :length"
        + " and locality.type = :type"
        + " and lower(locality.name) like lower(:name)"),
    @NamedQuery(name = "getLocalitiesByMinLengthAndType",
        query = "select locality "
        + " from Locality locality\n"
        + "where LENGTH(locality.name) >= :length"
        + " and locality.type = :type"
        + " and lower(locality.name) like (:name)"),
    @NamedQuery(name = Locality.INCREMENT_DEMAND_COUNT,
        query = "UPDATE Locality SET demandCount = demandCount +  1 WHERE id IN :ids"),
    @NamedQuery(name = Locality.DECREMENT_DEMAND_COUNT,
        query = "UPDATE Locality SET demandCount = demandCount -  1 WHERE id IN :ids"),
    @NamedQuery(name = Locality.INCREMENT_SUPPLIER_COUNT,
        query = "UPDATE Locality SET supplierCount = supplierCount +  1 WHERE id IN :ids"),
    @NamedQuery(name = Locality.DECREMENT_SUPPLIER_COUNT,
        query = "UPDATE Locality SET supplierCount = supplierCount -  1 WHERE id IN :ids")
})
public class Locality extends TreeItem {

    public static final String INCREMENT_DEMAND_COUNT = "Locality.INCREMENT_DEMAND_COUNT";
    public static final String DECREMENT_DEMAND_COUNT = "Locality.DECREMENT_DEMAND_COUNT";
    public static final String INCREMENT_SUPPLIER_COUNT = "Locality.INCREMENT_SUPPLIER_COUNT";
    public static final String DECREMENT_SUPPLIER_COUNT = "Locality.DECREMENT_SUPPLIER_COUNT";

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    private String name;

    @Enumerated(value = EnumType.STRING)
    @Column(length = OrmConstants.ENUM_FIELD_LENGTH)
    private LocalityType type;

    @ManyToMany(mappedBy = "localities")
    private List<Demand> demands;

    @Min(0)
    @NotNull
    private Integer demandCount = 0;

    @Min(0)
    @NotNull
    private Integer supplierCount = 0;

    /** Reference to the parent tree item. */
    @ManyToOne(fetch = FetchType.EAGER)
    private Locality parent;

    /** All children of this tree item in tree structure. */
    @OneToMany(mappedBy = "parent")
    private List<Locality> children;

    /**************************************************************************/
    /*  Initialization                                                        */
    /**************************************************************************/
    public Locality(String name, LocalityType type) {
        this.name = name;
        this.type = type;
    }

    public Locality() {
    }

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    /**************************************************************************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Get demands DIRECTLY associated to this locality.
     * <p>
     * Watch out that this method does NOT return ALL demands related to this locality, i.e. it does not return
     * any demands that belong to the sublocalities of this locality.
     * For this purpose use {@link com.eprovement.poptavka.service.demand.DemandService#getDemands(Locality...)}.
     *
     * @see com.eprovement.poptavka.service.demand.DemandService#getDemands(Locality...)
     * @return
     */
    public List<Demand> getDemands() {
        return demands;
    }

    public void setDemands(List<Demand> demands) {
        this.demands = demands;
    }

    public LocalityType getType() {
        return type;
    }

    public void setType(LocalityType type) {
        this.type = type;
    }

    public Locality getParent() {
        return parent;
    }

    public void setParent(Locality parent) {
        this.parent = parent;
    }

    public List<Locality> getChildren() {
        return children;
    }

    public void setChildren(List<Locality> children) {
        this.children = children;
    }

    public Integer getDemandCount() {
        return demandCount;
    }

    public void setDemandCount(Integer demandCount) {
        this.demandCount = demandCount;
    }

    public Integer getSupplierCount() {
        return supplierCount;
    }

    public void setSupplierCount(Integer supplierCount) {
        this.supplierCount = supplierCount;
    }

    /**************************************************************************/
    /*  Override methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Locality");
        sb.append("{id='").append(getId()).append("'");
        sb.append(", name='").append(name).append("'");
        sb.append(", type=").append(type);
        sb.append(", demandCount='").append(demandCount);
        sb.append(", supplierCount='").append(supplierCount);
        sb.append('}');
        return sb.toString();
    }
}
