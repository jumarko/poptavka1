package cz.poptavka.sample.domain.common;

import javax.persistence.Column;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.MappedSuperclass;

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
 *     The descendant of this class must override abstract methods and maps related fields. These fields cannot
 *     be mapped in this supperclass because then the strange errors occurs - e.g. the method {@@link #getChildren()}
 *     returns both categories and localities when called on Locality object.
 *
 * This class uses TABLE_PER_CLASS strategy because there is a references to the parent - this case is not allowed
 * to be presented in @MappedSuperclass.
 *
 * See <a href="http://opensource.atlassian.com/projects/hibernate/browse/EJB-199">this jira discussion</a>
 * for further explanation.
 *
 * @author Juraj Martinka
 *         Date: 31.1.11
 */
//@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@MappedSuperclass
public abstract class TreeItem extends DomainObject {

    /** The depth from root TreeItem. */
    private int level;


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


//    //--------------------- abstract methods to enforce implementation of problematic fields in subclass -------------
//    public abstract T getParent();
//
//    public abstract void setParent(T parent);
//
//    public abstract List<T> getChildren();
//
//    public abstract void setChildren(List<T> children);

}
