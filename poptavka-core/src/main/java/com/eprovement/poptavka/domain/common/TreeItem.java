package com.eprovement.poptavka.domain.common;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 * Base class that enables working with tree data structure. Objects that extend this class can be represented
 * as n-ary trees.
 *
 * <p>
 * The class implements the optimization which uses <code>leftBound</code> and <code>rightBound</code>.
 * Instead of simply working with a tree, we use the "Subset" view where all children of some tree time
 * are within its bounds - i.e. they are between tree item's <code>leftBound</code> and <code>rightBound</code>.
 *
 * See <a href="http://dev.mysql.com/tech-resources/articles/hierarchical-data.html">this documentation</a>
 * for more information.
 *
 * <p>
 *     The descendant of this class must override abstract methods and maps related fields
 *     (<code>children</code> and <code>parent</code>).
 *     These fields cannot be mapped in this superclass because then various errors occur
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
     * Mapping of parent and children is not allowed in this superclass - must be defined in descendants!
    */

//    @ManyToOne(fetch = FetchType.LAZY)
//    private TreeItem parent;
//
//    /** All children of this tree item in tree structure. */
//    @OneToMany(mappedBy = "parent")
//    private List<TreeItem> children;


    private Long treeId;

    /** The depth from root TreeItem. */
    private int level;

    /** The id of the leftmost descendant */
    @Column(name = "leftBound")
    private Long leftBound;

    /** The id of the rightmost descendant */
    @Column(name = "rightBound")
    private Long rightBound;

    public Long getTreeId() {
        return treeId;
    }

    public void setTreeId(Long treeId) {
        this.treeId = treeId;
    }

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


    /**
     * Computes whether this item is a leaf or not. This should be a very quick computation and does not interact
     * with the database.
     * @return true if this item is a leaf, false otherwise
     */
    public boolean isLeaf() {
        return Math.abs(leftBound - rightBound) == 1;
    }
}
