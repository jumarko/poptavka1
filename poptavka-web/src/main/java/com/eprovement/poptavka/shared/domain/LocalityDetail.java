package com.eprovement.poptavka.shared.domain;

import com.eprovement.poptavka.domain.enums.LocalityType;
import com.google.gwt.view.client.ProvidesKey;

/**
 * Low-bandwitdh representation of Locality designed for direct use on frontend.
 *
 * @author Beho, Martin Slavkovsky
 *
 */
public class LocalityDetail implements IListDetailObject {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    /**
     * Generated serialVersionUID.
     */
    private static final long serialVersionUID = 8480517227278818048L;
    private LocalityDetail parent;
    private long id;
    private String name;
    private String code;
    private LocalityType localityType;
    private long demandsCount;
    private long suppliersCount;
    //category level
    private int level = -1;
    //if parent = false, no child category exists;
    private boolean leaf = false;
    /**
     * The key provider that provides the unique ID of a FullSupplierDetail.
     */
    public static final ProvidesKey<LocalityDetail> KEY_PROVIDER = new ProvidesKey<LocalityDetail>() {
        @Override
        public Object getKey(LocalityDetail item) {
            return item == null ? null : item.getCode();
        }
    };

    /**************************************************************************/
    /* Initialize                                                             */
    /**************************************************************************/
    public LocalityDetail() {
    }

    public LocalityDetail(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public LocalityDetail(Long id, String name, String code) {
        this.id = id;
        this.name = name;
        this.code = code;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    public LocalityDetail getParent() {
        return parent;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getCode() {
        return code;
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

    public LocalityType getLocalityType() {
        return localityType;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    public void setParent(LocalityDetail parent) {
        this.parent = parent;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
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

    public boolean isLeaf() {
        return leaf;
    }

    public void setLeaf(boolean leaf) {
        this.leaf = leaf;
    }

    public void setLocalityType(LocalityType localityType) {
        this.localityType = localityType;
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    @Override
    public String toString() {
        return name;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.code != null ? this.code.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LocalityDetail other = (LocalityDetail) obj;
        if ((this.code == null) ? (other.code != null) : !this.code.equals(other.code)) {
            return false;
        }
        return true;
    }
}
