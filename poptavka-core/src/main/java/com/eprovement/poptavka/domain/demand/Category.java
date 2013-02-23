package com.eprovement.poptavka.domain.demand;

import com.eprovement.poptavka.domain.common.AdditionalInfo;
import com.eprovement.poptavka.domain.common.AdditionalInfoAware;
import com.eprovement.poptavka.domain.common.TreeItem;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.envers.NotAudited;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

/**
 * @author Juraj Martinka
 *         Date: 31.1.11
 *
 * @see TreeItem
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "getRootCategories", query = "from Category c where c.parent is null order by c.name"),
        @NamedQuery(name = "getCategoryById", query = "from Category c where c.id = :id")
})
public class Category extends TreeItem implements AdditionalInfoAware {

    /** The name of category. */
    @Column(length = 64)
    private String name;

    private String description;

    @OneToOne
    @Cascade(CascadeType.ALL)
    private AdditionalInfo additionalInfo;

    @ManyToOne
    @NotAudited
    private Template template;


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

    public AdditionalInfo getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(AdditionalInfo additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Category");
        sb.append("(name='").append(name).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", additionalInfo=").append(additionalInfo);
        sb.append('}');
        return sb.toString();
    }


    //---------------------- TreeItem attributes and methods ----------------------------------------------------------
    /** Reference to the parent tree item. */
    @ManyToOne(fetch = FetchType.LAZY)
    private Category parent;

    /** All children of this tree item in tree structure. */
    @OneToMany(mappedBy = "parent")
    private List<Category> children;

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
}

