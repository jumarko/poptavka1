/*
 * Copyright (C) 2014, eProvement s.r.o. All rights reserved.
 */
package com.eprovement.poptavka.shared.selectors.catLocSelector;

import com.google.gwt.view.client.ProvidesKey;

/**
 * Lesser detail object for Home Welcome view to display root category names only.
 * @author ivlcek
 */
public class LesserCatLocDetail implements ILesserCatLocDetail {

    /**************************************************************************/
    /* Attributes                                                             */
    /**************************************************************************/
    private long id;
    private String categoryName;
    private long demandsCount;
    /**
     * The key provider that provides the unique ID of a LesserCatLocDetail object.
     */
    public static final ProvidesKey<ILesserCatLocDetail> KEY_PROVIDER = new ProvidesKey<ILesserCatLocDetail>() {
        @Override
        public Object getKey(ILesserCatLocDetail item) {
            return item == null ? null : item.getId();
        }
    };

    /**************************************************************************/
    /* Initialization                                                          */
    /**************************************************************************/
    private LesserCatLocDetail() {
    }

    public LesserCatLocDetail(Long id, String categoryName, long demandsCount) {
        init(id, categoryName, demandsCount);
    }

    private void init(Long catId, String categoryName, long demandCount) {
        this.id = catId;
        this.categoryName = categoryName;
        this.demandsCount = demandCount;
    }

    /**************************************************************************/
    /* Getters                                                                */
    /**************************************************************************/
    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    @Override
    public long getDemandsCount() {
        return demandsCount;
    }

    /**************************************************************************/
    /* Setters                                                                */
    /**************************************************************************/
    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public void setDemandsCount(long demandsCount) {
        this.demandsCount = demandsCount;
    }

    /**************************************************************************/
    /* Overriden methods                                                      */
    /**************************************************************************/
    /**
     * Use to display LesserCatLocDetail's name in Home Welcome view containing root categories.
     * @return root category name
     */
    @Override
    public String toString() {
        return categoryName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final LesserCatLocDetail other = (LesserCatLocDetail) obj;
        if (this.id != other.id) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 68 * hash + (int) (this.id ^ (this.id >>> 32));
        return hash;
    }
}
