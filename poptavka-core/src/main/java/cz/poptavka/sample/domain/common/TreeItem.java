package cz.poptavka.sample.domain.common;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;

/**
 * Base class that enables working with tree data structure. Objects that extends this class can be placed
 * withing n-ary trees.
 *
 * <p>
 * The class implements the optimalization which uses <code>leftBound</code> and <code>rightBound</code>.
 * Instead of simply working with tree we use the "Subset" view where all children of some tree time
 * are within its bounds - i.e. they are between tree item's <code>leftBound</code> and <code>rightBound</code>.
 *
 * See <a href="http://dev.mysql.com/tech-resources/articles/hierarchical-data.html">this documentation</a>
 * for more information.
 *
 * <p>
 * This class uses TABLE_PER_CLASS strategy because there is a references to the parent - this case is not allowed
 * to be presented in @MappedSuperclass.
 *
 * See <a href="http://opensource.atlassian.com/projects/hibernate/browse/EJB-199">this jira discussion</a>
 * for further explanation.
 *
 * @author Juraj Martinka
 *         Date: 31.1.11
 */
@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class TreeItem extends DomainObject {

    /** The depth from root TreeItem. */
    private int level;

    /** Reference to the parent tree item. */
    @ManyToOne(fetch = FetchType.LAZY)
    private TreeItem parent;

    /** All children of this tree item in tree structure. */
    @OneToMany(mappedBy = "parent")
    private List<TreeItem> children;

    /** Reference to the left sibling - at the same level. */
    @Column(name = "leftBound")
    private Long leftBound;

    /** Reference to the right sibling - at the same level. */
    @Column(name = "rightBound")
    private Long rightBound;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public TreeItem getParent() {
        return parent;
    }

    public void setParent(TreeItem parent) {
        this.parent = parent;
    }

    public List<TreeItem> getChildren() {
        return children;
    }

    public void setChildren(List<TreeItem> children) {
        this.children = children;
    }

    public Long getLeftBound() {
        return leftBound;
    }

    public void setLeftBound(Long leftBound) {
        this.leftBound = leftBound;
    }

    public Long getRightBound() {
        return rightBound;
    }

    public void setRightBound(Long rightBound) {
        this.rightBound = rightBound;
    }

}
