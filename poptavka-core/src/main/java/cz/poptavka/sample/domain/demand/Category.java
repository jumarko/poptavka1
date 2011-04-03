package cz.poptavka.sample.domain.demand;

import cz.poptavka.sample.domain.common.AdditionalInfo;
import cz.poptavka.sample.domain.common.AdditionalInfoAware;
import cz.poptavka.sample.domain.common.TreeItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
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
        @NamedQuery(name = "getCategoryByCode", query = "from Category c where c.code = :code")
})
public class Category extends TreeItem implements AdditionalInfoAware {

    /** Business identifier of category. */
    @Column(length = 64)
    private String code;

    /** The name of category. */
    @Column(length = 64)
    private String name;

    private String description;

    private AdditionalInfo additionalInfo;


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
        sb.append("{code='").append(code).append('\'');
        sb.append(", name='").append(name).append('\'');
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
}

