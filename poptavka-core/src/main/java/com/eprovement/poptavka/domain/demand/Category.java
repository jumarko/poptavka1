package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.common.TreeItem;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import java.util.List;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * Categorizes goods and services the portal supports.
 * Categories form a tree structure.
 * They denote the area of interest of a supplier and the area within which a demand falls
 * and may be used to link potential suppliers to demands.
 *
 * @author Juraj Martinka
 *         Date: 31.1.11
 *
 * @see TreeItem
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "getRootCategories", query = "from Category c where c.parent is null order by c.name"),
    @NamedQuery(name = "getCategoryById", query = "from Category c where c.id = :id"),
    @NamedQuery(name = "getCategoriesByMaxLength",
        query = "select category "
        + " from Category category\n"
        + "where LENGTH(category.name) < :length"
        + " and lower(category.name) like lower(:name)"),
    @NamedQuery(name = "getCategoriesByMinLength",
        query = "select category "
        + " from Category category\n"
        + "where LENGTH(category.name) >= :length"
        + " and lower(category.name) like lower(:name)"),
    @NamedQuery(name = Category.INCREMENT_DEMAND_COUNT,
        query = "UPDATE Category SET demandCount = demandCount +  1 WHERE id IN :ids"),
    @NamedQuery(name = Category.DECREMENT_DEMAND_COUNT,
        query = "UPDATE Category SET demandCount = demandCount -  1 WHERE id IN :ids"),
    @NamedQuery(name = Category.INCREMENT_SUPPLIER_COUNT,
        query = "UPDATE Category SET supplierCount = supplierCount +  1 WHERE id IN :ids"),
    @NamedQuery(name = Category.DECREMENT_SUPPLIER_COUNT,
        query = "UPDATE Category SET supplierCount = supplierCount -  1 WHERE id IN :ids")
})
public class Category extends TreeItem {
    
    public static final String INCREMENT_DEMAND_COUNT = "Category.INCREMENT_DEMAND_COUNT";
    public static final String DECREMENT_DEMAND_COUNT = "Category.DECREMENT_DEMAND_COUNT";
    public static final String INCREMENT_SUPPLIER_COUNT = "Category.INCREMENT_SUPPLIER_COUNT";
    public static final String DECREMENT_SUPPLIER_COUNT = "Category.DECREMENT_SUPPLIER_COUNT";

    /**************************************************************************/
    /*  Attributes                                                            */
    /**************************************************************************/
    /** The name of category. */
    @Column(length = 64)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @NotAudited
    private Template template;

    @Min(0)
    @NotNull
    private Integer demandCount = 0;

    @Min(0)
    @NotNull
    private Integer supplierCount = 0;

    /** Reference to the parent tree item. */
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    /** All children of this tree item in tree structure. */
    @OneToMany(mappedBy = "parent")
    private List<Category> children;

    /**************************************************************************/
    /*  Getters & Setters                                                     */
    /**************************************************************************/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public List<Category> getChildren() {
        return children;
    }

    public void setChildren(List<Category> children) {
        this.children = children;
    }

    public Template getTemplate() {
        return template;
    }

    public void setTemplate(Template template) {
        this.template = template;
    }

    public Integer getDemandCount() {
        return demandCount;
    }

    public Integer getSupplierCount() {
        return supplierCount;
    }

    /**************************************************************************/
    /*  Override methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Category");
        sb.append("(name='").append(name).append("'");
        sb.append(", description='").append(description).append("'");
        sb.append(", demandCount='").append(demandCount);
        sb.append(", supplierCount='").append(supplierCount);
        sb.append('}');
        return sb.toString();
    }
}
