package com.eprovement.poptavka.shared.selectors.catLocSelector;

import com.google.gwt.view.client.ProvidesKey;

public class CatLocDetail implements ICatLocDetail {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long id;
    private String name;
    private String parentName;
    private long demandsCount;
    private long suppliersCount;
    //category level
    private int level = -1;
    //if parent = false, no child category exists;
    private boolean leaf = false;
    /**
     * The key provider that provides the unique ID of a FullSupplierDetail.
     */
    public static final ProvidesKey<ICatLocDetail> KEY_PROVIDER = new ProvidesKey<ICatLocDetail>() {
        @Override
        public Object getKey(ICatLocDetail item) {
            return item == null ? null : item.getId();
        }
    };

    /**************************************************************************/
    /* Initialization                                                          */
    /**************************************************************************/
    private CatLocDetail() {
    }

    public CatLocDetail(Long id, String name) {
        init(id, name, 0, 0);
    }

    public CatLocDetail(Long id, String name, long demands, long suppliers) {
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
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getParentName() {
        return parentName;
    }

    @Override
    public long getDemandsCount() {
        return demandsCount;
    }

    @Override
    public long getSuppliersCount() {
        return suppliersCount;
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public boolean isLeaf() {
        return leaf;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    @Override
    public void setDemandsCount(long demandsCount) {
        this.demandsCount = demandsCount;
    }

    @Override
    public void setSuppliersCount(long suppliersCount) {
        this.suppliersCount = suppliersCount;
    }

    @Override
    public void setLevel(int level) {
        this.level = level;
    }

    @Override
    public void setLeaf(boolean isLeaf) {
        this.leaf = isLeaf;
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    /**
     * Use to display CategoryDetail name in TextCell.
     * @return updated name
     */
    @Override
    public String toString() {
        return name.replaceAll("-", " ");
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CatLocDetail other = (CatLocDetail) obj;
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
