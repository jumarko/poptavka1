package com.eprovement.poptavka.shared.domain;

import com.google.gwt.view.client.ProvidesKey;

public class CategoryDetail implements IListDetailObject {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = -8677227400869014970L;
    private long id;
    private String name;
    private long demandsCount;
    private long suppliersCount;
    //category level
    private int level = -1;
    //if parent = false, no child category exists;
    private boolean leaf = false;
    /**
     * The key provider that provides the unique ID of a FullSupplierDetail.
     */
    public static final ProvidesKey<CategoryDetail> KEY_PROVIDER = new ProvidesKey<CategoryDetail>() {
        @Override
        public Object getKey(CategoryDetail item) {
            return item == null ? null : item.getId();
        }
    };

    /**************************************************************************/
    /* Initialization                                                          */
    /**************************************************************************/
    public CategoryDetail() {
    }

    public CategoryDetail(Long id, String name) {
        init(id, name, 0, 0);
    }

    public CategoryDetail(Long id, String name, long demands, long suppliers) {
        init(id, name, demands, suppliers);
    }

    private void init(Long catId, String nameString, long demandCount, long supplierCount) {
        this.id = catId;
        this.name = nameString;
        this.demandsCount = demandCount;
        this.suppliersCount = supplierCount;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public long getDemandsCount() {
        return demandsCount;
    }

    public long getSuppliersCount() {
        return suppliersCount;
    }

    public int getLevel() {
        return level;
    }

    public boolean isLeaf() {
        return leaf;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDemandsCount(long demandsCount) {
        this.demandsCount = demandsCount;
    }

    public void setSuppliersCount(long suppliersCount) {
        this.suppliersCount = suppliersCount;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setLeaf(boolean isLeaf) {
        this.leaf = isLeaf;
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CategoryDetail other = (CategoryDetail) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 67 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
