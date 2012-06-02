package com.eprovement.poptavka.domain.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Base class that enables working with tree data structure. Objects that extends this class can be placed
 * within n-ary trees.
 *
 * <p>
 * The class implements the optimization which uses <code>leftBound</code> and <code>rightBound</code>.
 * Instead of simply working with tree we use the "Subset" view where all children of some tree time
 * are within its bounds - i.e. they are between tree item's <code>leftBound</code> and <code>rightBound</code>.
 *
 * See <a href="http://dev.mysql.com/tech-resources/articles/hierarchical-data.html">this documentation</a>
 * for more information.
 *
 * <p>
 *     The descendant of this class must override abstract methods and maps related fields
 *     (<code>children</code> and <code>parent</code>).
 *     These fields cannot be mapped in this superclass because then the strange errors occurs
 *     - e.g. the method <code>getChildren()</code>
 *     returns both categories and localities when called on Locality object.
 *
 * The case where there is a references to the parent is not allowed to be presented in @MappedSuperclass.
 *
 * See <a href="http://opensource.atlassian.com/projects/hibernate/browse/EJB-199">this jira discussion</a>
 * for further explanation.
 *
 * @author Juraj Martinka
 *         Date: 31.1.11
 */
@MappedSuperclass
public abstract class TreeItem extends DomainObject {

    /*
     * Mapping of parent and children is not allowed in superclass - must be defined in descendant!
    */

//    @ManyToOne(fetch = FetchType.LAZY)
//    private TreeItem parent;
//
//    /** All children of this tree item in tree structure. */
//    @OneToMany(mappedBy = "parent")
//    private List<TreeItem> children;


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

}
